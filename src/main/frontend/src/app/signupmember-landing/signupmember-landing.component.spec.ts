import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SignupmemberLandingComponent} from './signupmember-landing.component';

describe('SignupmemberLandingComponent', () => {
  let component: SignupmemberLandingComponent;
  let fixture: ComponentFixture<SignupmemberLandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignupmemberLandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignupmemberLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
