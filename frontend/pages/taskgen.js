var CONST = require('./../conf.js');
module.exports = {
  get : function(req, java, javaBridge){
    var query = req.query;

    var count = query.count;
    if(count == undefined || count.trim().length == 0)  return "Task count is not filled";

    var sDate = query.sdate;
    var eDate = query.edate;
    if(sDate == undefined || sDate.trim().length == 0){
      if(eDate == undefined || eDate.trim().length == 0){
        sDate = getActualDate();
        eDate = getDateAfter(sDate, 5);
      }else{
        sDate = getDateAfter(eDate, -5);
      }
    }
    if(eDate == undefined || eDate.trim().length == 0){
      if(sDate == undefined || sDate.trim().length == 0){
        sDate = getActualDate();
      }
      eDate = getDateAfter(sDate, 5);
    }
    var startingDate = java.newInstanceSync("engine.TimeInput", "M/d/yyyy", sDate).getDateSync();
    var endingDate = java.newInstanceSync("engine.TimeInput", "M/d/yyyy", eDate).getDateSync();
    var dateRange = java.newArray("java.util.Date", [startingDate, endingDate]);

    var checkboxes = java.newInstanceSync("java.util.HashMap");
    var sliderValue = java.newInstanceSync("java.util.HashMap");

    checkboxes.putSync(CONST.SUBJECT, query.sub == "on");
    checkboxes.putSync(CONST.HTMLCODE, query.htmlc == "on");
    checkboxes.putSync(CONST.LINKS, query.links == "on");

    sliderValue.putSync(CONST.COMPLETED, parseInt(query.completeSl));
    sliderValue.putSync(CONST.REMINDER, parseInt(query.reminderSl));
    sliderValue.putSync(CONST.CONTENT_SIZE, parseInt(query.descSl));

    javaBridge.genTasksSync(parseInt(count), (query.czchar == "on"), checkboxes, sliderValue, dateRange);
    req.params.success = "Tasks were successfuly generated";
    return undefined;
  },
  post : function(){
    console.log('There is no POST handler');
  }
}


function getActualDate(){
  var today = new Date();
  var dd = today.getDate();
  var mm = today.getMonth()+1; //January is 0!
  var yyyy = today.getFullYear();

  if(dd<10) {
      dd='0'+dd
  }
  if(mm<10) {
      mm='0'+mm
  }
  today = mm+'/'+dd+'/'+yyyy;
  return today;
}

function getDateAfter(rootDate, days){
  var parts = rootDate.split('/');
  var date = new Date(parts[2], parts[0]-1, parts[1]);
  date.setDate(date.getDate() + days);
  var dd = date.getDate();
  var mm = date.getMonth()+1; //January is 0!
  var yyyy = date.getFullYear();
  if(dd<10) {
      dd='0'+dd
  }
  if(mm<10) {
      mm='0'+mm
  }
  date = mm+'/'+dd+'/'+yyyy;
  return date;
}

function validateDate(date) {
  var IsoDateRe = new RegExp("^([0-9]{2})/([0-9]{2})/([0-9]{4})$");
  var matches = IsoDateRe.exec(date);
  if (!matches) return false;
  var month = matches[2] - 1;
  var composedDate = new Date(matches[1], month, matches[3]);

  return ((composedDate.getMonth() == month) &&
          (composedDate.getDate() == matches[3]) &&
          (composedDate.getFullYear() == matches[1]));
}
