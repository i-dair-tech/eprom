import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ColumnMode, SelectionType } from '@swimlane/ngx-datatable';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { QuestionService } from '../../question-management/question.service';
import { Question } from '../../question-management/_models/question.model';
import { Survey } from '../_models/survey.model';
import { SurveyService } from '../survey.service';


@Component({
  selector: 'app-add-new-survey',
  templateUrl: './add-new-survey.component.html',
  styleUrls: ['./add-new-survey.component.scss']
})
export class AddNewSurveyComponent implements OnInit {

  

  @ViewChild('tableRowDetails') tableRowDetails: any;
  ColumnMode = ColumnMode;
  SelectionType = SelectionType;
  public chkBoxSelected :Question[] = [];
  public barwidth = 50 ;
  public show = false ;
  public nextshow = true ;
  public pervshow = false ;
  public saveshow = false ;
  public pageNum= 1;
  public rows: Question[];
  options = {checkboxes:true}




  public columns = [
    // { key: 'id', title: "ID",  width: 30, sorting: true },
    { key: 'text', title: 'Text', width: 500},
    { key: 'language', title: 'Language',  align: { head: 'center' }, width: 100, sorting: true, noWrap: { head: true, body: true } },
    { key: 'createdDate', title: 'Creation Date', width:100, sorting: false, pinned: false },
   
]

  //private
  private orderedQuestion : Question[] = [];
  private _subscription: Subscription[] = [];

  constructor(
    private readonly _formBuilder: FormBuilder,
    private _questionsService : QuestionService,
    private _surveysService: SurveyService,
    private _router: Router,
   
  ) {

   
  
  }

  
  ngOnInit() {
  
    this._subscription.push(
    this._questionsService.getAllQuestion().subscribe((response) => {
    
    this.rows = response;
   })
    )
  }

 /**
  *  On destroy
  */
  ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }


  public next(){
      this.show=true;
      this.pageNum=2;
      this.barwidth+=50;
      this.saveshow = true;
      this.nextshow = false ;
      this.pervshow = true;

  }

  public previous(){
  
    this.show=false;
    this.pageNum=1;
    this.barwidth-=50;
    this.saveshow = false;
    this.nextshow = true ;
    this.pervshow = false;
  }


  survey:Survey = new Survey();
  public save(form :NgForm){

    this.survey.questions = this.orderedQuestion;

    this._subscription.push(
      this._surveysService.addSurvey(this.survey).subscribe(
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
            text: 'the survey has been added successfully',
            customClass: {
              confirmButton: 'btn btn-success'
            }
          })
          form.reset();
          this._router.navigate(['/home/survey/list']);
        }
      )
    )
    
  }


  public  drop(event: CdkDragDrop<Question[]>) {

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
    }

    this.orderedQuestion = event.container.data
  }


  onCheckboxClick(event : any){

    this.chkBoxSelected = event;
    this.orderedQuestion  = event;
  }


  remove(item:any){

    this.chkBoxSelected = this.orderedQuestion.filter(function(el) { return el.id != item.id; })

    console.log(this.orderedQuestion)

  }

}




