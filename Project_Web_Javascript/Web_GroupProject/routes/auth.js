var express = require('express');

var router = express.Router();

const user = require('../database/model/userModel');

// GET page authentification
router.get('/', function(req, res, next) {

    //Treat the different cases (if user is already connected, unconnected, new, or failed to connect)
    if(!req.session.user){
        req.session.user = {};
        req.session.user.login = 'unconnect';
    }

    if(req.session.user.login == 'unconnect') {
        res.render('auth');
    }
    else if(req.session.user.login == 'fail'){
        req.session.user.login = 'unconnect';
        res.render('auth', {result: "authentification fail"});
    }
    else if(req.session.user.login.split(':')[0] == 'new'){
        let login = req.session.user.login.split(':')[1];
        req.session.user.login = 'unconnect';
        res.render('auth', {register : login});
    }
    else{
        res.redirect('/calendme');
    }
});


//Display the user creation page
router.get('/newUser', function(req, res, next){

    //Create a new session
    if(!req.session.user){
        req.session.user = {};
        req.session.user.login = 'unconnect';
    }


    if((req.session.user.login != 'unconnect') && (req.session.user.login != 'fail')) {
        res.redirect('/calendme');
    }

    //Page of creation
    else{
        res.render('newUser');
    }
});


//Process to create a new user
router.post('/newUser', async(req, res) => {
    let newUser = new user();

    //If user filled correctly fields of name and firstname
    if(req.body.name && req.body.firstname){
        newUser.name = req.body.name;
        newUser.firstname = req.body.firstname;

        //Login created directly from name and firstname
        newUser.login = req.body.firstname[0].toLowerCase() + "." + req.body.name.toLowerCase();
        newUser.roles = ["member"];

        //Saving the new user if he has confirmed well the password
        if(req.body.password == req.body.confirmpassword){
            newUser.password = req.body.password;
            console.log(newUser);

            await newUser.save()
                .catch((err) => {
                    res.redirect('newUser');
                });

            //Session now new, have to give the login to the user
            req.session.user.login = 'new:' + newUser.login;
            res.redirect('/calendme/auth');
        }
        else{
            res.render('newUser', {result : 'Passwords don\'t match'});
        }
    }
    else{
        res.render('newUser', {result : 'Missing fields'});
    }

});

// Process authentication form
router.post('/', async(req, res) => {

    //Find user with given informations
    let userFound = await user.findOne(req.body)
        .catch((mongoError) => res.render('error', {error: mongoError}));

    if (userFound){
        //set session variables
        req.session.user.login=userFound.id;
        req.session.user.name = userFound.name;
        req.session.user.firstname = userFound.firstname;
        req.session.user.roles = userFound.roles;
        req.session.project = 'unknown';
        req.session.task = 'unknown';
        req.session.status = 'unknown';

        res.redirect('/calendme');
    }
    else{
        //fail operations
        req.session.user.login='fail';
        res.redirect('/calendme/auth');
    }
});

//Process sign out button
router.post('/logout', (req, res) => {
    req.session.user.login='unconnect';
    res.redirect('/calendme/auth');
});


module.exports = router;