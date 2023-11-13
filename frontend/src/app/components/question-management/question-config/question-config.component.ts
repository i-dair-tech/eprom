import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { TypeQuestion } from '../_models/questions-type.model';
import { QuestionsTypeService } from './questions-type.service';

@Component({
  selector: 'app-question-config',
  templateUrl: './question-config.component.html',
  styleUrls: ['./question-config.component.scss']
})
export class QuestionConfigComponent implements OnInit {

  private _subscription: Subscription[] = [];
  
 public questionsTypes!: TypeQuestion[];
 public questionsType!: TypeQuestion;


  constructor(
    public questionsTypeService: QuestionsTypeService,
  ) { }

   /**
  *  On init
  */

  ngOnInit(): void {

    this._getQuestionType()
    this._resetForm()
  }


  /**
  *  On destroy
  */
   ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }


   




  // public Methods

  onDelete(questionType : TypeQuestion) {
    if (confirm('Are you sure to delete this record ?')) {
      this.questionsTypeService.deleteQuestionsType(questionType.id).subscribe(res => { 
          this._getQuestionType();
          this._resetForm();
        },

          err => {
            console.log(err);
            
          }
        )

    }
  }


  populateForm(questionsType: TypeQuestion) {
    this.questionsTypeService.formData = Object.assign({}, questionsType);
  }


  onSubmit(form: NgForm) {
    
    if ( this.questionsTypeService.formData.id == 0 ){

      this._insertRecord(form);
    }
    else{
      this._updateRecord(form);
    }  
     
  }




  // Private Methods

  private _getQuestionType() {
      
    this._subscription.push(
      this.questionsTypeService.getQuestionTypesAll().subscribe((response) => {
      this.questionsTypes = response;
     
      })
    )
  }

  // Edit

  private _updateRecord(form: NgForm) {
  this.questionsTypeService.updateQuestionsType().subscribe(res => {
      this._resetForm(form); 
      this._getQuestionType();
    },
    err => {
      console.log(err);

    }
  )
}


// Insert
  private _insertRecord(form: NgForm) {
    this.questionsTypeService.addQuestionType(form.value).subscribe(
      res => {
        this._resetForm(form);
        this._getQuestionType();
      },
      err => {
        console.log(err);
        
      }
    )
  }



  private _resetForm(form?: NgForm) {

    if (form != null)
      form.resetForm();
    this.questionsTypeService.formData = {
     
      id: 0,
      text: '',
      createdBy: '',
      createdDate: undefined,
      lastModifiedBy: '',
      lastModifiedDate: undefined,
      isArchived: undefined,
      archivedDate: undefined,
    }
  }

}
