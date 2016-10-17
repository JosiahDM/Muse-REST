# Muse-REST

## Description
A productivity tracker app with the purpose of helping users get into the "flow" zone of work. 
Based off of some ideas from Cal Newport (http://calnewport.com/blog/2014/09/13/deep-habits-jumpstart-your-concentration-with-a-depth-ritual/).


The app starts with a "Depth Ritual" editor for the user to enter any notes, reminders, or ideas
before starting work to help get the mind focused on the task.

Once the user starts, the time they started is tracked. Once they finish, the end time is tracked and 
stored for that time block. While in the focus block, the user can add notes for reminders, next steps, etc.

## Goal

The primary goal was to practice with JavaScript, AJAX calls, and REST API building/consumption. 

## Fun things:
- I made a fun little map to generate the stats for a given user's time blocks. This puts all time blocks 
into a map using the subject as a key, then determines the last date they did something with that subject, 
the total hours spent in that subject for the past month, 3 months, 6 months, and year. This map is then looped 
over to generate the html table.
```var map = [];
    $.each(xhr, function(index, val) {
        var name = val.subject.name;
        map[name] = { 'latest': latestDate(val.end, name, map),
                      'oneMo' : sumTime(val.start, val.end, MONTH, name, map, 'oneMo'),
                      'threeMo' : sumTime(val.start, val.end, THREEMONTH, name, map, 'threeMo'),
                      'sixMo' : sumTime(val.start, val.end, SIXMONTH, name, map, 'sixMo'),
                      'year' : sumTime(val.start, val.end, YEAR, name, map, 'year')
                        };
    });```
    
- HTML Snippets. Pretty useful. Much better than writing a bunch of js generated html by hand. 

- TinyMCE. Super cool library for a WYSIWYG editor within the html. I wanted to be able to have formatting 
in the notes areas, to help organize, and TinyMCE was pretty easy to get working.

## Challenges:
- Time. There's a lot more functionality I want to add. Just not enough time to do it all.
- Keeping JS code nice and clean. Just need more experience to build more of an intuition about how to structure
methods, AJAX calls, etc.
- Date/time tracking.
