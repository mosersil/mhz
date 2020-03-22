import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LibraryFormMessagesComponent} from './library-form-messages.component';

describe('LibraryFormMessagesComponent', () => {
  let component: LibraryFormMessagesComponent;
  let fixture: ComponentFixture<LibraryFormMessagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LibraryFormMessagesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LibraryFormMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
