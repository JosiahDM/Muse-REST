// Some times in millis
var DAY = 86400000; // 24 hours
var WEEK = 6.048e+8;
var MONTH = 2.628e+9;
var THREEMONTH = 7.884e+9;
var SIXMONTH = 1.577e+10;
var YEAR = 3.154e+10;

// Function to keep the TinyMCE form at 100% height
function resize() {
    if ($('#preForm').length || $('#right').length) {
        setTimeout(function () {
            // Main container
            var max = $('.mce-tinymce')
                  .css('border', 'none')
                  .parent().parent().outerHeight();
            // toolbar
            max -= $('.mce-toolbar-grp').outerHeight();

            // Random fix lawl - why 1px? no one knows
            max -= 1;

            // Set the new height
            $('.mce-edit-area').height(max);
            $('#formText_ifr').height(max);
        }, 200);
    }
}

var startDisplay = function() {
    $('#container').load('startDisplay.html #buttonList', function(e){
        $('#startButton').click(beginButtonClicked);
        var user = true; // HARD CODED USER, FIX LATER
        if (user) {
            var stats = $('<li><button id="statsBtn">Stats</button></li>');
            $('#buttonList').append(stats);
            $(stats).click(statsButtonClicked);
        } else {
            var login = $('<li><button id="splashLogin">Login</button></li>');
            $('#buttonList').append(login)
            $(login).click(splashLoginClicked);
        }
    });
};

var statsButtonClicked = function(e) {
    $.ajax({
        type: "GET",
        url: "api/block/user/1",  // HARD CODED USER ID MUST CHANGE LATER
        dataType: "json",
        success: displayStats
    });
};

var displayStats = function(xhr) {
    var map = [];
    $.each(xhr, function(index, val) {
        var name = val.subject.name;
        map[name] = { 'latest': latestDate(val.end, name, map),
                      'oneMo' : sumTime(val.start, val.end, MONTH, name, map, 'oneMo'),
                      'threeMo' : sumTime(val.start, val.end, THREEMONTH, name, map, 'threeMo'),
                      'sixMo' : sumTime(val.start, val.end, SIXMONTH, name, map, 'sixMo'),
                      'year' : sumTime(val.start, val.end, YEAR, name, map, 'year')
                        };
    });
    $('#container').load('fullStats.html', function(e){
        for (var p in map) {
            var row = map[p];
            var tr = $('<tr>');
            var subject = $('<td>').text(p);
            tr.append(subject);
            for (var val in row) {
                // Convert the milliseconds values to correct date or hour values.
                var converted = (val === 'latest') ?
                        dateMaker(row[val]) : (row[val]/1000/60/60).toFixed(2);
                var td = $('<td>'+converted+'</td>')
                tr.append(td);
            }
            $('#statsBody').append(tr);
        }
    });

};

// Get the hour:minutes difference of date2-date1
var timeDifference = function(epoch1, epoch2, hoursOnly) {
    if (hoursOnly) {
        return (epoch2-epoch1)/1000/60/60;
    }
    var date1 = new Date(epoch1);
    var date2 = new Date(epoch2);
    var hours = date2.getHours() - date1.getHours();
    var minutes = date2.getMinutes() - date1.getMinutes();
    minutes = (minutes < 10 ) ? '0'+minutes : minutes;
    return hours + ':' + minutes
};

var sumTime = function(start, end, endRange, key, map, where) {
    var currentTime = (new Date).getTime();
    var cutoffTime = currentTime - endRange;
    var sum = (map[key]) ? map[key][where] : 0;
    if (start > cutoffTime) {
        sum += end - start;
    } else if (start > cutoffTime && start <= cutoffTime) {
        sum += end - cutoffTime;
    }
    return sum;
};

// Looks through the block array to endRange time (milliseconds)
// and totals the hours and minutes for each block in range.
// Range goes from the time its called, to the endRange value
var getTotalTime = function(blockArr, endRange, hoursOnly){
    var currentTime = (new Date).getTime();
    var cutoffTime = currentTime - endRange;
    var sum = 0; // milliseconds
    $.each(blockArr, function(index, val) {
        if (val.start > cutoffTime) {
            sum += val.end - val.start;
        } else if (val.start > cutoffTime && val.start <= cutoffTime) {
            sum += val.end - cutoffTime;
        }
    });
    var hours = parseInt(sum/1000/60/60);
    if (hours < 1) { hours = ''; }
    var minutes = Math.round(sum/1000/60%60);
    if (minutes < 10) { minutes = '0'+minutes;}
    return hours + ':' + minutes;
};

