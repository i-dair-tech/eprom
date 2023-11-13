import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserManagementRoutingModule } from './user-management-routing.module';
import { ListUsersComponent } from './list-users/list-users.component';
import { AddNewUserComponent } from './add-new-user/add-new-user.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule } from 'ngx-bootstrap/modal';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { UpdateUserComponent } from './update-user/update-user.component';
import { DetailsUserComponent } from './details-user/details-user.component';


@NgModule({
  declarations: [
    ListUsersComponent,
    AddNewUserComponent,
    UpdateUserComponent,
    DetailsUserComponent
  ],
  imports: [
    CommonModule,
    UserManagementRoutingModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    FormsModule,
    ModalModule.forRoot(),
    NgMultiSelectDropDownModule.forRoot(),
  ]
})
export class UserManagementModule { }
