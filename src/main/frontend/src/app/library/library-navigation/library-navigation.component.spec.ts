import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LibraryNavigationComponent} from './library-navigation.component';

describe('LibraryNavigationComponent', () => {
  let component: LibraryNavigationComponent;
  let fixture: ComponentFixture<LibraryNavigationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LibraryNavigationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LibraryNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
