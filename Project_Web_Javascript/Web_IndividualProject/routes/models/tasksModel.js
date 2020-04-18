var mongoose = require('../Controllers/connect');
var ObjectId = mongoose.Schema.Types.ObjectId

var tasksSchema = new mongoose.Schema({

    project : { type: ObjectId, ref: 'Project' },
    name : { type: String },
    description : { type: String },
    assignee : { type: ObjectId, ref: 'User' },
    start_date: { type: Date },
    due_date: { type: Date },
    priority: { type: Number},
    status: { type: ObjectId, ref: 'Status' }

});


var Tasks = mongoose.model('Task', tasksSchema);

module.exports = Tasks;