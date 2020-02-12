import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LibraryService} from "../../common/services/library.service";
import {Composer} from "../../common/entities/composer";
import {Composition} from "../../common/entities/composition";
import {Router} from "@angular/router";
import {Sheet} from "../../common/entities/sheet";

@Component({
  selector: 'app-library-form',
  templateUrl: './library-form.component.html',
  styleUrls: ['./library-form.component.scss']
})
export class LibraryFormComponent implements OnInit, OnChanges {

  @Output() submitComposition = new EventEmitter<Composition>();
  @Input() composition: Composition;
  @Input() editing = false;

  compositionForm: FormGroup;
  composer_list: Composer[];
  addedSheets: Sheet[];

  @ViewChild('file', {static: true}) file;
  public files: Set<File> = new Set();

  constructor(private fb: FormBuilder, private ls: LibraryService, private router: Router) {
  }

  ngOnInit() {
    this.initForm();
  }

  ngOnChanges() {
    this.initForm();
    this.setFormValues(this.composition);
  }


  private setFormValues(composition: Composition) {
    this.compositionForm.patchValue(composition)
    this.compositionForm.setControl("composers", this.buildComposersArray(composition.composers));
    this.compositionForm.setControl("arrangers", this.buildComposersArray(composition.arrangers));
    this.compositionForm.setControl("sheets", this.buildSheetsArray(composition.sheets));
  }

  private initForm() {

    this.ls.getComposers().subscribe(data => {
      this.composer_list = data;
    });

    if (this.compositionForm) {
      return;
    }

    this.addedSheets = new Array();

    this.compositionForm = this.fb.group({
      id: [''],
      inventory: [''],
      title: ['', [Validators.required, Validators.minLength(3)]],
      subtitle: [''],
      genre: [''],
      description: [''],
      composers: this.buildComposersArray([]),
      arrangers: this.buildComposersArray([]),
      sheets: this.buildSheetsArray([])
    });
  }

  private buildSheetsArray(values: Sheet[]): FormArray {
    return this.fb.array(values.map(t => t.title + "(" + t.id + ")"));
  }

  private buildComposersArray(values: Composer[]): FormArray {
    return this.fb.array(values.map(t => t.name));
  }

  private getComposerByName(name: string): Composer {

    if (name == null || name.trim() == "") {
      return null;
    }

    var composer: Composer = this.composer_list.find(value => value.name == name);

    if (composer == null) {
      composer = new Composer();
      composer.name = name;
    }

    return composer;
  }

  submitForm() {


    const formValue = this.compositionForm.value;

    const composers = formValue.composers.map(composer => this.getComposerByName(composer));
    const arrangers = formValue.arrangers.map(arranger => this.getComposerByName(arranger));
    const sheets = formValue.sheets.map(sheet => this.getSheetById)

    let newComposition = new Composition();

    newComposition.id = formValue.id;
    newComposition.inventory = formValue.inventory;
    newComposition.title = formValue.title;
    newComposition.subtitle = formValue.subtitle;
    newComposition.description = formValue.description;
    newComposition.genre = formValue.genre;
    newComposition.composers = composers;
    newComposition.arrangers = arrangers;
    newComposition.sheets = sheets;

    this.submitComposition.emit(newComposition);
    this.compositionForm.reset();


  }

  get composers(): FormArray {
    return <FormArray>this.compositionForm.get('composers') as FormArray
  }

  get arrangers(): FormArray {
    return <FormArray>this.compositionForm.get('arrangers') as FormArray
  }

  get sheets(): FormArray {
    return <FormArray>this.compositionForm.get('sheets') as FormArray
  }

  addComposerControl() {
    this.composers.push(this.fb.control(null));
  }

  removeComposerControl(index: number) {
    this.composers.removeAt(index)
  }

  addArrangerControl() {
    this.arrangers.push(this.fb.control(null));
  }

  removeArrangerControl(index: number) {
    this.arrangers.removeAt(index)
  }

  removeSheetControl(index: number) {
    this.sheets.removeAt(index)
  }


  onFileAdded() {
    /*
    const files: { [key: string]: File } = this.file.nativeElement.files;
    for (let key in files) {
      if (!isNaN(parseInt(key))) {
        this.files.add(files[key]);
        const sheet: Sheet = new Sheet();
        sheet.title = files[key].name;
        sheet.location = files[key].name;
        this.ls.uploadSheet(this.composition.id, sheet.title, files[key]);
      }
    }
    this.ls.getComposition(String(this.composition.id)).subscribe(data => {
      this.composition = data;
      this.setFormValues(this.composition);
    });
    */
  }

}
