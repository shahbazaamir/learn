import React from 'react';
import { Button } from 'react-bootstrap';

function Project(props) {
  return (
    
    <div class="row" >
      <div class="col-md-1"> {props.project.projectId}</div>
       <div class="col-md-3"> {props.project.projectDesc}</div> 
      <div class="col-md-3">  <Button>Delete</Button> </div> 
    </div>
  );
}

export default Project;
