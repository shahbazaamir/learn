import { Directive , ElementRef,Input} from '@angular/core';

@Directive({
  selector: '[appContent]'
})
export class ContentDirective {
  @Input()  innerHtml ;
  constructor(private el: ElementRef) { }
  
  ngOnInit() {
    this.el.nativeElement.innerHTML = this.innerHtml;
    console.log(this.innerHtml);
   }
}
