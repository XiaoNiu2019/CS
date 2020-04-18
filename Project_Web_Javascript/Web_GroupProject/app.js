var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var session = require('express-session');

const exphbs  = require('express-handlebars');

//load routers
var indexRouter = require('./routes/index');
var authRouter = require('./routes/auth');
var homeRouter = require('./routes/home');
var projectRouter = require('./routes/project');
var taskRouter = require('./routes/task');
var exportRouter = require('./routes/export');

var app = express();

// view engine setup
app.engine('.hbs', exphbs({
  defaultLayout: 'main',
  extname: '.hbs',
  helpers: require('./config/handlebars-helpers')
}));

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', '.hbs');


app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//set express-session
app.use(session({
  secret: 'my little secret',
  saveUninitialized: false,
  resave: false
}));

//set routers
app.use('/', indexRouter);
app.use('/calendme/auth', authRouter);
app.use('/calendme', homeRouter);
app.use('/calendme/project', projectRouter);
app.use('/calendme/task', taskRouter);
app.use('/calendme/export', exportRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
