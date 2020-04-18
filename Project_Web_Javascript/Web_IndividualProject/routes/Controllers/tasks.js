var Projects = require('../models/projectsModel');
var Tasks = require('../models/tasksModel');
var Status = require('../models/statusModel');
var Journals = require('../models/journalsModel');
var express = require('express');
var router = express.Router();

router.get('/tasks/:id', function(req, res) {
    //console.log(req.params.id )
    // request parameter stored in req.params.borough_id
   // Tasks.findOne({ _id: req.params.id },function(err, task_selected) {
        //console.log(project_selected.name)
        //console.log(req.params.id )

    Tasks.findOne({ _id: req.params.id }).populate({ path: 'assignee', select: 'firstname name' })
            .exec().then(function(task_selected){

        Status.findOne({_id:{$in: [task_selected.status]}}, function(err, status){
            // console.log(tasks.name)
            Journals.find({task:{$in: [req.params.id]}},).populate({ path: 'author', select: 'firstname name' })
                .exec().then(function(journals){
                return res.render('task', {
                    status:status,
                    journals:journals,
                    task:task_selected,
                    projectname: req.session.project.name,
                    username:  req.session.user.firstname + ' ' + req.session.user.name});
        });


        });



    });
});


/**/


module.exports = router;