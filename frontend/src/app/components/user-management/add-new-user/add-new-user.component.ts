import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { LANGUAGES } from '../user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-add-new-user',
  templateUrl: './add-new-user.component.html',
  styleUrls: ['./add-new-user.component.scss']
})
export class AddNewUserComponent implements OnInit {

  public languages = LANGUAGES;
  public authorities: string[] = [];
  public selectedAuth: string[] = [];
  public editForm: FormGroup;
  public isSaving = false;
  public settings = {
    singleSelection: false,
    idField: 'item_id',
    textField: 'item_text',
    enableCheckAll: true,
    selectAllText: 'select all',
    unSelectAllText: 'unselect all',
    allowSearchFilter: true,
    limitSelection: -1,
    clearSearchFilter: true,
    maxHeight: 197,
    itemsShowLimit: 3,
    searchPlaceholderText: 'search here ',
    noDataAvailablePlaceholderText: 'no data available',
    closeDropDownOnSelection: false,
    showSelectedItemsAtTop: false,
    defaultOpen: false,
  }
  

  private _subscription: Subscription[] = [];

  
  constructor(
    public fb: FormBuilder,
    private _userService: UserService, 
    private route: ActivatedRoute,
    private _router: Router,
  ) { 

    this.editForm = this.fb.group({
      id: null,
      login: null,
      firstName: null,
      lastName: null,
      email: null,
      activated: null,
      langKey: null,
      authorities: null,
    });
  }

  ngOnInit(): void {

    this._userService.authorities().subscribe( (authorities: string[]) => (this.authorities = authorities));
  }

  get f() {
    return this.editForm.controls;
  }


  previousState(): void {
    window.history.back();
  }


  public save(): void {
  
    const user = this.editForm.getRawValue();
    user.authorities = this.selectedAuth;

    this._subscription.push(
      this._userService.addUser(user).subscribe(
        (response) => { },
        (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error : Form Invalid!',
            text: 'please complete all the fields !' ,
            customClass: {
              confirmButton: 'btn btn-primary'
            }
          })
        },
        () => {
          Swal.fire({
            icon: 'success',
            title: 'Added!',
            text: 'the question has been added successfully',
            customClass: {
              confirmButton: 'btn btn-success'
            }
          })
          this.editForm.reset();
          this._router.navigate(['/home/user/list']);
        }
      )
    )
  }



  public onItemSelect(item: any) {
    this.selectedAuth.push(item)
  }
  public onDeSelect(item: any) {
    this.selectedAuth = this.selectedAuth.filter(e=> e !== item)
  }

  public onSelectAll(items: any) {
    this.selectedAuth = items
  }
  public onDeSelectAll(items: any) {
    this.selectedAuth = items
  }

}
