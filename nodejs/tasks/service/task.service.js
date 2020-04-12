
'user strict';
var sql = require('./db.js');

//Task object constructor
var Task = function (task) {
    this.task_Desc = task.taskDesc;
    this.task_Ref = task.taskRef;
    this.task_Status = task.taskStatus;
};


var TaskDto = function (task) {
    this.taskDesc = task.TASK_DESC;
    this.taskRef = task.TASK_REF;
    this.taskStatus = task.TASK_STATUS;
	this.taskId = task.TASK_ID;
	this.updatedOn = task.UPDATED_ON;
};



Task.createTask = function (newTask, result) {
    sql.query("INSERT INTO tasks set ?", newTask, function (err, res) {

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
Task.getTaskById = function (taskId, result) {
    sql.query("Select task from tasks where id = ? ", taskId, function (err, res) {
        if (err) {
            console.log("error: ", err);
            result(err, null);
        }
        else {
            result(null, res);

        }
    });
};
Task.getAllTask = function (result) {
    sql.query("Select * from tasks", function (err, res) {

        if (err) {
            console.log("error: ", err);
            result(null, err);
        }
        else {
            console.log('tasks : ', res);
			let tasksDto =res.map(
				(task)=> new TaskDto(task)
			);
            result(null, tasksDto);
        }
    });
};
Task.updateById = function (id, task, result) {
    sql.query("UPDATE tasks SET task = ? WHERE id = ?", [task.task, id], function (err, res) {
        if (err) {
            console.log("error: ", err);
            result(null, err);
        }
        else {
            result(null, res);
        }
    });
};
Task.remove = function (id, result) {
    sql.query("DELETE FROM tasks WHERE TASK_ID = ?", [id], function (err, res) {

        if (err) {
            console.log("error: ", err);
            result(null, err);
        }
        else {

            result(null, res);
        }
    });
};

module.exports = Task;