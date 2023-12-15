import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidationErrors, ValidatorFn } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { QuestionsTypeService } from '../question-config/questions-type.service';
import { QuestionService } from '../question.service';
import { AnswerChoice } from '../_models/answer-choice.model';
import { Question } from '../_models/question.model';
import { TypeQuestion } from '../_models/questions-type.model';
import { validAnswerChoice } from '../custom-validators';



@Component({
  selector: 'app-update-question',
  templateUrl: './update-question.component.html',
  styleUrls: ['./update-question.component.scss']
})
export class UpdateQuestionComponent implements OnInit {

  private question_id : any;
  private _subscription: Subscription[] = [];

  
  public questionToUpdate: Object | undefined  = Question;
  public form: FormGroup;
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
      answerChoices: this.fb.array([], {
        validators: [
          this.duplicateTextValidator(),
          this.duplicateAdditionalInputValidator()
        ]
      }),

    });


  }
 
  ngOnInit(): void {
    

    this._route.queryParamMap.subscribe(
      params => {
        
        let id  = parseInt(params.get("id")!);

        this._questionsService.getQuestionById(id).subscribe(data => {
           data;

           data.answerChoices.forEach((choice: any) => {
            // const match = choice.text.match(/\((\d+) points\)/);
            // if (match && match[1]) {
            //   choice.additionalInput = parseInt(match[1], 10);
            // }            
            // // choice.text = choice.text.replace(/\(\d+ points\)/, '').trim();
            const match = choice.text.match(/\d+/);

            if (match) {
              choice.additionalInput = parseInt(match[0], 10);
            }
            const lastIndex = choice.text.lastIndexOf('(');
            if (lastIndex !== -1) {
              choice.text = choice.text.substring(0, lastIndex).trim();
            }
          

          });
          console.log("dattttttttttaaa :",data);
          
          if(data !== undefined ){

           let ques : Question = data          

            for (let index = 0; index < ques.answerChoices.length; index++) {
              const element = ques.answerChoices[index];
              (<FormArray>this.form.get('answerChoices')).push(this._fillOption(element))
            }
             
            this.form.patchValue(ques);
                  
          }

        })
      }
    )

    this._getQuestionType();


  }
  duplicateAdditionalInputValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const answerChoices = control.value as any[];
      const uniqueAdditionalInputValues = new Set<string>();
  
      for (const choice of answerChoices) {
        const additionalInputValue = choice.additionalInput.toString()?.toLowerCase();
  
        if (uniqueAdditionalInputValues.has(additionalInputValue)) {
          return { duplicateAdditionalInput: true };
        }
  
        uniqueAdditionalInputValues.add(additionalInputValue);
      }
  
      return null;
    };
  }
  duplicateTextValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const answerChoices = control.value as any[];
      const uniqueTextValues = new Set<string>();
  
      for (const choice of answerChoices) {
        const textValue = choice.text?.toLowerCase();
  
        if (uniqueTextValues.has(textValue)) {
          return { duplicateText: true };
        }
  
        uniqueTextValues.add(textValue);
      }
  
      return null;
    };
  }

  ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());

  }


  public trackQuestionTypeById(index: number, item: TypeQuestion) {
    return item.id;
  }

  addItem() {
    (<FormArray>this.form.get('answerChoices')).push(this._addOption());
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


  private _fillOption(element: AnswerChoice) {
    return this.fb.group({
      text: element.text,
      createdBy:element.createdBy,
      createdDate:element.createdDate,
      lastModifiedBy: element.lastModifiedBy,
      lastModifiedDate: element.lastModifiedDate,
      isArchived:element.isArchived,
      archivedDate: element.archivedDate,
      additionalInput:element.additionalInput
    });
  }


  private _addOption() {
    return this.fb.group({
      text:  [null],
      createdBy:null,
      createdDate:null,
      lastModifiedBy: null,
      lastModifiedDate: null,
      isArchived:null,
      archivedDate: null,
      additionalInput: [null, validAnswerChoice],
    });
  }

  
  private _getQuestionType() {
      
    this._subscription.push(
      this._questionsTypeService.getQuestionTypesAll().subscribe((response) => {
      this.typeQuestions = response;
     
      })
    )
  }

  
  question: Question = new Question();
  isValidFormSubmitted = false;

  public onSubmit() {



        if (this.form.invalid) {
          this.isValidFormSubmitted = false;
          
          Swal.fire({
            icon: 'error',
            title: 'ERROR!',
            text: 'form invalid',
            customClass: {
              confirmButton: 'btn btn-primary'
            }
          })

        }
        else {
          console.log("this.form.value add question update ::", this.form.value);
          this.form.value.answerChoices.forEach((choice: any) => {
            // choice.additionalInput = 10  ;
            choice.text = `${choice.text} ( ${choice.additionalInput} points ) `;
          });
          this.isValidFormSubmitted = true
          
            this._subscription.push(
            this._questionsService.updateQuestion(this.form.value).subscribe(
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
                  title: 'Updated!',
                  text: 'the question has been updated successfully',
                  customClass: {
                    confirmButton: 'btn btn-success'
                  }
                })
                this._router.navigate(['/home/question/list']);
              }
            )
          )
        }
      
      
    } 

    

    public resetFormWithDefaultValues() {
      this.form.reset();
    }



    public get f() {
      return this.form.controls;
    }


    public goToPage(pageName: string) {
      this._router.navigate([`${pageName}`]);
    }

}
