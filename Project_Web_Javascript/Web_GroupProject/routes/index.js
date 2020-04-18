var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    //redirect to calendme app
    res.redirect('/calendme');
});

module.exports = router;
