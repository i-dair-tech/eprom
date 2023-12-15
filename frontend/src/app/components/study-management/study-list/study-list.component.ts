import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { Study } from '../study.model';
import { StudyService } from '../study.service';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { DetailsStudyComponent } from '../details-study/details-study.component';


@Component({
  selector: 'app-study-list',
  templateUrl: './study-list.component.html',
  styleUrls: ['./study-list.component.scss']
})
export class StudyListComponent implements OnInit {

  private _subscription: Subscription[] = [];
  modalRef: BsModalRef | undefined;

  public studies!: Study[];
  public q: any;
  public searchValue: string = "";

  
  constructor(
    private _studyService: StudyService,
    private _router: Router,
    private modalService: BsModalService

  ) {
   
   }

  ngOnInit(): void {

    this._getStudies()
    
  }


   /**
 *  On destroy
 */
    ngOnDestroy(): void {
      this._subscription.forEach(subscription => subscription.unsubscribe());
    }



    //Private Methods

    private _getStudies() {
      
        this._subscription.push(
          this._studyService.getAllStudies().subscribe((response) => {

          this.studies = response;
         
        })
        )
      }

      public onDelete(study : Study) {
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
            this._subscription.push(this._studyService.deleteStudy(study.id!).subscribe(
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
                this._getStudies()
              }
            )
            );
          }
        })
  
      }

      

      public onUpdate(study : Study) {
        
        this._router.navigate(['/home/study/update'], { queryParams: { id: study.id } })
  
      }


      public goToPage(pageName: string) {
        this._router.navigate([`${pageName}`]);
      }

      public showDetails(study:Study){

        this.modalRef = this.modalService.show(DetailsStudyComponent,  {
          initialState: {
            study:study
          }
        });        
        this.modalRef.setClass('modal-lg');
      }

}
