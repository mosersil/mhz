import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SignupmemberComponent} from './signupmember.component';

describe('SignupmemberComponent', () => {
  let component: SignupmemberComponent;
  let fixture: ComponentFixture<SignupmemberComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignupmemberComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignupmemberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
