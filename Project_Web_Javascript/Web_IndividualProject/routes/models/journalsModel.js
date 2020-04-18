var mongoose = require('../Controllers/connect');
var ObjectId = mongoose.Schema.Types.ObjectId

var journalSchema = new mongoose.Schema({
    date: { type: Date },
    entry: { type: String },
    author: { type: ObjectId, ref: 'User' },
    task: { type: ObjectId, ref: 'Task' }
});


var Journals = mongoose.model('Journal', journalSchema);
module.exports = Journals;