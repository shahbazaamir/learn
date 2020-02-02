import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('test add ', () => {
    expect(component.add(1,2) ).toEqual(3);
  });

  it('test add negative', () => { 
    expect(component.add(2,2) ).toEqual(3); 
  });
  it('test add negative2', () => { 
    expect(component.add(2,2) ).toEqual(3); 
  });

});
