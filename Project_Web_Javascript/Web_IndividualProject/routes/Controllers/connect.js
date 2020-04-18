const mongoose = require('mongoose');

mongoose.connect('mongodb://localhost:27017/calendme', {useNewUrlParser: true});

module.exports = mongoose;
