import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotLandingComponent } from './forgot-landing.component';

describe('ForgotLandingComponent', () => {
  let component: ForgotLandingComponent;
  let fixture: ComponentFixture<ForgotLandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ForgotLandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ForgotLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
