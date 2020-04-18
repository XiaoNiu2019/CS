var mongoose = require('../Controllers/connect');
var ObjectId = mongoose.Schema.Types.ObjectId;

var projectsSchema = new mongoose.Schema({
    name: { type: String },
    members: [{ type: ObjectId, ref: 'User' }]
});

//projectsSchema
   // .virtual('projectname')
   // .get(function () {
   //     return this.name;
   // });

var Projects = mongoose.model('Project', projectsSchema);

module.exports = Projects;