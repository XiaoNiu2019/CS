var Projects = require('../models/projectsModel');
var Tasks = require('../models/tasksModel');
var Assignee = require('../models/userModel');
var express = require('express');
var router = express.Router();

router.get('/projects', function(req, res) {

   // console.log(Projects.find({members:{$in: [req.session.user._id]}}))
    Projects.find({members:{$in: [req.session.user._id]}}, function(err, projects)
    {
        return res.render('projects', { projects: projects, username:  req.session.user.firstname + ' ' + req.session.user.name});
    })

});

router.get('/projects/:id', function(req, res) {
    //console.log(req.params.id )
    // request parameter stored in req.params.borough_id
       Projects.findOne({ _id: req.params.id },function(err, project_selected) {
        //console.log(project_selected.name)
           req.session.project = project_selected;

           Tasks.find({project:{$in: [req.params.id]}}).populate({ path: 'assignee', select: 'firstname name' })
               .exec().then(function(tasks){
               return res.render('project', {project:project_selected, tasks:tasks, username:  req.session.user.firstname + ' ' + req.session.user.name});
           });

       });
});


router.get('/projects/:id/new', (req, res) => {
    res.render('new',{project_id:req.params.id});
});


/* POST newly added restaurant, called when the Save button is clicked */
router.post('/projects/:id/new.post', async(req, res) => {
    var newtask = new Tasks({
        name: req.body.name,
        status: req.body.status,
        start_date: req.body.start_date,
        due_date: req.body.due_date,
        assignee: req.body.assignee,
        project:req.params.id

    });
    console.log(newtask.name);
    // save model to database
    newtask.save(function (err, task) {
        if (err) return console.error(err);
        console.log(task.name + " saved to Tasks collection.");
        res.redirect('/projects/'+req.params.id);
    });

});

/* GET edit restaurant form page (hyperlink from table in home page) */
router.get('/projects/:id/edit/:t_id', function(req, res) {
    var filter = { _id: req.params.t_id };
    Tasks.find(filter).limit(1).exec(function(err, task) {
        if(err) {console.error('Not found', filter); }
        res.render('edit', { task: task[0],project_id:req.params.id});
    });
});


/* POST edited restaurant, called when the Save button is clicked */
router.post('/projects/:id/edit.post/:t_id', function(req, res) {
    var filter = { _id: req.params.t_id };
    var updates = req.body;
    console.log("yes1")
    Tasks.update(filter, updates, function(err) {
        if(err) { console.error('Not found', filter); res.redirect('/'); }
        console.log("yes2")
        res.redirect('/projects/'+req.params.id);
    });

});







/*
return res.render('project', {project:project_selected, tasks:tasks, username:  req.session.user.firstname + ' ' + req.session.user.name});
router.get('/projects/:id', async(req, res) => {


    let project_selected = await Projects.find({_id: req.params.id })
        .catch((mongoError) => res.render('error', {error: mongoError}));
    req.session.project = project_selected;
    //var project_selected_obj = project_selected.transformNodeToObject();
    //console.log(project_selected_obj.name);

    //let project_obj = await project_selected.find().lean()
    //    .catch((mongoError) => res.render('error', {error: mongoError}));
    //project_obj.addedProperty = 'foobar';
    //project_selected.{ getters: true }

    let tasks = await Tasks.find({project:{$in: [req.params.id]}})
        .catch((mongoError) => res.render('error', {error: mongoError}));
    console.log(project_selected.name);
    res.render('project', {project:project_selected, tasks:tasks, username:  req.session.user.firstname + ' ' + req.session.user.name});

});

*/
/*
    // create a new instance in memory from the message body
    let task = Tasks(req.body);

    // save a document to the database based on the instance content
    await Tasks.save()
        .catch((err) => {
            // let the user retry in case of error: '/new' page again
            res.render('new')
        });
    // saved ok: go back to home page
    res.redirect('/projects/:id');*/


module.exports = router;