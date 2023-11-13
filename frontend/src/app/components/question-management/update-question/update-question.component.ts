import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
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
      answerChoices: this.fb.array([]),

    });


  }
 
  ngOnInit(): void {
    

    this._route.queryParamMap.subscribe(
      params => {
        
        let id  = parseInt(params.get("id")!);

        this._questionsService.getQuestionById(id).subscribe(data => {
           data;

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
    });
  }


  private _addOption() {
    return this.fb.group({
      text:  [null , validAnswerChoice],
      createdBy:null,
      createdDate:null,
      lastModifiedBy: null,
      lastModifiedDate: null,
      isArchived:null,
      archivedDate: null,
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
