import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LibraryService} from "../../common/services/library.service";
import {Composer} from "../../common/entities/composer";
import {Composition} from "../../common/entities/composition";
import {Router} from "@angular/router";
import {StaticFile} from "../../common/entities/staticfile";

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
  addedSheets: StaticFile[];

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
    this.compositionForm.setControl("sheets", this.buildStaticFileArray(composition.sheets));
    this.compositionForm.setControl("samples", this.buildStaticFileArray(composition.samples));
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
      sheets: this.buildStaticFileArray([]),
      samples: this.buildStaticFileArray([])
    });
    console.log(this.compositionForm);
  }

  private buildStaticFileArray(values: StaticFile[]): FormArray {
    return this.fb.array(values.map(t => this.fb.group(
      {
        id: [t.id],
        title: [t.title],
        description: [t.description],
        created: [t.created],
        staticFileCategory: [t.staticFileCategory],
        location: [t.location],
        fileType: [t.fileType]
      }
    )));
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
    const sheets = formValue.sheets.map(sheet => sheet);
    const samples = formValue.samples.map(sample => sample);

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
    newComposition.samples = samples;

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

  get samples(): FormArray {
    return <FormArray>this.compositionForm.get('samples') as FormArray
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

  removeSampleControl(index: number) {
    this.samples.removeAt(index)
  }


  pushSheet(sheet:StaticFile) {
    this.composition.sheets.push(sheet);
    this.setFormValues(this.composition);
  }

  pushSample(sheet:StaticFile) {
    this.composition.samples.push(sheet);
    this.setFormValues(this.composition);
  }

}
