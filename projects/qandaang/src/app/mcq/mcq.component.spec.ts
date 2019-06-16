import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MCQComponent } from './mcq.component';

describe('MCQComponent', () => {
  let component: MCQComponent;
  let fixture: ComponentFixture<MCQComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MCQComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MCQComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
