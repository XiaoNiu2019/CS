var express = require('express');

var router = express.Router();

//Initialization of the models
const project = require('../database/model/projectModel');
const task = require('../database/model/taskModel');
const status = require('../database/model/statusModel');
const user = require('../database/model/userModel');
const journal = require('../database/model/journalModel');


//display selected project
router.get('/', async (req, res, next) => {
    //check if a project is selected
    if (req.session.project == 'unknown'){
        res.redirect('/calendme');
    }
    else{

        let statuslist = await status.find()
            .catch((mongoError) => res.render('error', {error: mongoError}));

        //find the project to display
        let currentProject = await project.findById(req.session.project)
            .populate('members')
            .catch((mongoError) => res.render('error', {error: mongoError}));

        //find associated tasks
        let filter = {};
        filter.project = currentProject._id;
        let tasks = await task.find(filter)
            .populate('assignee')
            .catch((mongoError) => res.render('error', {error: mongoError}));

        //Compute the progress of the project
        let projectprogress=0;
        var n=0;
        for (var i=0;i<tasks.length;i++) {
            if(typeof tasks[i].progress !== 'undefined') {
                projectprogress += tasks[i].progress;
            }
        }
        projectprogress=projectprogress/i;

        res.render('project', {user: req.session.user, project: currentProject, tasks: tasks, statuslist: statuslist, projectprogress:projectprogress});
    }
});

//Process to display a task or edit a project, depending on what attribute of session is filled
router.post('/', (req, res) => {
    if(req.body.task) {
        req.session.task = req.body.task;
        res.redirect('/calendme/task')
    }
    else if(req.body.project){
        req.session.project = req.body.project;
        res.redirect('/calendme/project/edit')
    }
});


//Process to create a task
router.post('/newtask', (req, res) => {
    req.session.project = req.body.project;
    res.redirect('/calendme/task/new');
});


//Process to filter or to sort
router.post('/filtered-sorted', async(req, res) => {
    //process to sort
    var sorter = req.body.sorter;
    if (sorter == "default") {
        sorter = "name"
    }

    let statuslist = await status.find()
        .catch((mongoError) => res.render('error', {error: mongoError}));
    let currentProject = await project.findById(req.session.project)
        .populate('members')
        .catch((mongoError) => res.render('error', {error: mongoError}));
    let filter = {};
    filter.project = currentProject._id;


    let tasks = await task.find(filter)
        .populate({path:'assignee', options: { sort: { 'name': -1 } }})
        .sort(sorter)
        .catch((mongoError) => res.render('error', {error: mongoError}));
    var tasks_filtered_final =[];

    //process to filter assignee
    var tasks_filtered_assignee = [];
    if(req.body.assignee_selected !== "All"){
        var items = tasks.map((item) => {
            if (item.assignee._id == req.body.assignee_selected) {
                tasks_filtered_assignee.push (item)
            }
            return item;
        })
    }
    else{
        tasks_filtered_assignee = tasks;
    }

    //process to filter status
    var tasks_filtered_status = [];
    if(req.body.status_selected !== "All"){
        var items = tasks.map((item) => {
            if (item.status == req.body.status_selected) {
                tasks_filtered_status.push (item)
            }
            return item;
        })
    }
    else{
        tasks_filtered_status = tasks;
    }

    //process to filter start date
    var tasks_filtered_start = [];
    if(req.body.start_before_after == "Before" && req.body.start_date_selected !== "" ){
        var items = tasks.map((item) => {
            item_date = item.start_date.getFullYear().toString()+'-'+("0"+(item.start_date.getMonth()+1).toString()).slice(-2)+'-'+("0"+item.start_date.getDate().toString()).slice(-2);
            if ( item_date<= req.body.start_date_selected) {
                tasks_filtered_start.push (item)
            }
            return item;
        })
    }
    else if(req.body.start_before_after == "After" && req.body.start_date_selected !== "" ){
        var items = tasks.map((item) => {
            item_date = item.start_date.getFullYear().toString()+'-'+("0"+(item.start_date.getMonth()+1).toString()).slice(-2)+'-'+("0"+item.start_date.getDate().toString()).slice(-2);
            if (item_date >= req.body.start_date_selected ) {
                tasks_filtered_start.push (item)
            }
            return item;
        })
    }
    else {
        tasks_filtered_start = tasks;
    }

    //process to filter due date
    var tasks_filtered_due = [];
    if(req.body.due_before_after == "Before" && req.body.due_date_selected !== "" ){
        var items = tasks.map((item) => {
            item_date = item.due_date.getFullYear().toString()+'-'+("0"+(item.due_date.getMonth()+1).toString()).slice(-2)+'-'+("0"+item.due_date.getDate().toString()).slice(-2);
            if ( item_date<= req.body.due_date_selected) {
                tasks_filtered_due.push (item)
            }
            return item;
        })
    }
    else if(req.body.due_before_after == "After" && req.body.due_date_selected !== "" ){
        var items = tasks.map((item) => {
            item_date = item.due_date.getFullYear().toString()+'-'+("0"+(item.due_date.getMonth()+1).toString()).slice(-2)+'-'+("0"+item.due_date.getDate().toString()).slice(-2);
            if (item_date >= req.body.due_date_selected ) {
                tasks_filtered_due.push (item)
            }
            return item;
        })
    }
    else {
        tasks_filtered_due = tasks;
    }

    //process to compare all filters
    var items = tasks.map((item) => {
        if (tasks_filtered_start.includes(item) &&
            tasks_filtered_due.includes(item)&&
            tasks_filtered_assignee.includes(item)&&
            tasks_filtered_status.includes(item)) {
            tasks_filtered_final.push(item)
        }
        return item;
    });

    //sort assignee by name
    if(sorter == "assignee" && tasks_filtered_final!== []){
        tasks_filtered_final.sort(function(a,b){
            return a.assignee.name.localeCompare(b.assignee.name)
        })
    }

    res.render('project',{
        user: req.session.user,
        project: currentProject,
        tasks:tasks_filtered_final,
        statuslist: statuslist});
});

