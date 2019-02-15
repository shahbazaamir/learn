import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SellpriceComponent } from './sellprice.component';

describe('SellpriceComponent', () => {
  let component: SellpriceComponent;
  let fixture: ComponentFixture<SellpriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SellpriceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SellpriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
