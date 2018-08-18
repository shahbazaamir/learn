import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NseCodeComponent } from './nse-code.component';

describe('NseCodeComponent', () => {
  let component: NseCodeComponent;
  let fixture: ComponentFixture<NseCodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NseCodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NseCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
