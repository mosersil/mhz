import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LibraryUploadComponent} from './library-upload.component';

describe('LibraryUploadComponent', () => {
  let component: LibraryUploadComponent;
  let fixture: ComponentFixture<LibraryUploadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LibraryUploadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LibraryUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
