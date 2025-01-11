import { EventEmitter, Injectable } from '@angular/core';
@Injectable()
export class ProjectService{
	 constructor() {}
	loadRepositories(){
		return [
		{"name":"new","url":"file:///F:/shahbaz/work/projects/repository/allProjects/"},
		{"name":"mongoDB","url":"file:///F:/shahbaz/work/projects/collegeSearch/repository/main/branches/1/eclipseWorkspace3"}
		];
	}
}