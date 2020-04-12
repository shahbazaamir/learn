var express = require('express');

var router = express.Router();
var Project = require('./project.service.js');

router.get('/', function (req, res) {
  Project.getAllProject(function (err, project) {
    console.log('controller')
    if (err)
      res.send(err);
    console.log('res', project);
    res.send({'resp':project});
  });
});

router.post('/create', function (req, res) {
  console.log(req.body);
  var new_project = new Project(req.body);
  console.log(new_project);
  Project.createProject(new_project, function (err, project) {
    if (err)
      res.send(err);
    res.json(project);
  });
});


router.post('/delete/:id', function (req, res) {
  console.log(req.params);
  console.log(req.url);
  console.log(req.query);

  Project.remove(req.params.id, function (err, project) {
    if (err)
      res.send(err);
    res.json(project);
  });
});



module.exports = router;