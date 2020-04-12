import React from 'react';

import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";
import TaskList from '../task/task-list.js';
import ProjectList from '../project/project-list.js';
import './home.css';

function  Home () {
    return(
        <Router>
        <div >
            
                <ul  class="nav nav-tabs">
                    
                    <li class="links">
                        <Link to="/tasks">Tasks</Link>
                    </li>
                    <li class="links">
                        <Link to="/projects">Projects</Link>
                    </li>
                </ul>
             

         
            <Switch>
            <Route path="/tasks">
                <TaskList />
            </Route>
            <Route path="/projects">
                <ProjectList />
            </Route>
            
            </Switch>
        </div>
        </Router >
    );
}



export default Home;

