import { Component, NgZone, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Question } from '../_models/question.model';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-details-question',
  templateUrl: './details-question.component.html',
  styleUrls: ['./details-question.component.scss']
})
export class DetailsQuestionComponent implements OnInit {

  question : Question;
  constructor(
    public modalRef: BsModalRef,
    public dialog: MatDialog,
    public bsModalRef: BsModalRef, private zone: NgZone
    
    
  ) { }

  ngOnInit(): void {
  }

  close() {
    this.zone.run(() => {
      this.bsModalRef.hide();
    });
  }
}
