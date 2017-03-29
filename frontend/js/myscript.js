$('#contentSlider').slider({
	formatter: function(value) {
		$('#contentSliderValue').text(value);
		$('#contentSliderValue').val(value);
		return 'Current value: ' + value;
	}
});

$('#flagSlider').slider({
	formatter: function(value) {
		$('#flagSliderValue').text(value);
		$('#flagSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#highPrioritySlider').slider({
	formatter: function(value) {
		$('#highPrioritySliderValue').text(value);
		$('#highPrioritySlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#inlineImgSlider').slider({
	formatter: function(value) {
		$('#inlineImgSliderValue').text(value);
		$('#inlineImgSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#attachmentSlider').slider({
	formatter: function(value) {
		$('#attachmentSliderValue').text(value);
		$('#attachmentSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#alldaySlider').slider({
	formatter: function(value) {
		$('#alldaySliderValue').text(value);
		$('#alldaySlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#pastEventSlider').slider({
	formatter: function(value) {
		$('#pastEventSliderValue').text(value);
		$('#pastEventSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#resourceSlider').slider({
	formatter: function(value) {
		$('#resourceSliderValue').text(value);
		$('#resourceSlider').val(value);
		return 'Current value: ' + value;
	}
});


$('#recurrenceSlider').slider({
	formatter: function(value) {
		$('#recurrenceSliderValue').text(value);
		$('#recurrenceSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#attendeeSlider').slider({
	formatter: function(value) {
		$('#attendeeSliderValue').text(value);
		$('#attendeeSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#emailSlider').slider({
	formatter: function(value) {
		$('#emailSliderValue').text(value);
		$('#emailSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#phoneSlider').slider({
	formatter: function(value) {
		$('#phoneSliderValue').text(value);
		$('#phoneSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#avatarSlider').slider({
	formatter: function(value) {
		$('#avatarSliderValue').text(value);
		var count = $('#contactCount').val();

		$('#avatarSliderSum').text(count * 20 * value /100);
		$('#avatarSlider').val(value);
		return 'Current value: ' + value;
	}
});

$( "#contactCount" ).focusout(function() {
	var count = $('#contactCount').val();
	var value = $('#avatarSlider').val();
	$('#avatarSliderSum').text(count * 20 * value /100);
});

$('#addressSlider').slider({
	formatter: function(value) {
		$('#addressSliderValue').text(value);
		$('#addressSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#taskcompletedSlider').slider({
	formatter: function(value) {
		$('#taskcompletedSliderValue').text(value);
		$('#taskcompletedSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#reminderSlider').slider({
	formatter: function(value) {
		$('#reminderSliderValue').text(value);
		$('#reminderSlider').val(value);
		return 'Current value: ' + value;
	}
});


$('#noteContentSlider').slider({
	formatter: function(value) {
		$('#noteContentSliderValue').text(value);
		$('#noteContentSlider').val(value);
		return 'Current value: ' + value;
	}
});


$('#noteDescSlider').slider({
	formatter: function(value) {
		$('#noteDescSliderValue').text(value);
		$('#noteDescSlider').val(value);
		return 'Current value: ' + value;
	}
});

$('#accountDomainCheckbox').change(function(){
	if(this.checked){
		$('#domainNameText').removeAttr("disabled");
	}else{
		$('#domainNameText').attr("disabled", "disabled");
	}
});

$(function () {
    $('#datepickerStart').datepicker({
			onSelect: function (dateText, inst){
				$('#datepickerEnd').datepicker("option", "minDate", dateText);
			}
		});
		$('#datepickerEnd').datepicker({
			onSelect: function (dateText, inst){
				$('#datepickerStart').datepicker("option", "maxDate", dateText);
			}
		});
});

$(function () {
    $('#startTaskDate').datepicker({
			onSelect: function (dateText, inst){
				$('#endTaskDate').datepicker("option", "minDate", dateText);
			}
		});
		$('#endTaskDate').datepicker({
			onSelect: function (dateText, inst){
				$('#startTaskDate').datepicker("option", "maxDate", dateText);
			}
		});
});
