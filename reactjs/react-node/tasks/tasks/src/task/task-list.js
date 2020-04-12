import React from 'react';
import Task from './task.js';



class TaskList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "task-list",
      tasks: []
    };
  }

  componentDidMount() {
    fetch(`http://localhost:8082/tasks`)
      .then(res => res.json())
      .then(result => {
        console.log(result);
        this.setState({
          tasks: result.resp
        });
      })
  }
  
  render() {
    console.log(this.state);
    const retVal = this.state.tasks.map(
      (task) => <Task task={task}  >  </Task>

    );
    return [  (<h1>Task List</h1>) ,retVal ];
  }
}


export default TaskList;

