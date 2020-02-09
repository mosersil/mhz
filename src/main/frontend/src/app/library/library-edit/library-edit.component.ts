import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LibraryService} from "../../common/services/library.service";
import {Composer} from "../../common/entities/composer";
import {Composition} from "../../common/entities/composition";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-library-edit',
  templateUrl: './library-edit.component.html',
  styleUrls: ['./library-edit.component.scss']
})
export class LibraryEditComponent implements OnInit, OnChanges {

  @Input() composition: Composition;
  @Input() editing: false;

  myForm: FormGroup;
  composer_list: Composer[];

  constructor(private fb: FormBuilder, private ls: LibraryService, private route: ActivatedRoute, private router:Router) {
  }

  ngOnInit() {

    const params = this.route.snapshot.paramMap;
    this.ls.getComposition(params.get('id')).subscribe(data => {
      this.composition = data;
    })

    this.ls.getComposers().subscribe(data => {
      this.composer_list = data;
    });


  }

  ngOnChanges() {
    this.initForm();
    this.setFormValues(this.composition);
  }


  private setFormValues(composition: Composition) {
    this.myForm.patchValue(composition);
    this.myForm.setControl("composers", this.buildComposersArray(composition.composers));
    this.myForm.setControl("arrangers", this.buildComposersArray(composition.arrangers));
  }

  private initForm() {
    this.myForm = this.fb.group({
      id: ['', Validators.required],
      title: [''],
      subtitle: [''],
      description: [''],
      composers: this.buildComposersArray([]),
      arrangers: this.buildComposersArray([])
    });
  }

  private buildComposersArray(values: Composer[]): FormArray {
    return this.fb.array(values, Validators.required);
  }

  submitForm() {
    const formValue = this.myForm.value;

    const composers = formValue.composers.filter(composer => composer);
    const arrangers = formValue.arrangers.filter(arranger => arranger);

    const newComposition: Composition = <Composition>{
      id: null,
      title: formValue.title,
      subtitle: formValue.subtitle,
      description: formValue.description,
      genre: formValue.genre,
      tag: formValue.tag,
      composers: composers,
      arrangers: arrangers
    };

    if (this.editing) {
      this.ls.update(newComposition).subscribe(data => {
        console.log("created new composition: " + data);
      })
    }
    else {
      this.ls.create(newComposition).subscribe(data => {
        console.log("created new composition: " + data);
      })
    }

  }

  updateComposition(composition: Composition) {
    this.ls.update(composition).subscribe(() => {
      this.router.navigate(['/library/composition/'+composition.id]);
    });
  }

  get composers(): FormArray {
    return this.myForm.get('composers') as FormArray
  }

  get arrangers(): FormArray {
    return this.myForm.get('arrangers') as FormArray
  }

  addComposerControl() {
    this.composers.push(this.fb.control(null));
  }

  addArrangerControl() {
    this.arrangers.push(this.fb.control(null));
  }

}
