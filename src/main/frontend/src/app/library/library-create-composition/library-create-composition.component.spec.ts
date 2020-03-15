import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LibraryCreateCompositionComponent} from './library-create-composition.component';

describe('LibraryCreateCompositionComponent', () => {
  let component: LibraryCreateCompositionComponent;
  let fixture: ComponentFixture<LibraryCreateCompositionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LibraryCreateCompositionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LibraryCreateCompositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
