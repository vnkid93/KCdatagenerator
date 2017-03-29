var CONST = require('./../conf.js');
module.exports = {
  get : function(req, java, javaBridge){
    console.log('nothing here');
  },
  post : function(req, java){
    req.params.isLogged = false;
    if(req.body.hostaddr == undefined   || req.body.hostaddr.trim().length == 0)  return 'Host is not filled';
    if(req.body.aduser == undefined     || req.body.aduser.trim().length == 0)  return 'Administrator login is not filled';
    if(req.body.adpass == undefined     || req.body.adpass.length == 0)  return 'Administrator password is not filled';
    if(req.body.clientuser == undefined || req.body.clientuser.trim().length == 0)  return 'User login is not filled';
    if(req.body.clientpass == undefined || req.body.clientpass.length == 0)  return 'User password is not filled';

    var hostAddress   = req.body.hostaddr.trim();
    var adminLogin    = req.body.aduser.trim();
    var adminPassword = req.body.adpass;
    var userLogin     = req.body.clientuser.trim();
    var userPassword  = req.body.clientpass;

    req.session.host = hostAddress;
    req.session.adminLogin = adminLogin;
    req.session.adminPass = adminPassword;
    req.session.userLogin = userLogin;
    req.session.userPass = userPassword;
    var javaBridge = java.newInstanceSync("bridge.NodejsBridge");
    if(javaBridge.loginToAllSync(hostAddress, adminLogin, adminPassword, userLogin, userPassword)){
      req.params.isLogged = true;
      req.session.success = 'Connection established';
      return undefined;
    }else{
      return 'Not connected. Please, check your login.';
    }
  }
}
