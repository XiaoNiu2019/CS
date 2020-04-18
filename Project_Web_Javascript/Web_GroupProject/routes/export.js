var express = require('express');
const json2csv = require('json2csv');
var xmlbuilder = require('xmlbuilder');
var mongoXlsx = require('mongo-xlsx');

var router = express.Router();

//Initialization of the models
const project = require('../database/model/projectModel');
const task = require('../database/model/taskModel');
const journal = require('../database/model/journalModel');
const status = require('../database/model/statusModel');
const user = require('../database/model/userModel');

//display export page
router.get('/', async (req, res, next) => {
    res.redirect('/calendme');
});

//Process to export data
router.post('/', async (req, res) => {
    //Collect the data to export
    let exportData = {};
    exportData.projects = await project.find().catch((mongoError) => res.render('error', {error: mongoError}));
    exportData.tasks = await task.find().catch((mongoError) => res.render('error', {error: mongoError}));
    exportData.journals = await journal.find().catch((mongoError) => res.render('error', {error: mongoError}));
    exportData.status = await status.find().catch((mongoError) => res.render('error', {error: mongoError}));
    exportData.users = await user.find().catch((mongoError) => res.render('error', {error: mongoError}));

    //set actual date for files name
    let now = new Date();
    let date = now.toISOString().split('T')[0] + "_" + now.toISOString().split('T')[1].split('.')[0];

    //tell the navigator what it will receive
    res.setHeader('Content-Type', 'text/plain');

    //Select right format to export
    if(req.body.exportType == 'JSON'){
        //Set name of the file to send
        res.setHeader('Content-Disposition', 'attachment; filename=\"' + 'calendme_' + date +'.json\"');

        //send data to the navigator
        res.send(exportData);
    }
    else if(req.body.exportType == 'CSV'){
        //Set name of the file to send
        res.setHeader('Content-Disposition', 'attachment; filename=\"' + 'calendme_' + date +'.csv\"');

        //set fields to store in the csv file
        let field = [{label: 'projects.id', value: 'projects._doc._id'}, {label: 'projects.name', value: 'projects._doc.name'}, 'projects.members', 'tasks._id', 'tasks.name', 'tasks.start_date', 'tasks.due_date', 'tasks.status', 'tasks.assignee', 'tasks.description', 'journals.id', 'journals.date', 'journals.author', 'journals.entry', 'journals.task', 'status._id', 'status.name', {label: 'users.id', value: 'users._doc._id'}, {label: 'users.name', value: 'users._doc.name'}, {label: 'users.firstname', value: 'users._doc.firstname'}, {label: 'users.login', value: 'users._doc.login'}, {label: 'users.password', value: 'users._doc.password'}, 'users.roles'];
        //Use json2csv package to generate csv file
        res.send(json2csv.parse(exportData, {fields:field, unwind:['projects', 'projects.members', 'tasks', 'journals', 'status', 'users', 'users.roles'], unwindBlank: true}));

    }
    else if(req.body.exportType == 'XML'){
        //Set name of the file to send
        res.setHeader('Content-Disposition', 'attachment; filename=\"' + 'calendme_' + date +'.xml\"');
        //Use xmlbuilder package to generate xml file
        res.send(xmlbuilder.create(JSON.parse(JSON.stringify(exportData))).end({ pretty: true}));
    }
    else if(req.body.exportType == 'XLSX'){
        //Set name of the file to send
        res.setHeader('Content-Disposition', 'attachment; filename=\"' + 'calendme_' + date +'.xlsx\"');

        //model for xlsx exportation
        const xlsxProjectModel = mongoXlsx.buildDynamicModel(exportData.projects);
        const xlsxTaskModel = mongoXlsx.buildDynamicModel(exportData.tasks);
        const xlsxJournalModel = mongoXlsx.buildDynamicModel(exportData.journals);
        const xlsxStatusModel = mongoXlsx.buildDynamicModel(exportData.status);
        const xlsxUserModel = mongoXlsx.buildDynamicModel(exportData.users);

        //Array with each sheet of excel file
        let dataArray = [mongoXlsx.mongoData2XlsxData(exportData.projects, xlsxProjectModel), mongoXlsx.mongoData2XlsxData(exportData.tasks, xlsxTaskModel),
            mongoXlsx.mongoData2XlsxData(exportData.journals, xlsxJournalModel), mongoXlsx.mongoData2XlsxData(exportData.status, xlsxStatusModel),
            mongoXlsx.mongoData2XlsxData(exportData.users, xlsxUserModel)];
        //Name of each sheet
        let nameArray = ['projects', 'tasks', 'journals', 'status', 'users'];

        //Generate excel file with modified mongoXlsx
        res.send(mongoXlsx.mongoData2XlsxMultiPage(dataArray, nameArray, {save: false, fileName: 'calendme_' + date +'.xlsx' } ,function(err, data) {
            if(err){ console.log(err) }
        }));

    }

});


module.exports = router;