import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddNewUserComponent } from './add-new-user/add-new-user.component';
import { ListUsersComponent } from './list-users/list-users.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { MailConfigComponent } from './mail-config/mail-config.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListUsersComponent
  },
  {
    path: 'add',
    component: AddNewUserComponent
  },
  {
    path: 'update',
    component: UpdateUserComponent
  }
  ,
  {
    path: 'config',
    component: MailConfigComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserManagementRoutingModule { }
