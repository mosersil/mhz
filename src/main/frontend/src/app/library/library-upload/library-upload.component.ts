import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Sheet} from "../../common/entities/sheet";
import {LibraryService} from "../../common/services/library.service";
import {Composition} from "../../common/entities/composition";

@Component({
  selector: 'app-library-upload',
  templateUrl: './library-upload.component.html',
  styleUrls: ['./library-upload.component.sass']
})
export class LibraryUploadComponent implements OnInit {

  @ViewChild('file', {static: false}) file
  public files: Set<File> = new Set()
  progress
  canBeClosed = true
  primaryButtonText = 'Upload'
  showCancelButton = true
  uploading = false
  uploadSuccessful = false

  @Input()
  composition:Composition;

  @Output() uploadEvent = new EventEmitter<Sheet>();

  constructor(private ls:LibraryService) { }

  ngOnInit() {
  }

  addFiles() {
    this.file.nativeElement.click();

    console.log("addFiles called. file="+this.file);
  }

  onFilesAdded() {
    const files: { [key: string]: File } = this.file.nativeElement.files;
    for (let key in files) {
      if (!isNaN(parseInt(key))) {
        this.files.add(files[key]);

        const sheet: Sheet = new Sheet();
        sheet.title = files[key].name;
        sheet.location = files[key].name;
        this.ls.uploadSheet(this.composition.id, sheet.title, files[key]).subscribe(response =>
        {
          let sheet:Sheet = response;
          this.uploadEvent.emit(sheet);
        });

      }
    }

  }

}
