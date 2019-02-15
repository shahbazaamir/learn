import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecidentialstatusComponent } from './recidentialstatus.component';

describe('RecidentialstatusComponent', () => {
  let component: RecidentialstatusComponent;
  let fixture: ComponentFixture<RecidentialstatusComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecidentialstatusComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecidentialstatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
