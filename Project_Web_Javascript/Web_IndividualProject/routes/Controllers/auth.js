var User = require('../models/userModel');
var express = require('express');
var router = express.Router();
//var session = require('express-session');

router.get('/auth',function(req, res){
    if (!req.session.loginstat) {req.session.loginstat = 0;}
    res.render('auth',{loginstat:req.session.loginstat})
})



router.post('/auth.form', function (req, res) {

        User.findOne({login : req.body.login, password : req.body.password}, function (err, result){
            if (result== null) {
                //res.flash('Incorrect username or password');
                req.session.loginstat = 1;
                return res.redirect('/auth')
            }
            else {
                //console.log(req.session)
                req.session.loginstat  = 2;
                req.session.user = result;
                //console.log(req.session.user.firstname)
                return res.redirect('/projects');
            }
        });
})


module.exports = router;