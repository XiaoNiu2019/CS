var express = require('express');

//require needed to cast to ObjectId
const mongoose = require('mongoose');

var router = express.Router();

//Initialization of the models
const project = require('../database/model/projectModel');
const task = require('../database/model/taskModel');
const status = require('../database/model/statusModel');
const user = require('../database/model/userModel');


//session authentication checker
router.all('/*', (req, res, next) => {

    //unknown session
    if(!req.session.user){
        req.session.user = {};
        req.session.user.login = 'unconnect'
    }
    //unconnected session
    if(req.session.user.login == 'unconnect' || req.session.user.login == 'fail'){
        res.redirect('/calendme/auth')
    }
    //connected session
    else{
        next();
    }
});


//Homepage display
router.get('/', async (req, res, next) => {
    //reset project to display
    req.session.project = 'unknown';

    //find the user's projects
    let filter = {};
    filter.members = mongoose.mongo.ObjectId(req.session.user.login);
    let projects = await project.find(filter)
        .populate('members')
        .catch((mongoError) => res.render('error', {error: mongoError}));

    //find the user's tasks
    let tasks = await task.find({assignee:filter.members})
        .populate('project')
        .populate('status')
        .catch((mongoError) => res.render('error', {error: mongoError}));

    //find the finished tasks
    let statuttermine = await status.findOne({name:"Terminé"})
        .catch((mongoError) => res.render('error', {error: mongoError}));
    let finished = await task.find({assignee:filter.members, status:statuttermine})
        .populate('project')
        .populate('status')
        .catch((mongoError) => res.render('error', {error: mongoError}));

    //find the users (rendered only by admins)
    let allUsers = await user.find();

    //Load status list
    let statusList = await status.find();

    res.render('home', {user: req.session.user, projects: projects, assignedTasks: tasks, finished:finished, user_admin : allUsers, statusList : statusList})
});


//Process to single project display
router.post('/', (req, res) => {

    //set id of project to display
    req.session.project = req.body.project;

    res.redirect('/calendme/project');
});


//Process to single task display
router.post('/get-task', (req, res) => {

    //set id of task to display
    req.session.project = req.body.project;
    req.session.task = req.body.task;

    res.redirect('/calendme/task');
});


//Process to delete a member (available for admins only)
router.post('/deletemember', async(req,res) => {
    await user.deleteOne({_id:req.body.member}, function(err) {
        if (err) return res.send(500, { error: err });
        res.redirect('/calendme');
    });
});


//Process to make an user admin (available for admins only)
router.post('/upgrademember', async(req,res) => {
    await user.update({_id:req.body.member},{roles:['admin','member']}, function (err) {
        if (err) return res.send(500, { error: err });
        res.redirect('/calendme');
    });
});


//Display the project creation page
router.get('/newProject' , async (req, res) => {

    //Offer the choice to include everybody to the project
    let userList = await user.find();
    res.render('newProject', {user : req.session.user, listUsers : userList});
});


//Process to create a new project
router.post('/newProject', async(req, res) => {
    //User filled the fields correctly
    if(req.body.members){
        let newProject = new project(req.body);
        await newProject.save()
            .catch((err) => {
                res.redirect('newProject');
            });
        res.redirect('/calendme');
    }

    //User forgot the members of the project
    else{
        let userList = await user.find();
        res.render('newProject', {user : req.session.user, listUsers : userList, result : "Select at least one member"});
    }
});

//Display the creation page of  a new status
router.get('/newStatus' , async (req, res) => {
    res.render('newStatus', {user : req.session.user});
});

//Process to create a new status
router.post('/newStatus' , async (req, res) => {
    let newStatus = new status(req.body);
    await newStatus.save()
        .catch((err) => {
            res.redirect('newStatus');
        });
    res.redirect('/calendme')
});

//Display status edition page
router.get('/editStatus' , async (req, res) => {
    //check if a status is selected
    if (req.session.status == 'unknown'){
        res.redirect('/calendme');
    }

    //find the status to edit
    let editStatus = await status.findById(req.session.status);

    res.render('editStatus', {user : req.session.user, status: editStatus});
});

//Process status update or edit query
router.post('/editStatus' , async (req, res) => {
    //select status and redirect to edition page
    if(req.body.status){
        req.session.status = req.body.status;
        res.redirect('/calendme/editStatus')
    }
    //update status in the database
    else{
        await status.updateOne({_id: req.body._id}, req.body)
            .catch((err) => {
                res.redirect('/calendme/editStatus')});

        res.redirect('/calendme');

    }
});

//Process deletion of a status
router.post('/deleteStatus' , async (req, res) => {
    await status.deleteOne({_id:req.body._id});
    res.redirect('/calendme')
});

module.exports = router;