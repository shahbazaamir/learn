var express = require('express');
var bodyParser = require('body-parser');
var cors = require('cors');

var corsOptions = {
  origin: 'http://localhost:8082',
  optionsSuccessStatus: 200 // some legacy browsers (IE11, various SmartTVs) choke on 204
}

var tasks = require('./service/task.controller.js');
var projects = require('./service/project.controller.js');
var app = express();

app.use(cors() );
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use('/tasks', tasks);

app.use('/projects', projects);

app.listen(8082);

 