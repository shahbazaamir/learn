import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuypriceComponent } from './buyprice.component';

describe('BuypriceComponent', () => {
  let component: BuypriceComponent;
  let fixture: ComponentFixture<BuypriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuypriceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuypriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
