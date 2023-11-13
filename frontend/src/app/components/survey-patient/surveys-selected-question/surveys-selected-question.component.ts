import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { fromEvent, Subject, takeUntil } from 'rxjs';
import Swal from 'sweetalert2';
import { Question } from '../../question-management/_models/question.model';
import { Survey } from '../../survey-management/_models/survey.model';
import { SurveyPatientService } from '../survey-patient.service';
import { Answer, FinalAnswer } from '../_models/answer.model';
import { NgxStarRatingModule } from 'ngx-star-rating';
import { AccountService } from 'src/app/components/authentification/authService/account.service';
import { User } from 'src/app/components/user-management/user.model';

@Component({
  selector: 'app-surveys-selected-question',
  templateUrl: './surveys-selected-question.component.html',
  styleUrls: ['./surveys-selected-question.component.scss']
})
export class SurveysSelectedQuestionComponent implements OnInit {
  
  public survey :Survey;
  // public questions: Question[];
  public questions: Question[] = [];
  public answers:any[] = [];
  public currentQuestion = 0;
  public model:any;
  public multipleSelectedAnswers: any[] = [];
  public radioChoice:any;
  public radioChoiceScore:any;
  public invitationId : number;
  public showError: boolean = false;
  public totalScore = 0;
  public buttonDisabled = false;
  userInfo:User;
  private unsubscriber: Subject<void> = new Subject<void>();

  public checked = false;
 
  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private _surveysService: SurveyPatientService,
    private localStorageService: LocalStorageService,
    private sessionStorageService: SessionStorageService,
  ) { }

  ngOnInit(): void {
    this.userInfo = this.localStorageService.retrieve('accountInfo') ? this.localStorageService.retrieve('accountInfo') : this.sessionStorageService.retrieve('accountInfo');
    this._route.queryParamMap.subscribe(
      params => {
        
        let id  = parseInt(params.get("id")!);
        let invitationId = parseInt(params.get("invitationId")!);
        this.invitationId = invitationId
        this._surveysService.getSurveyById(id).subscribe(data => {
          this.survey = data;
          this.questions = data.questions!;
          this.questions.forEach(question => {
            question.answerChoices.sort((a, b) => {
              const dateA = new Date(a.createdDate!);
              const dateB = new Date(b.createdDate!);
              return dateA.getTime() - dateB.getTime();
            });
          });
        })
      }
    )

    history.pushState(null, '');

    fromEvent(window, 'popstate')
      .pipe(takeUntil(this.unsubscriber))
      .subscribe((_) => {
        history.pushState(null, '');
        this.showError = true;
      });
  }

  ngOnDestroy(): void {
    this.unsubscriber.next();
    this.unsubscriber.complete();
    this.totalScore=0;
  }

  submitCurrentQuestion() {

   let  answer :Answer = new Answer();
    answer.survey= this.survey
    answer.question = this.survey.questions![this.currentQuestion]
    if(this.radioChoice) {

      let num = this.radioChoice['text'].match(/\d+/)[0];
      this.totalScore += parseInt(num)

      answer.answerChoices=[this.radioChoice]

    } else if(this.multipleSelectedAnswers.length != 0) {

      this.multipleSelectedAnswers.forEach(an => {
        let num = an['text'].match(/\d+/)[0] 
        this.totalScore += parseInt(num)
      })

      answer.answerChoices = this.multipleSelectedAnswers

    }else if(this.radioChoiceScore){

      let num = this.radioChoiceScore['text'].match(/\d+/)[0];

      this.totalScore += parseInt(num)

      answer.answerChoices=[this.radioChoiceScore]
    }
    this.checked=false

    this.answers.push(answer)
   
    this.radioChoice='';
    this.radioChoiceScore= ''
    this.multipleSelectedAnswers=[];


    ++this.currentQuestion;
  }

  getSelection(item :any) {
    return this.multipleSelectedAnswers.findIndex(s => s.id === item.id) !== -1;
  }


  changeHandler(item: any, event: any) {
    const id = item.id;
    const index = this.multipleSelectedAnswers.findIndex(u => u.id === id);
    if (index === -1) {
      this.multipleSelectedAnswers = [...this.multipleSelectedAnswers, item];
    } else {
      this.multipleSelectedAnswers = this.multipleSelectedAnswers.filter(user => user.id !== item.id)
    }
  }

  public finish(){
     
    this.buttonDisabled = true
    let finalAnswer: FinalAnswer = new FinalAnswer();
    let email = this.localStorageService.retrieve('accountInfo') ? this.localStorageService.retrieve('accountInfo').email : this.sessionStorageService.retrieve('accountInfo').email;
    finalAnswer.invitationId = this.invitationId;
    finalAnswer.email = email;
    finalAnswer.answers=this.answers;
    finalAnswer.surveyId = this.survey.id!;
    finalAnswer.finalScore = this.totalScore

     //console.log('TTTTTTT',finalAnswer)
    this._surveysService.saveAnswers(finalAnswer).subscribe(
      (response) => { },
      (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error!',
          text: 'An error occur !' ,
          customClass: {
            confirmButton: 'btn btn-primary'
          }
        })
      },
      () => {
        Swal.fire({
          icon: 'success',
          title: 'Completed!',
          text: 'your answers was sent successfully',
          customClass: {
            confirmButton: 'btn btn-success'
          }
        })
        this._router.navigate(['/home/survey-patient/list']);
      }
    )
    
  }

  public changeEvent(event: any) {
    if (event.target.checked) {
      this.checked = true
    } else {
      this.checked = false
    }
  }
}