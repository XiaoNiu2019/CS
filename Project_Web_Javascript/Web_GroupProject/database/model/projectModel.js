const mongoose = require('../connect');

// A scheme describing project documents in the database
const projectSchema = mongoose.Schema({
    name: String,
    members: [{type: mongoose.Schema.ObjectId, ref: 'users'}]
});

// Prepare a collection named 'projects'
const projectModel = mongoose.model('projects', projectSchema);

module.exports = projectModel;