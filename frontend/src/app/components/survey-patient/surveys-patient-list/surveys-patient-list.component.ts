import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { Subscription } from 'rxjs';
import { User } from '../../user-management/user.model';
import { SurveyPatientService } from '../survey-patient.service';
import { SurveyPatient } from '../_models/survey-patient.model';

@Component({
  selector: 'app-surveys-patient-list',
  templateUrl: './surveys-patient-list.component.html',
  styleUrls: ['./surveys-patient-list.component.scss'],
})
export class SurveysPatientListComponent implements OnInit {

  todayDate : Date = new Date();
  todayString : string = new Date().toDateString();
  todayISOString : string = new Date().toISOString();
  


  private _subscription: Subscription[] = [];

  public surveys!: SurveyPatient[];
  public surveysStatus: any[];
  public userInfo: User;

  constructor(private _surveyPatientService: SurveyPatientService,
    private localStorageService: LocalStorageService,
    private sessionStorageService: SessionStorageService,
    private _router: Router,
  ) { }

  ngOnInit(): void {
    this._getPatientSurveys()
  }


  /**
 *  On destroy
 */
  ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }

  _getPatientSurveys() {

    this.userInfo = this.localStorageService.retrieve('accountInfo') ? this.localStorageService.retrieve('accountInfo') : this.sessionStorageService.retrieve('accountInfo');

    this._subscription.push(

      this._surveyPatientService.getAllSurveysByPatient(this.userInfo.email!).subscribe((response) => {
       // let temp = response
        // .sort((item1, item2) => {
        //   return (item1['id']! > item2['id']!) ? 1 : -1;
        // });
         //console.log(response)
        this._surveyPatientService.getSurveyStatus(this.userInfo.email!).subscribe((response) => {

          console.log('rrr',response)
          setTimeout(() => {
            this.surveysStatus = response
            .sort((item1, item2) => {
              return (item1['id']! > item2['id']!) ? 1 : -1;
            })
            .map(el => ({ id: el.id, status: el.status }))

            let temp = response.map(el => el['survey'])
            console.log('temp',temp)

            for (let index = 0; index < temp.length; index++) {

              const elem = temp[index];
              elem["status"] = this.surveysStatus[index]["status"];
              elem["invitationId"] = this.surveysStatus[index]["id"];
            }

            this.surveys = temp;

          
          }, 1500);
        })

      })
    )

  }


  public choice(survey: SurveyPatient) {

    this._router.navigate(['/home/survey-patient/details'], { queryParams: { id: survey.id, invitationId: survey.invitationId } })

  }

}
