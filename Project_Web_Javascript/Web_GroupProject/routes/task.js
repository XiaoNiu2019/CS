var express = require('express');

var router = express.Router();

const project = require('../database/model/projectModel');
const task = require('../database/model/taskModel');
const journal = require('../database/model/journalModel');
const status = require('../database/model/statusModel');

//display task
router.get('/', async (req, res, next) => {

    //check if one task is selected
    if (req.session.task == 'unknown'){
        res.redirect('/calendme');
    }
    else{

        //find selected task
        let currentTask = await task.findById(req.session.task)
            .populate('assignee')
            .populate('status')
            .catch((mongoError) => res.render('error', {error: mongoError}));

        //find associated project
        let currentProject = await project.findById(currentTask.project)
            .catch((mongoError) => res.render('error', {error: mongoError}));

        //find associated journals
        let filter = {};
        filter.task = currentTask._id;
        let comments = await journal.find(filter)
            .populate('author')
            .catch((mongoError) => res.render('error', {error: mongoError}));


        res.render('task', {user: req.session.user, task: currentTask, comments: comments, project: currentProject});
    }
});

//process to edit task
router.post('/', (req, res) => {
    req.session.task=req.body.task;
    res.redirect('/calendme/task/edit');
});

//display task creation
router.get('/new', async(req, res) => {

    //check if a project is selected
    if (req.session.project == 'unknown'){
        res.redirect('/calendme');
    }
    else{

        //find selected project
        let currentProject = await project.findById(req.session.project)
            .populate('members')
            .catch((mongoError) => res.render('error', {error: mongoError}));

        //load status list
        let statusList = await status.find()
            .catch((mongoError) => res.render('error', {error: mongoError}));

        res.render('newTask', {user: req.session.user, project: currentProject, statusList: statusList});
    }
});

//process new task saving
router.post('/new', async(req, res) => {

    //create new task
    let newTask = task(req.body);

    //set project to display
    req.session.project = req.body.project;

    //save the new task
    await newTask.save()
        .catch((err) => {
            res.redirect('/calendme/task/new');
        });

    res.redirect('/calendme/project');
});

//Process to add a new comment
router.post('/newcomment', async(req,res) => {

    //Create a new entry in memory with current date
    let newComment = new journal({author:req.session.user.login});
    newComment.entry = req.body.newcomment;
    newComment.task = req.body.task;
    newComment.date = new Date();

    //Save to the database
    await newComment.save()
        .catch((mongoError) => res.render('error',{error:mongoError}));

    res.redirect('/calendme/task')
});


//display edit task page
router.get('/edit', async(req, res) => {
    //check if one task is selected
    if (req.session.task == 'unknown'){
        res.redirect('/calendme');
    }
    else {

        //find selected task
        let currentTask = await task.findById(req.session.task)
            .catch((mongoError) => res.render('error', {error: mongoError}));

        //load status list
        let statusList = await status.find()
            .catch((mongoError) => res.render('error', {error: mongoError}));

        // find associated project
        let currentProject = await project.findById(currentTask.project)
            .populate('members')
            .catch((mongoError) => res.render('error', {error: mongoError}));


        res.render('editTask', {user: req.session.user, project: currentProject, statusList: statusList, task: currentTask});
    }
});

//process edited task saving
router.post('/edit', async(req, res) => {

    //set task to display
    req.session.task = req.body._id;

    //update task
    await task.updateOne({_id: req.body._id}, req.body)
        .catch((err) => {
            res.redirect('/calendme/task/edit')});

    res.redirect('/calendme/task');
});

//Process to delete a journal, available only for admin or author of the task
router.post('/delete', async(req,res) => {
    await journal.deleteOne({_id: req.body.journalToDelete}, function (err) {
        if (err) return res.send(500, {error: err});
    });
    res.redirect('/calendme/task');
});

module.exports = router;