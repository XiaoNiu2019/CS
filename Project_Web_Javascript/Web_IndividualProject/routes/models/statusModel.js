var mongoose = require('../Controllers/connect');

var statusSchema = new mongoose.Schema({
    name: { type: String },
});

var Status = mongoose.model('Status', statusSchema);

module.exports = Status;