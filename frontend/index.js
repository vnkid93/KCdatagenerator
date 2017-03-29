var CONST = require('./conf.js');

var express = require('express');
var bodyParser =  require('body-parser');
var app = express();
var ejs = require('ejs');
var session = require('client-sessions');

var imageCache

// node-java
var fs = require("fs");
var java = require("java");
// dependency if maven was used
var baseDir = "./target/lib";
var dependencies = fs.readdirSync(baseDir);
dependencies.forEach(function(dependency){
  java.classpath.push(baseDir + "/" + dependency);
});
// java api
java.classpath.push("target/api.jar");



app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(express.static(__dirname));
app.set('view engine', 'ejs');
app.use(session({
  cookieName: 'session',
  secret: 'random_string_goes_here',
  duration: 30 * 60 * 1000,
  activeDuration: 5 * 60 * 1000,
}));
// *******************************************
var index       = require('./pages/index.js');
var accountgen  = require('./pages/accountgen.js');
var emailgen    = require('./pages/emailgen.js');
var contactgen  = require('./pages/contactgen.js');
var eventgen    = require('./pages/eventgen.js');
var taskgen     = require('./pages/taskgen.js');
var notegen     = require('./pages/notegen.js');

serverGet("");
serverGet(CONST.PAGE_ACCOUNTGEN);
serverGet(CONST.PAGE_EMAILGEN);
serverGet(CONST.PAGE_EVENTGEN);
serverGet(CONST.PAGE_CONTACTGEN);
serverGet(CONST.PAGE_TASKGEN);
serverGet(CONST.PAGE_NOTEGEN);

serverPost("");
serverPost(CONST.PAGE_ACCOUNTGEN);
serverPost(CONST.PAGE_EMAILGEN);
serverPost(CONST.PAGE_EVENTGEN);
serverPost(CONST.PAGE_CONTACTGEN);
serverPost(CONST.PAGE_TASKGEN);
serverPost(CONST.PAGE_NOTEGEN);

// run the server
app.listen(33344, function(){
  console.log('Application is running on 33344');
});


// ************************* functions ****************
/* get listener */
function serverGet(path){
  app.get('/'+path, function(req, res){
    var start = new Date().getTime();
    if(path.length == 0)  path = CONST.PAGE_INDEX;
    if(req.query.reset == undefined){
      var error = processQueryGet(path, req, java);
      if(error != undefined){
        setErrorCode(req, error);
      }
    }
    res.render('template.ejs', {
      page: path,
      query: req.query,
      params: req.params,
      session: req.session
    });
    var time = new Date().getTime() - start;
    console.log("The time: "+time);
  });
}

function serverPost(path){
  app.post('/'+path, function(req, res){
    if(req.body.submit != undefined && req.body.logoutbtn == undefined){ // submit
      var error = index.post(req, java);
      if(error != undefined){
        setErrorCode(req, error);
      }
    }else if(req.body.logoutbtn != undefined){ // logout
      logout(req.session);
    }
    res.render('template.ejs', {
      page: CONST.PAGE_INDEX,
      query: req.body,
      params: req.params,
      session: req.session
    });
  });
}


function processQueryGet(page, req, java){
  var result = undefined;
  var query = req.query;
  var javaBridge = java.newInstanceSync("bridge.NodejsBridge");
  if(Object.keys(query).length > 0 && authenticate(req.session, javaBridge) && query.submit != undefined){
    if(page == CONST.PAGE_INDEX){
      result = index.get(req, java, javaBridge);
    }else if(page == CONST.PAGE_ACCOUNTGEN){
      result = accountgen.get(req, java, javaBridge);
    }else if(page == CONST.PAGE_EMAILGEN){
      result = emailgen.get(req, java, javaBridge);
    }else if(page == CONST.PAGE_CONTACTGEN){
      result = contactgen.get(req, java, javaBridge);
    }else if(page == CONST.PAGE_EVENTGEN){
      result = eventgen.get(req, java, javaBridge);
    }else if(page == CONST.PAGE_TASKGEN){
      result = taskgen.get(req, java, javaBridge);
    }else if(page == CONST.PAGE_NOTEGEN){
      result = notegen.get(req, java, javaBridge);
    }
  }else if(Object.keys(query).length > 0){
    console.log("*********************xxxxxxxxxxxxxxxxxx");
    setErrorCode(req, 'You are not connected to Kerio Connect.');
  }
  req.params.isLogged = isLogged(req.session);
  return result;
}


function setErrorCode(req, message){
  req.params.error = message;
}

function setSuccessCode(req, message){
  req.params.success = message;
}

function authenticate(session, javaBridge){
  if(isLogged(session)){
    if(javaBridge.loginToAllSync(session.host, session.adminLogin, session.adminPass, session.userLogin, session.userPass)){
      return true;
    }else{
      return false;
    }
  }else{
    return false;
  }
}

function isLogged(session){
  return (session.host != undefined);
}

function logout(session){
  session.destroy();
}