// return whichever date is greater
var latestDate = function(date, key, map) {
    if (!map[key]) {
        return date;
    } else {
        return (map[key].latest < date) ? date : map[key].latest;
    }
};

var beginButtonClicked = function(e) {
    $('#container').load('preForm.html #preForm', function(e){
        tinymce.init({
          selector: '#formText',
          toolbar: 'bold underline italic fontsizeselect formatselect',
          menubar: false,
          resize: false,
          statusbar: false,
          height: 400
        });
        resize();
        addStartListener();
    });
};

var addStartListener = function() {
    $('#start-btn').click(function(e) {
        var userContent = $('#formText_ifr').contents().find('#tinymce').html();
        var milliseconds = (new Date).getTime();
        var timeGoal = timeConvert($('#timeGoal').val(),
                                    milliseconds,
                                    $('#timeUnit').val());
        var blockData = {'preNotes':userContent,
                         'postNotes':'',
                         'start':milliseconds,
                         'end':0,
                         'timeGoal':timeGoal};
        if (!timeGoal) {
            insertError('#timeUnit','Please enter a valid time goal');
        } else {
            if ($('#error').length > 0) {
                $('#error').remove();
            }
            preBlockSubmit(JSON.stringify(blockData));
        }
    });
};

// Insert error message after given location
var insertError = function(location, text) {
    var ptag = $('<p>')
    ptag.attr('id', 'error');
    if ($('#error').length < 1) {
        $(location).after(ptag.text(text));
    }
};

// Convert user time goal input from minutes or hours into milliseconds
// return null if not a number
var timeConvert = function(timeGoal, current, unit) {
    timeGoal = parseFloat(timeGoal);
    var millis;
    if (isNaN(timeGoal)) {
        return null;
    }
    switch (unit) {
        case 'minutes': millis = timeGoal * 60000;
            break;
        case 'hours': millis = timeGoal * 360000;
            break;
        default: break;
    }
    return millis+current;
};

var preBlockSubmit = function(blockData) {
    $.ajax({
        type: "POST",
        url: "api/user/1/block/",        // USER ID HARD CODED NEEDS FIX LATER
        contentType: "application/json",
        dataType: "json",
        data: blockData,
        success: displayDuring
    });
};

var blockView = function() {
    tinymce.EditorManager.execCommand('mceRemoveEditor',true, 'formText');
    $('#container').css('width', '95%');
    $('#container').css('background-color', 'inherit');
    $('#container').empty();
};

// Setup display during focus block.
var displayDuring = function(xhr) {
    blockView();
    $('#container').load('displayDuring.html', function(e){
        loadLeftScreen(xhr);
        loadRightScreen(xhr, true);
    });
};

var loadLeftScreen = function(xhr) {
    $('#left').append(xhr.preNotes);
};

var loadRightScreen = function(xhr, edit) {
    if (edit) {
        $('#postForm').append($('<textarea id="formText"></textarea>'));
        tinymce.EditorManager.execCommand('mceAddEditor',true, 'formText');
        $('#postForm').append($('<input id="end-btn" type="button" value="Finish"></input>'));
        $('#end-btn').click(function(e) {
            if (confirm("Are you sure you want to end this block?") ) {
                updateBlockAfterFinish(xhr);
            }
        });
    } else {
        $('#right').append(xhr.postNotes);
        $('#right').css('text-align', 'left');
    }
};

var updateBlockAfterFinish = function(xhr) {
    var notes = $('#formText_ifr').contents().find('#tinymce').html();
    var end = (new Date).getTime();
    xhr.postNotes = notes;
    xhr.end = end;
    sendBlockUpdate(JSON.stringify(xhr), xhr.id);
    loadSubjects(xhr);
};
// Update a block by id. data is JSON string.
var sendBlockUpdate = function(data, id, callback) {
    $.ajax({
        type: "PUT",
        url: "api/block/"+id,
        contentType: "application/json",
        data: data,
        success: function(xhr) {
            if (callback) {
                callback(data);
            }
        }
    }).fail(function(a, b, c){
        console.log(a);
        console.log(b);
        console.log(c);
    });
};

// load the user's subjects to populate the select options in addSubjectDisplay
var loadSubjects = function(blockData) {
    $.ajax({
        type: "GET",
        url: "api/user/1/subjects",  // HARD CODED USER ID MUST CHANGE LATER
        dataType: "json",
        success: function(xhr) {
            addSubjectDisplay(xhr, blockData);
        }
    });
};

