import {Component, Input, OnChanges, OnInit, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LibraryService} from "../../common/services/library.service";
import {Composer} from "../../common/entities/composer";
import {Composition} from "../../common/entities/composition";
import {ActivatedRoute} from "@angular/router";
import {Sheet} from "../../common/entities/sheet";

@Component({
  selector: 'app-library-form',
  templateUrl: './library-form.component.html',
  styleUrls: ['./library-form.component.scss']
})
export class LibraryFormComponent implements OnInit, OnChanges {

  @Input() composition: Composition;
  @Input() editing: false;

  myForm: FormGroup;
  composer_list: Composer[];
  addedSheets: Sheet[];

  @ViewChild('file', {static: true}) file;
  public files: Set<File> = new Set();

  constructor(private fb: FormBuilder, private ls: LibraryService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.ls.getComposers().subscribe(data => {
      this.composer_list = data;
    });
    this.addedSheets = new Array();
  }

  ngOnChanges() {
    this.initForm();
    this.setFormValues(this.composition);
  }


  private setFormValues(composition: Composition) {
    this.myForm.patchValue(composition);
    this.myForm.setControl("composers", this.buildComposersArray(composition.composers));
    this.myForm.setControl("arrangers", this.buildComposersArray(composition.arrangers));
    this.myForm.setControl("sheets", this.buildSheetsArray(composition.sheets));
  }

  private initForm() {
    this.myForm = this.fb.group({
      id: ['', Validators.required],
      inventory: [''],
      title: [''],
      subtitle: [''],
      description: [''],
      composers: this.buildComposersArray([]),
      arrangers: this.buildComposersArray([]),
      sheets: this.buildSheetsArray([])
    });
  }

  private buildSheetsArray(values: Sheet[]): FormArray {
    return this.fb.array(values.map(t => t.title + "("+t.id+")"), Validators.required);
  }

  private buildComposersArray(values: Composer[]): FormArray {
    return this.fb.array(values.map(t => t.name), Validators.required);
  }

  private getComposerByName(name: string): Composer {
    return this.composer_list.find(value => value.name == name);
  }

  submitForm() {
    const formValue = this.myForm.value;

    const composers = formValue.composers.map(composer => this.getComposerByName(composer));
    const arrangers = formValue.arrangers.map(arranger => this.getComposerByName(arranger));

    const newComposition: Composition = <Composition>{
      id: formValue.id,
      inventory: formValue.inventory,
      title: formValue.title,
      subtitle: formValue.subtitle,
      description: formValue.description,
      genre: formValue.genre,
      tag: formValue.tag,
      composers: composers,
      arrangers: arrangers
    };


    this.ls.save(newComposition).subscribe(data => {

      //this.ls.uploadSheet(data.id, formValue.title, this.files);

      //console.log("created new composition: " + data);
    })

  }

  get composers(): FormArray {
    return <FormArray>this.myForm.get('composers') as FormArray
  }

  get arrangers(): FormArray {
    return <FormArray>this.myForm.get('arrangers') as FormArray
  }

  addComposerControl() {
    this.composers.push(this.fb.control(null));
  }

  addArrangerControl() {
    this.arrangers.push(this.fb.control(null));
  }


  onFileAdded() {
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
  }

}
