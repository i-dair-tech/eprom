import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ReportService } from './reports.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {
  private _subscription: Subscription[] = [];


  number_question = 0;
  number_users = 0;
  number_studies = 0;
  number_surveys = 0;
  number_answer = 0;
  number_invitation = 0;
  number_invitation_sent = 0;
  number_invitation_no_answer = 0;

  option: any

  constructor(private _reportService : ReportService) { }

  ngOnInit(): void {

  
      this.getCount()
   
  }



  /**
  *  On destroy
  */
   ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }


  getCount(){

    this._subscription.push(
     this._reportService.getQuestionsNumber().subscribe((response) => {
      this.number_question = response;
    })
    )

    this._subscription.push(
    this._reportService.getSurveysNumber().subscribe((response) => {
      this.number_surveys = response;
    })
    )

    this._subscription.push(
    this._reportService.getStudiesNumber().subscribe((response) => {
      this.number_studies = response;
    })
    )

    this._subscription.push(
    this._reportService.getUsersNumber().subscribe((response) => {
      this.number_users = response;
    })
    )

    this._subscription.push(
    this._reportService.getAnswersNumber().subscribe((response) => {
      this.number_answer = response;
    })
    )

    this._subscription.push(
    this._reportService.getInvitationsNumber().subscribe((response) => {
      this.number_invitation = response;
    })
    )
   
  }

}
