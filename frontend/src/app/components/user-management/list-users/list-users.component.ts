import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { DetailsUserComponent } from '../details-user/details-user.component';
import { UserDTO } from '../user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrls: ['./list-users.component.scss']
})
export class ListUsersComponent implements OnInit {

  private _subscription: Subscription[] = [];
  modalRef: BsModalRef | undefined;
  public users!: UserDTO[];
  public q: any;
  public searchValue: string = "";

  constructor(
    private _usersService: UserService,
    private _router: Router,
    private modalService: BsModalService
  ) { }

  ngOnInit(): void {

    this._getUser()
  }


  /**
  *  On destroy
  */
  ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }



  //Private Methods

  private _getUser() {

    this._subscription.push(
      this._usersService.getAllUser().subscribe((response) => {
        this.users = response;

      })
    )
  }






  //public methods

  onDelete(user: UserDTO) {
    Swal.fire({
      title: 'Es-tu sûr?',
      text: "you will delete this user definitively !",
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
        this._subscription.push(this._usersService.deleteUser(user.login!).subscribe(
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
              text: 'The user has been deleted successfully',
              customClass: {
                confirmButton: 'btn btn-success'
              }
            })
            this._getUser()
          }
        )
        );
      }
    })

  }



  public onUpdate(user: UserDTO) {

    this._router.navigate(['/home/user/update'], { queryParams: { login: user.login } })

  }


  public goToPage(pageName: string) {
    this._router.navigate([`${pageName}`]);
  }


  public showDetails(user:UserDTO){
    this.modalRef = this.modalService.show(DetailsUserComponent,  {
      initialState: {
        user: user,
      }
    });
    this.modalRef.setClass('modal-lg');
  }

}
