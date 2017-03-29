var CONST = require('./../conf.js');
module.exports = {
  get : function(req, java, javaBridge){
    var query = req.query;

    var count = query.count;
    if(count == undefined) return "Event count is not filled";

    var attendees = query.attendees;
    if(attendees == undefined || attendees.trim().length == 0){
      var attenArr = attendees.split(";");
      var attenList = java.newInstanceSync("java.util.ArrayList");

      for(i=0; i<attenArr.length; i++){
        if(attenArr[i].length > 0){
          var tmpAtten = attenArr[i].trim();
          if(tmpAtten.length > 0 && validateEmail(tmpAtten)){
            attenList.addSync(java.newInstanceSync("engine.SimpleAccount", tmpAtten));
          }
        }
      }
    }

    var sliderValue = java.newInstanceSync("java.util.HashMap");
    var checkboxes = java.newInstanceSync("java.util.HashMap");
    checkboxes.putSync(CONST.SUBJECT, query.sub == "on");
    checkboxes.putSync(CONST.LOCATION, query.loc == "on");
    checkboxes.putSync(CONST.LABEL, query.lab == "on");
    checkboxes.putSync(CONST.SHOWAS, query.show == "on");
    checkboxes.putSync(CONST.REMINDER, query.remind == "on");
    checkboxes.putSync(CONST.HTMLCODE, query.htmlc == "on");
    checkboxes.putSync(CONST.LINKS, query.links == "on");

    sliderValue.putSync(CONST.RESOURCE, parseInt(query.resource));
    sliderValue.putSync(CONST.PRIVATE, parseInt(query.private));
    sliderValue.putSync(CONST.ALL_DAY, parseInt(query.alld));
    sliderValue.putSync(CONST.PAST_EVENTS, parseInt(query.past));
    sliderValue.putSync(CONST.RECURRENCE, parseInt(query.recc));
    sliderValue.putSync(CONST.ATTENDEES, parseInt(query.attenprob));
    sliderValue.putSync(CONST.CONTENT_SIZE, parseInt(query.csize));

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

    var attenArr = query.attendees.split(";");
    var attendees = java.newInstanceSync("java.util.ArrayList");
    var i;
    for(i=0; i<attenArr.length; i++){
      var attendee = java.newInstanceSync("engine.SimpleAccount", attenArr[i].trim());
      attendees.addSync(attendee);
    }
    javaBridge.genEventsSync(parseInt(count), (query.czchar == "on"), checkboxes, sliderValue, dateRange, attendees);
    req.params.success = "Events were successfuly generated";
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
