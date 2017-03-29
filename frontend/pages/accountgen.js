var CONST = require('./../conf.js');
module.exports = {
  get : function(req, java, javaBridge){
    var query = req.query;

    var count = query.count;
    if(count == undefined || count.trim().length == 0) return "Account count is not filled";

    var checkboxes = java.newInstanceSync("java.util.HashMap");
    checkboxes.putSync(CONST.PASSWORD, query.spass == "on");
    checkboxes.putSync(CONST.FULLNAME, query.fname == "on");
    checkboxes.putSync(CONST.DESCRIPTION, query.desc == "on");
    var domain = (query.domain) ? query.domain : null;
    javaBridge.genAccountsSync(parseInt(query.count), (query.czchar == "on"), checkboxes, domain);
    req.params.success = "Accounts were successfuly generated";
    return undefined;
  },
  post : function(){
    console.log('There is no POST handler');
  }
}