router.get('/edit', async(req, res) => {
    if (req.session.project == 'unknown'){
        res.redirect('/calendme');
    }
    else {
        // find associated project
        let currentProject = await project.findById(req.session.project)
            .catch((mongoError) => res.render('error', {error: mongoError}));

        let listUsers = await user.find();

        res.render('editProject', {user: req.session.user, project: currentProject, listUsers : listUsers});
    }
});

//Process to edit a project (members and name)
router.post('/edit', async(req, res) => {

    //set project to display
    req.session.project = req.body.project;

    //update project
    await project.updateOne({_id: req.body.project}, req.body)
        .catch((err) => {
            res.redirect('/calendme/project/edit')});

    res.redirect('/calendme/project');
});

//Function used to delete a task in two different routes
async function deleteTask(task_id, res){
    //First delete all the journals related to the task
    let listJournals = await journal.find({task : task_id});
    for(let i =0; i<listJournals.length; i++){
        await journal.deleteOne({_id: listJournals[i]._id}, function (err) {
            if (err) return res.send(500, {error: err});
        });
    }

    //Then delete the task
    await task.deleteOne({_id: task_id}, function (err) {
        if (err) return res.send(500, {error: err});
    });
}

//Process to delete a project or a task according to which field of req.body is filled
router.post('/delete', async(req,res) => {

    //If it's a project, first delete associated tasks with the function
    if(req.body.projectToDelete) {
        let listTasks = await task.find({project : req.body.projectToDelete});
        for(let i =0; i<listTasks.length; i++){
            await deleteTask(listTasks[i]._id, res);
        }
        await project.deleteOne({_id: req.body.projectToDelete}, function (err) {
            if (err) return res.send(500, {error: err});
        });
        res.redirect('/calendme');
    }

    //If it's a task, just use the function
    else if(req.body.taskToDelete){
        await deleteTask(req.body.taskToDelete, res);
        res.redirect('/calendme/project');
    }
});


module.exports = router;