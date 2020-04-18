const mongoose = require('mongoose');

//Connect mongoose to the database
mongoose.connect('mongodb://localhost:27017/calendme', {useNewUrlParser: true});

module.exports = mongoose;
