var mongoose = require('../Controllers/connect');

var UserSchema = new mongoose.Schema({
    firstname: String,
    name: String,
    login: String,
    password: String
});


var User = mongoose.model('User', UserSchema);

module.exports = User;