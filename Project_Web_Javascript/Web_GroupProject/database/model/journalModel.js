const mongoose = require('../connect');

// A scheme describing journal documents in the database
const journalSchema = mongoose.Schema({
    date: Date,
    author: {type : mongoose.Schema.ObjectId, ref : 'users'},
    entry: String,
    task: mongoose.Schema.ObjectId,
});

// Prepare a collection named 'journal'
const journalModel = mongoose.model('journals', journalSchema);

module.exports = journalModel;