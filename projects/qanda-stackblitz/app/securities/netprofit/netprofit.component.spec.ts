import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetprofitComponent } from './netprofit.component';

describe('NetprofitComponent', () => {
  let component: NetprofitComponent;
  let fixture: ComponentFixture<NetprofitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetprofitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetprofitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
