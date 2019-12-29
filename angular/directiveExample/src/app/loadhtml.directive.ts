import { Directive , ElementRef ,Input } from '@angular/core';

@Directive({
  selector: '[appLoadhtml]'
})
export class LoadhtmlDirective {

@Input()  innerHtml ;
  constructor(private el: ElementRef) { 
    console.log(el);
    
    el.nativeElement.style.backgroundColor = 'yellow';
  }

  
ngOnInit() {
   this.el.nativeElement.innerHTML = this.innerHtml;
  
  }

}