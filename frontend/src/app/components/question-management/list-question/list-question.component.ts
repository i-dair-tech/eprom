import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

import { QuestionService } from '../question.service';
import { Question } from '../_models/question.model';
import { DetailsQuestionComponent } from '../details-question/details-question.component';

@Component({
  selector: 'app-list-question',
  templateUrl: './list-question.component.html',
  styleUrls: ['./list-question.component.scss']
})
export class ListQuestionComponent implements OnInit, OnDestroy {

  private _subscription: Subscription[] = [];
  modalRef: BsModalRef | undefined;
  public questions!: Question[];
  public q: any;
  public searchValue: string = "";

  constructor(
    private _questionsService: QuestionService,
    private _router: Router,
    private modalService: BsModalService
  ) { }

  ngOnInit(): void {

    this._getQuestion()
  }


  /**
  *  On destroy
  */
  ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }



  //Private Methods

  private _getQuestion() {

    this._subscription.push(
      this._questionsService.getAllQuestion().subscribe((response) => {
        this.questions = response;
        console.log('XXXXXXXXXXX',this.questions)

      })
    )
  }






  //public methods

  onDelete(question: Question) {
    Swal.fire({
      title: 'Es-tu sûr?',
      text: "you will delete this question definitively !",
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
        this._subscription.push(this._questionsService.deleteQuestion(question.id!).subscribe(
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
              text: 'The question has been deleted successfully',
              customClass: {
                confirmButton: 'btn btn-success'
              }
            })
            this._getQuestion()
          }
        )
        );
      }
    })

  }



  public onUpdate(question: Question) {

    this._router.navigate(['/home/question/update'], { queryParams: { id: question.id } })

  }


  public goToPage(pageName: string) {
    this._router.navigate([`${pageName}`]);
  }


  public showDetails(question:Question){

    this.modalRef = this.modalService.show(DetailsQuestionComponent,  {
      initialState: {
        question: question,
      }
    });
    this.modalRef.setClass('modal-lg');
  }

}
