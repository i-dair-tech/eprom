import { Component, OnInit } from '@angular/core';

import { Survey } from '../_models/survey.model';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-details-survey',
  templateUrl: './details-survey.component.html',
  styleUrls: ['./details-survey.component.scss']
})
export class DetailsSurveyComponent implements OnInit {

  survey:Survey;
  constructor(
    public modalRef: BsModalRef
  ) { }

  ngOnInit(): void {
  }

}
