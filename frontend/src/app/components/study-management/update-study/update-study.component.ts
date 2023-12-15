import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { Survey } from '../../survey-management/_models/survey.model';
import { SurveyService } from '../../survey-management/survey.service';
import { Study } from '../study.model';
import { StudyService } from '../study.service';

@Component({
  selector: 'app-update-study',
  templateUrl: './update-study.component.html',
  styleUrls: ['./update-study.component.scss']
})
export class UpdateStudyComponent implements OnInit {
  
  public rows: Survey[];
  public selected: Survey[] = [];
  public form: FormGroup;

  private _subscription: Subscription[] = [];

  constructor(
    public fb: FormBuilder,
    private _studyService : StudyService,
    private _surveysService: SurveyService,
    private _router: Router,
    private _route: ActivatedRoute,
   
  ) {
    this.form = this.fb.group({
      id: null,
      title: null,
      createdBy: null,
      createdDate: null,
      lastModifiedBy: null,
      lastModifiedDate: null,
      isArchived: null,
      archivedDate: null,
      surveys: this.fb.array([]),

      })

    } 

  ngOnInit(): void {

     this._getSurveys();

    this._route.queryParamMap.subscribe(
      params => {
        
        let id  = parseInt(params.get("id")!);

        this._studyService.getStudyById(id).subscribe(data => {
           data;

          if(data !== undefined ){

           let study : Study = data          

            this.form.patchValue(study);
            this.selected = study.surveys!;

            const ids = study.surveys?.map(e => e.id);
            console.log(ids)
            console.log(this.rows)
            this.rows = this.rows.filter(e => ! ids!.includes(e.id));
             
            console.log(this.rows)
                  
          }

        })
      }
    )
     
    }



    private _getSurveys(){


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
  public save(){

    this.study =  this.form.value
    this.study.surveys = this.selected;

    this._subscription.push(
      this._studyService.updateStudy(this.study).subscribe(
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
            text: 'the study has been updated successfully',
            customClass: {
              confirmButton: 'btn btn-success'
            }
          })
          this.form.reset();
          this._router.navigate(['/home/study/list']);
        }
      )
    )
    
  }

  public goToPage(pageName: string) {
    this._router.navigate([`${pageName}`]);
  }
}