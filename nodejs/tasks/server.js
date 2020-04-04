var express = require('express');
var bodyParser = require('body-parser');

var users = require('./service/users.js');
var app = express();

//var users = require('./service/users.js');


//app.use(express.static('public'));

 
//app.get('/', function (req, res) {
  // res.send('Hello World');
//})


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


/*var server = app.listen(8082, function () {
   var host = server.address().address;
   var port = server.address().port;

   console.log("Example app listening at http://%s:%s", host, port)
});*/

/*
app.post('/api/users', function(req, res) {
   console.log(req.body);

  res.send('success');
});
*/

app.use('/users', users);


app.listen(8082);

//app.use('/users', users);