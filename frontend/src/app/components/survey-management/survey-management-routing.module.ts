import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddNewSurveyComponent } from './add-new-survey/add-new-survey.component';
import { InviteUsersComponent } from './invite-users/invite-users.component';
import { SurveyListComponent } from './survey-list/survey-list.component';
import { UpdateSurveyComponent } from './update-survey/update-survey.component';

const routes: Routes = [
  {
    path: 'list',
    component: SurveyListComponent
  },
  {
    path: 'add',
    component: AddNewSurveyComponent
  },
  {
    path: 'update',
    component: UpdateSurveyComponent
  },
  {
    path: 'invite',
    component: InviteUsersComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SurveyManagementRoutingModule { }
