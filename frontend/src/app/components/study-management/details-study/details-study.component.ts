import { Component, OnInit } from '@angular/core';
import { Study } from '../study.model';
import { BsModalRef } from 'ngx-bootstrap/modal';


@Component({
  selector: 'app-details-study',
  templateUrl: './details-study.component.html',
  styleUrls: ['./details-study.component.scss']
})
export class DetailsStudyComponent implements OnInit {

  study:Study;
  constructor(
    public modalRef: BsModalRef
  ) { }

  ngOnInit(): void {
  }

}
