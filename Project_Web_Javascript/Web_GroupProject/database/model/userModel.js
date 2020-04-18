const mongoose = require('../connect');

// A scheme describing user documents in the database
const userSchema = mongoose.Schema({
    name: String,
    firstname: String,
    login: String,
    password: String,
    roles: Array,
});

// Prepare a collection named 'users'
const userModel = mongoose.model('users', userSchema);

module.exports = userModel;