import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {StaticFile} from "../../common/entities/staticfile";
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
  btn_label:string

  @Input()
  composition: Composition;

  @Input()
  type: string

  @Output() uploadStaticFileEvent = new EventEmitter<StaticFile>();

  constructor(private ls: LibraryService) {
  }

  ngOnInit() {
    if (this.type === "LIBRARY_SHEETS") {
      this.btn_label = "Noten hinzufügen";
    }
    if (this.type === "LIBRARY_SAMPLES") {
      this.btn_label = "Hörbeispiel hinzufügen";
    }
  }

  addFiles() {
    this.file.nativeElement.click();
  }

  onFilesAdded() {
    const files: { [key: string]: File } = this.file.nativeElement.files;
    for (let key in files) {
      if (!isNaN(parseInt(key))) {
        this.files.add(files[key]);

        this.ls.uploadSheet(this.composition.id, files[key].name, this.type, files[key]).subscribe(response => {
          let uploadedStaticFile: StaticFile = response;
          this.uploadStaticFileEvent.emit(uploadedStaticFile);
        });

      }
    }
  }
}
