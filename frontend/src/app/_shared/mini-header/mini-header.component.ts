import { Component, OnInit } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { NotificationsComponent } from 'src/app/components/notifications/notifications.component';

@Component({
  selector: 'app-mini-header',
  templateUrl: './mini-header.component.html',
  styleUrls: ['./mini-header.component.scss']
})
export class MiniHeaderComponent implements OnInit {

  modalRef: BsModalRef | undefined;

  constructor(
    private modalService: BsModalService
  ) { }

  ngOnInit(): void {
  }



  listNotifications(){

    this.modalRef = this.modalService.show(NotificationsComponent,  {
      initialState: {
        notification: 'yes',
      }
    });
    this.modalRef.setClass('modal-lg');

  }



}
