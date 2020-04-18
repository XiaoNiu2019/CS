const mongoose = require('../connect');

// A scheme describing task documents in the database
const taskSchema = mongoose.Schema({
    name: String,
    start_date:Date,
    due_date: Date,
    status: {type : mongoose.Schema.ObjectId, ref : 'status'},
    project: {type : mongoose.Schema.ObjectId, ref : 'projects'},
    assignee: {type : mongoose.Schema.ObjectId, ref : 'users'},
    description: String,
    progress: {type:Number, min:0, max:100}
});

// Prepare a collection named 'tasks'
const taskModel = mongoose.model('tasks', taskSchema);

module.exports = taskModel;
