
'user strict';
var sql = require('./db.js');

//Project object constructor
var Project = function (project) {
    this.project_Desc = project.projectDesc;
    this.project_Ref = project.projectRef;
    this.project_Status = project.projectStatus;
};

var ProjectDto = function (project) {
    this.projectDesc = project.PROJECT_DESC;
    this.projectRef = project.PROJECT_REF;
    this.projectStatus = project.PROJECT_STATUS;
	this.projectId = project.PROJECT_ID;
	this.updatedOn = project.UPDATED_ON;
};



Project.createProject = function (newProject, result) {
    sql.query("INSERT INTO projects set ?", newProject, function (err, res) {

        if (err) {
            console.log("error: ", err);
            result(err, null);
        }
        else {
            console.log(res.insertId);
            result(null, res.insertId);
        }
    });
};
Project.getProjectById = function (projectId, result) {
    sql.query("Select project from projects where id = ? ", projectId, function (err, res) {
        if (err) {
            console.log("error: ", err);
            result(err, null);
        }
        else {
            result(null, res);

        }
    });
};
Project.getAllProject = function (result) {
    sql.query("Select * from projects", function (err, res) {

        if (err) {
            console.log("error: ", err);
            result(null, err);
        }
        else {
            console.log('projects : ', res);
			let projectsDto =res.map(
				(project)=> new ProjectDto(project)
			);
            result(null, projectsDto);
        }
    });
};
Project.updateById = function (id, project, result) {
    sql.query("UPDATE projects SET project = ? WHERE id = ?", [project.project, id], function (err, res) {
        if (err) {
            console.log("error: ", err);
            result(null, err);
        }
        else {
            result(null, res);
        }
    });
};
Project.remove = function (id, result) {
    sql.query("DELETE FROM projects WHERE PROJECT_ID = ?", [id], function (err, res) {

        if (err) {
            console.log("error: ", err);
            result(null, err);
        }
        else {

            result(null, res);
        }
    });
};

module.exports = Project;