var express = require('express');
var app = express();

var users = require('./service/users.js');

app.use(express.static('public'));

//app.get('/', function (req, res) {
  // res.send('Hello World');
//})

app.use('/users', users);

var server = app.listen(8082, function () {
   var host = server.address().address;
   var port = server.address().port;

   console.log("Example app listening at http://%s:%s", host, port)
})