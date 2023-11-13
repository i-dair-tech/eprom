import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Survey } from '../../survey-management/_models/survey.model';
import { SurveyPatientService } from '../survey-patient.service';

@Component({
  selector: 'app-surveys-selected-details',
  templateUrl: './surveys-selected-details.component.html',
  styleUrls: ['./surveys-selected-details.component.scss']
})
export class SurveysSelectedDetailsComponent implements OnInit {

  public survey: Survey;
  public invitationId: number;
  public checked = false;


  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private _surveysService: SurveyPatientService,
  ) { }

  ngOnInit(): void {

    this._route.queryParamMap.subscribe(
      params => {

        let id = parseInt(params.get("id")!);
        let invitationId = parseInt(params.get("invitationId")!);
        this.invitationId = invitationId
        this._surveysService.getSurveyById(id).subscribe(data => {
          this.survey = data;

        })
      }
    )

  }

  public goAhead(survey: any) {
    this._router.navigate(['/home/survey-patient/questions'], { queryParams: { id: survey.id, invitationId: this.invitationId } })
  }

  public changeEvent(event: any) {
    if (event.target.checked) {
      this.checked = true
    } else {
      this.checked = false
    }
  }
}