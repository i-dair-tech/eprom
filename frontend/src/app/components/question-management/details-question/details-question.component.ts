import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Question } from '../_models/question.model';

@Component({
  selector: 'app-details-question',
  templateUrl: './details-question.component.html',
  styleUrls: ['./details-question.component.scss']
})
export class DetailsQuestionComponent implements OnInit {

  question : Question;
  constructor(
    public modalRef: BsModalRef
  ) { }

  ngOnInit(): void {
  }

}
