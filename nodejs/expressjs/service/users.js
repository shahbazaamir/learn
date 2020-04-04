var express = require('express');
 
var router = express.Router();
var Task = require('./task.service.js');

router.get('/', function(req, res){
   Task.getAllTask(function(err, task) {

      console.log('controller')
      if (err)
        res.send(err);
        console.log('res', task);
      res.send(task);
    });
});
router.post('/create', function(req, res){
   console.log(req.body);
   var new_task = new Task(req.body);
   console.log(new_task);
   Task.createTask(new_task, function(err, task) {
    
      if (err)
        res.send(err);
      res.json(task);
    });
  // res.send('success.');

});

//export this router to use in our index.js
module.exports = router;