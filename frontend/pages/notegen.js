var CONST = require('./../conf.js');
module.exports = {
  get : function(req, java, javaBridge){
    var query = req.query;
    var count = query.count;
    if(count == undefined || count.trim().length == 0)  return "Note count is not filled";
    var checkboxes = java.newInstanceSync("java.util.HashMap");
    var sliderValue = java.newInstanceSync("java.util.HashMap");
    checkboxes.putSync(CONST.SUBJECT, query.sub == "on");
    checkboxes.putSync(CONST.HTMLCODE, query.htmlc == "on");
    checkboxes.putSync(CONST.LINKS, query.links == "on");
    checkboxes.putSync(CONST.COLOR, query.color == "on");
    sliderValue.putSync(CONST.CONTENT_SIZE, parseInt(query.csize));
    javaBridge.genNotesSync(parseInt(query.count), (query.czchar == "on"), checkboxes, sliderValue);
    req.params.success = "Notes were successfuly generated";
    return undefined;
  },
  post : function(){
    console.log('There is no POST handler');
  }
}
