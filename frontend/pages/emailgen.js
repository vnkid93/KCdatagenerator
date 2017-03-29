var CONST = require('./../conf.js');
module.exports = {
  get : function(req, java, javaBridge){
    var query = req.query;

    var count = query.count;
    var recipients = query.recip;

    if(count == undefined || count.length == 0) return "Email count is not filled";
    if(recipients == undefined || recipients.length == 0) return "You need to fill at least one recipient";
    var recipArr = query.recip.split(";");
    var recipList = java.newInstanceSync("java.util.ArrayList");

    for(i=0; i<recipArr.length; i++){
      if(recipArr[i].length > 0){
        var tmpRecip = recipArr[i].trim();
        if(tmpRecip.length > 0 && validateEmail(tmpRecip)){
          recipList.addSync(java.newInstanceSync("engine.SimpleAccount", tmpRecip));
        }
      }
    }
    if(recipList.sizeSync() == 0)  return "You need to fill at least one recipient";

    var sliderValue = java.newInstanceSync("java.util.HashMap");
    var checkboxes = java.newInstanceSync("java.util.HashMap");
    checkboxes.putSync(CONST.READCONFIRM, query.rconf == "on");
    checkboxes.putSync(CONST.DELIVERY,  query.dconf == "on");
    checkboxes.putSync(CONST.SUBJECT,   query.sub == "on");
    checkboxes.putSync(CONST.EMAIL_CC,  query.cc == "on");
    checkboxes.putSync(CONST.HTMLCODE,  query.htmlc == "on");
    checkboxes.putSync(CONST.LINKS,     query.links == "on");

    sliderValue.putSync(CONST.FLAG_PROB, parseInt(query.flag));
    sliderValue.putSync(CONST.HIGH_PRIORITY, parseInt(query.prior));
    sliderValue.putSync(CONST.ATTACHMENT_PROB, parseInt(query.attach));
    sliderValue.putSync(CONST.CONTENT_SIZE, parseInt(query.csize));
    sliderValue.putSync(CONST.INLINE_IMG_PROB, parseInt(query.inimg));

    console.log("ahiihihihih");
    javaBridge.genEmailsSync(parseInt(count), (query.czchar == "on"), checkboxes, sliderValue, recipList);
    req.params.success = "Emails were successfuly generated";
    return undefined;
  },
  post : function(){
    console.log('There is no POST handler');
  }
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}
