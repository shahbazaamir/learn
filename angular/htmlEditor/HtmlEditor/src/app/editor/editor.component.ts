import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.sass']
})
export class EditorComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  execCom(command){
    document.execCommand(command);
  }
}
