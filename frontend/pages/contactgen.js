var CONST = require('./../conf.js');
module.exports = {
  get : function(req, java, javaBridge){
    var query = req.query;

    var count = query.count;
    if(count == undefined) return "Event count is not filled";

    var sliderValue = java.newInstanceSync("java.util.HashMap");
    var checkboxes = java.newInstanceSync("java.util.HashMap");
    checkboxes.putSync(CONST.FIRSTNAME, query.fname == "on");
    checkboxes.putSync(CONST.LASTNAME,  query.lname == "on");
    checkboxes.putSync(CONST.MIDDLENAME, query.mname == "on");
    checkboxes.putSync(CONST.NICKNAME,  query.nname == "on");
    checkboxes.putSync(CONST.WEBSITE,   query.web == "on");
    checkboxes.putSync(CONST.COMPANY,   query.com == "on");
    checkboxes.putSync(CONST.JOBTITLE,  query.job == "on");
    checkboxes.putSync(CONST.HTMLCODE,  query.htmlc == "on");
    checkboxes.putSync(CONST.LINKS,     query.links == "on");
    checkboxes.putSync(CONST.IMADDRESS, query.imadd == "on");
    checkboxes.putSync(CONST.BIRTHDAY,  query.birth == "on");
    checkboxes.putSync(CONST.DEPARTMENT, query.office == "on");
    checkboxes.putSync(CONST.MANAGER,   query.manag == "on");

    sliderValue.putSync(CONST.EMAIL_COUNT, parseInt(query.emailc));
    sliderValue.putSync(CONST.PHONE_COUNT, parseInt(query.phonec));
    sliderValue.putSync(CONST.AVATAR, parseInt(query.avaprob));
    sliderValue.putSync(CONST.ADDRESS_COUNT, parseInt(query.add));
    sliderValue.putSync(CONST.DESCRIPTION, parseInt(query.notes));

    javaBridge.genContactsSync(parseInt(count), (query.czchar == "on"), checkboxes, sliderValue);
    req.params.success = "Contacts were successfuly generated";
    return undefined;
  },
  post : function(){
    console.log('There is no POST handler');
  }
}
