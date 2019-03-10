import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildcComponent } from './childc.component';

describe('ChildcComponent', () => {
  let component: ChildcComponent;
  let fixture: ComponentFixture<ChildcComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChildcComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChildcComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
