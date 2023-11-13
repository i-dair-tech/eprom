import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

import { Survey } from '../_models/survey.model';
import { SurveyService } from '../survey.service';
import { DetailsSurveyComponent } from '../details-survey/details-survey.component';

@Component({
  selector: 'app-survey-list',
  templateUrl: './survey-list.component.html',
  styleUrls: ['./survey-list.component.scss']
})
export class SurveyListComponent implements OnInit {

  private _subscription: Subscription[] = [];
  modalRef: BsModalRef | undefined;

  public surveys!: Survey[];
  public q: any;
  public searchValue: string = "";

  
  constructor(
    private _surveysService: SurveyService,
    private _router: Router,
    private modalService: BsModalService

  ) {
   
   }

  ngOnInit(): void {

    this._getSurveys()
    
  }


   /**
 *  On destroy
 */
    ngOnDestroy(): void {
      this._subscription.forEach(subscription => subscription.unsubscribe());
    }



    //Private Methods

    private _getSurveys() {
      
        this._subscription.push(
          this._surveysService.getAllSurveys().subscribe((response) => {

          this.surveys = response;
         
        })
        )
      }

      public onDelete(survey : Survey) {
        Swal.fire({
          title: 'Are You sure?',
          text: "you will delete this survey definitively !",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#7367F0',
          cancelButtonColor: '#E42728',
          confirmButtonText: 'yes, Delete it!',
          customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-danger ml-1'
          }
        }).then((result) => {
          if (result.value) {
            this._subscription.push(this._surveysService.deleteSurvey(survey.id!).subscribe(
              (response) => { },
              (err) => {
                Swal.fire({
                  icon: 'error',
                  title: 'ERROR!',
                  text: err.error.message,
                  customClass: {
                    confirmButton: 'btn btn-primary'
                  }
                })
              },
              () => {
                Swal.fire({
                  icon: 'success',
                  title: 'Deleted!',
                  text: 'The survey has been deleted successfully',
                  customClass: {
                    confirmButton: 'btn btn-success'
                  }
                })
                this._getSurveys()
              }
            )
            );
          }
        })
  
      }

      

      public onUpdate(survey : Survey) {
        
        this._router.navigate(['/home/survey/update'], { queryParams: { id: survey.id } })
  
      }

      public goToPage(pageName: string) {
        this._router.navigate([`${pageName}`]);
      }

      public invite(survey:Survey){
        this._router.navigate(['/home/survey/invite'], { queryParams: { id: survey.id } })
      }

      public showDetails(survey:Survey){

        this.modalRef = this.modalService.show(DetailsSurveyComponent,  {
          initialState: {
            survey: survey,
          }
        });
        this.modalRef.setClass('modal-lg');
      }

}
