import { Component, OnInit } from '@angular/core';
import { UserDTO } from '../user.model';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-details-user',
  templateUrl: './details-user.component.html',
  styleUrls: ['./details-user.component.scss']
})
export class DetailsUserComponent implements OnInit {
  user : UserDTO;

  constructor(
    public modalRef: BsModalRef
  ) { }

  ngOnInit(): void {
  }
  close() {
    
    this.modalRef.hide();
}

}
