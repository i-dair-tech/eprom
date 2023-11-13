import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { LANGUAGES, User } from '../user.model';
import { UserService } from '../user.service';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.scss']
})
export class UpdateUserComponent implements OnInit {

  @ViewChild('multiSelect') multiSelect: any;
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
    allowSearchFilter: false,
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
    private _route: ActivatedRoute,
    private _router: Router,
    
  ) { 

    
  }

  ngOnInit(): void {

   

    this._route.queryParamMap.subscribe(
      params => {
        
        let login  = String(params.get("login")!);

        this._userService.getUserById(login).subscribe(data => {

           data;

          if(data !== undefined ){

           let user : User = data          
           
           this.authorities = user.authorities!
           this.editForm.reset(user);
           
                  
          }

        })
      }
    )

    this._userService.authorities().subscribe( (authorities: string[]) => (this.authorities = authorities));
    this.setForm();
  }


  public setForm() {
    this.editForm = new FormGroup({
      id: new FormControl(null, Validators.required),
      login: new FormControl(null, Validators.required),
      firstName: new FormControl(null, Validators.required),
      lastName: new FormControl(null, Validators.required),
      email: new FormControl(null, Validators.required),
      activated: new FormControl(false, Validators.required),
      langKey: new FormControl(null, Validators.required),
      authorities: new FormControl(this.authorities, Validators.required),
    });

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
      this._userService.updateUser(user).subscribe(
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
            title: 'Updated!',
            text: 'the user has been updated successfully',
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
