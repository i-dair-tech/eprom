import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { Survey } from '../../survey-management/_models/survey.model';
import { SurveyService } from '../../survey-management/survey.service';
import { Study } from '../study.model';
import { StudyService } from '../study.service';

@Component({
  selector: 'app-add-new-study',
  templateUrl: './add-new-study.component.html',
  styleUrls: ['./add-new-study.component.scss']
})
export class AddNewStudyComponent implements OnInit {

  public rows: Survey[];
  public selected: Survey[] = [];

  private _subscription: Subscription[] = [];

  constructor(
    private _surveysService: SurveyService,
    private _studyService: StudyService,
    private _router: Router,
  ) { }

  ngOnInit(): void {
    this._subscription.push(
      this._surveysService.getAllSurveys().subscribe((response) => {
      
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

  drop(event: CdkDragDrop<Survey[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
    }
  }

  study:Study = new Study();
  public save(form :NgForm){
    if(this.selected.length ==0 || form.value.title =='' || form.value.title === undefined){

      Swal.fire({
        icon: 'error',
        title: 'Error : Form Invalid!',
        text: 'please complete all the fields !' ,
        customClass: {
          confirmButton: 'btn btn-primary'
        }
      })
    }else{

    this.study.surveys = this.selected;

    this._subscription.push(
      this._studyService.addStudy(this.study).subscribe(
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
            text: 'the study has been added successfully',
            customClass: {
              confirmButton: 'btn btn-success'
            }
          })
          form.reset();
          this._router.navigate(['/home/study/list']);
        }
      )
    )
    }
 }

  public resetFormWithDefaultValues() {
    this.study.title = "";
    this.selected = []
    this.ngOnInit()
  }
}