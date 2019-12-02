import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AddresslistGeneratorComponent} from './addresslist-generator.component';

describe('AddresslistGeneratorComponent', () => {
  let component: AddresslistGeneratorComponent;
  let fixture: ComponentFixture<AddresslistGeneratorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddresslistGeneratorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddresslistGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
