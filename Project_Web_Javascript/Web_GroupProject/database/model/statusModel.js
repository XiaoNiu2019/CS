const mongoose = require('../connect');

// A scheme describing status documents in the database
const statusSchema = mongoose.Schema({
    name: String,
});

// Prepare a collection named 'status'
const statusModel = mongoose.model('status', statusSchema);

module.exports = statusModel;