// After pulling the subjects for the current user, display the subject form
var addSubjectDisplay = function(xhr, blockData) {
    tinymce.EditorManager.execCommand('mceRemoveEditor',true, 'formText');
    $('#container').empty();
    $('#container').css('width', '400px');
    $('#container').css('background-color', 'rgba(255, 255, 255, 0.55)');

    $('#container').load('addSubjectDisplay.html', function(e){
        appendSubjects(xhr, '#subjectSelect');
        previousSubjectClick(e);
        newSubjectClick(e);
        $('#submitSubject').click(function(e){
            e.preventDefault();
            var checked = $('input[name="choice"]:checked').val();
            updateSubjectSubmit(checked, blockData, loadStats);
        });
    });
};

// For the subject of the currently passed block (xhr), load all the blocks
// of that subject, for current user. This will be used to generate aggregated
// time stats for an invidual subject
var loadStats = function(xhr) {
    console.log("LOAD STATS NAO!");
    console.log(JSON.parse(xhr));
    var block = JSON.parse(xhr);
    $.ajax({
        type: "GET",
        url: "api/user/1/blocks/"+block.subject.name,  // HARD CODED USER ID MUST CHANGE LATER
        dataType: "json",
        success: subjectStatDisplay
    });
};

// return date string as MM/DD/YYYY HH:MM
var dateMaker = function(epochVal) {
    var date = new Date(epochVal);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    minutes = (minutes < 10 ) ? '0'+minutes : minutes;
    return month + '/' + day + '/' + year + ' ' + hours + ':' + minutes
};



// setup the view for all blocks by subject
var subjectStatDisplay = function(xhr) {
    $('#container').empty();
    $('#container').css('width', '70vw');
    $('#container').css('background-color', 'rgba(255, 255, 255, 0.55)');
    $('#container').load('statsSubject.html', function(e){
        console.log("Loaded stats display");
        console.log(xhr);
        listBlocks(xhr, '#blocks');
        $('#dayTime').text(getTotalTime(xhr, DAY));
        $('#weekTime').text(getTotalTime(xhr, WEEK));
        $('#monthTime').text(getTotalTime(xhr, MONTH));
        $('#subjectSpan').text(xhr[0].subject.name);
    });
};


// build the list of blocks with buttons
var listBlocks = function(blockArr, ulId) {
    $.each(blockArr, function(index, val) {
        var li = $('<li>'+dateMaker(val.end)+' </li>');
        var timeSpan = $('<span>'+timeDifference(val.start, val.end)+'</span>')
        var viewBtn = $('<button id="'+val.id+'">View</button>');
        viewBtn.click(function(e) {
            console.log("CLICKED VIEW BTN");
            console.log(e.target.id);
            showBlock(e.target.id);
        });
        li.append(timeSpan);
        li.append(viewBtn);
        $(ulId).append(li);
    });
};

var showBlock = function(id) {
    $.ajax({
        type: "GET",
        url: "api/block/"+id,
        dataType: "json",
        success: function(xhr) {
            blockView();
            $('#container').load('displayDuring.html', function(e){
                loadLeftScreen(xhr);
                loadRightScreen(xhr, false);
            });
        }
    });
};

// On click of Submit button in subject display.
var updateSubjectSubmit = function(checked, blockData, callback) {
    if (checked === 'new') {
        if (!$('#newSubject').val().trim().length) {
            insertError('#newRadio','Please enter a name for the subject or '+
                                                    'select one from the list');
            return;
        }
        blockData.subject = {name:$('#newSubject').val()};
        sendBlockUpdate(JSON.stringify(blockData), blockData.id, callback);
    } else {
        blockData.subject = {name:$('#subjectSelect option:selected').text()};
        sendBlockUpdate(JSON.stringify(blockData), blockData.id, callback);
    }
};

// On click of subject list, auto-select its radio button.
var previousSubjectClick = function(e) {
    $('#subjectSelect').click(function(e) {
        $('#newRadio').removeAttr('checked');
        $('#prevRadio').attr('checked', 'checked');
    });
};

// on click of new subject text input, auto-select its radio button
var newSubjectClick = function(e) {
    $('#newSubject').click(function(e) {
        $('#prevRadio').removeAttr('checked');
        $('#newRadio').attr('checked', 'checked');
    });
};

// append full list of user's subjects to given location
var appendSubjects = function(xhr, location) {
    $.each(xhr, function(index, value) {
        var opt = $('<option value="'+value.id+'">'+value.name+'</option>')
        $(location).append(opt);
    });
};

var splashLoginClicked = function(e) {
    $('#buttonList').remove();
    $('#container').load('loginDisplay.html #loginForm', function(e){
        $('#login-btn').click(function(e) {
            e.preventDefault();
            console.log("LOGIN BtN");
        });
    });
}





$(document).ready(function() {
    $(window).on('resize', function () { resize(); });
    startDisplay();
});
