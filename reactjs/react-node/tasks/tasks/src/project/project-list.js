import React from 'react';
import Project from './project.js';



class ProjectList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "project-list",
      projects: []
    };
  }

  componentDidMount() {
    fetch(`http://localhost:8082/projects`)
      .then(res => res.json())
      .then(result => {
        console.log(result);
        this.setState({
          projects: result.resp
        });
      })
  }
  
  render() {
    console.log(this.state);
    const retVal = this.state.projects.map(
      (project) => <Project project={project}  >  </Project>

    );
    return [  (<h1>Project List</h1>) ,retVal ];
  }
}


export default ProjectList;

