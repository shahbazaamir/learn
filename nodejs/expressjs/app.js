var express = require('express');
var bodyParser = require('body-parser');
var app = express();

  
app.post('/api/users', function(req, res) {
   console.log(req.body);

  res.send('success');
});
 

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
 


app.listen(8082);

 