import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { QuestionsTypeService } from '../question-config/questions-type.service';
import { QuestionService } from '../question.service';
import { TypeQuestion } from '../_models/questions-type.model';
import { validAnswerChoice } from '../custom-validators';


@Component({
  selector: 'app-add-new-question',
  templateUrl: './add-new-question.component.html',
  styleUrls: ['./add-new-question.component.scss']
})
export class AddNewQuestionComponent implements OnInit {

  private _subscription: Subscription[] = [];
   
  public form: FormGroup;
  public questionToUpdate: Object | undefined = {};
  public typeQuestions: TypeQuestion[];
  public languages = [
    { value: 'ENGLISH', viewValue: 'ENGLISH' },
    { value: 'HINDI', viewValue: 'HINDI' },
    { value: 'ARABIC', viewValue: 'ARABIC' },
  ]



  constructor(
    public fb: FormBuilder,
    private _questionsService: QuestionService,
    private _questionsTypeService : QuestionsTypeService,
    private _router: Router,
    private _route: ActivatedRoute,
  ) {
    
    this.form = this.fb.group({
      id: null,
      text: null,
      language :null,
      createdBy: null,
      createdDate: null,
      lastModifiedBy: null,
      lastModifiedDate: null,
      isArchived: null,
      archivedDate: null,
      typeQuestion: null,
      answerChoices: this.fb.array([this.createOption(), this.createOption()]),

    });
  
  }

  /**
  *  On Init
  */

   ngOnInit(): void {

    this._getQuestionType()
  
  }

/**
*  On destroy
*/
ngOnDestroy(): void {
  this._subscription.forEach(subscription => subscription.unsubscribe());
}



public resetFormWithDefaultValues() {
  this.form.reset();
}

public get f() {
  return this.form.controls;
}


public trackQuestionTypeById(index: number, item: TypeQuestion) {
  return item.id;
}


addItem() {
  (<FormArray>this.form.get('answerChoices')).push(this.createOption());
}


removeItem(idx: number): void {
  (<FormArray>this.form.get('answerChoices')).removeAt(idx);
}


public getControls(frmGrp: FormGroup, key: string) {
  return (<FormArray>frmGrp.controls[key]).controls;
}


formArrayContols(index: number, key: string) {
  return (<FormArray>(<FormArray>this.form.controls[key]).controls[index]).controls
}


createOption() {
  return this.fb.group({
    text: [null , validAnswerChoice],
    createdBy:null,
    createdDate:null,
    lastModifiedBy: null,
    lastModifiedDate: null,
    isArchived:null,
    archivedDate: null,
  });
}



  isValidFormSubmitted = false;

  onSubmit() {

      if (this.form.invalid) {
        this.isValidFormSubmitted = false;
        
        Swal.fire({
          icon: 'error',
          title: 'ERROR!',
          text: 'please complete all the fields !',
          customClass: {
            confirmButton: 'btn btn-primary'
          }
        })
      }

      else {
        this.isValidFormSubmitted = true
        let str : string = String(this.form.value.text)
      
         
          //let result =  str.charAt(str.length-1) !== '?' ? this.form.value.text+' ?' : this.form.value.text
           //this.form.value.text = result;
          
           let hoursToAdd = 0;
           this.form.value.answerChoices.forEach((choice:any) => {
            if (!choice.createdDate) {
              let now = new Date(Date.now());
              now.setHours(now.getHours() + hoursToAdd);
              choice.createdDate = now.toISOString();
              hoursToAdd++;
          }
        });

           
          this._subscription.push(
          this._questionsService.addQuestion(this.form.value).subscribe(
            (response) => { },
            (err) => {
              Swal.fire({
                icon: 'error',
                title: 'Error : Form Invalid!',
                text: 'please complete all the fields !' ,
                customClass: {
                  confirmButton: 'btn btn-primary'
                }
              })
            },
            () => {
              Swal.fire({
                icon: 'success',
                title: 'Added!',
                text: 'the question has been added successfully',
                customClass: {
                  confirmButton: 'btn btn-success'
                }
              })
              this.form.reset();
              this._router.navigate(['/home/question/list']);
            }
          )
        )
      }
    
    
  } 
  
  
  private _getQuestionType() {
    
    this._subscription.push(
      this._questionsTypeService.getQuestionTypesAll().subscribe((response) => {
      this.typeQuestions = response;
     
      })
    )
  }

  


}
