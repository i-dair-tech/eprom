import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PerStudiesComponent } from './per-studies/per-studies.component';
import { PerSurveysComponent } from './per-surveys/per-surveys.component';
import { PerUsersComponent } from './per-users/per-users.component';
import { ReportsComponent } from './reports.component';

const routes: Routes = [
  {
    path: '',
    component: ReportsComponent
  },
  {
    path: 'per-study',
    component: PerStudiesComponent
  },
  {
    path: 'per-survey',
    component: PerSurveysComponent
  },
  {
    path: 'per-user',
    component: PerUsersComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }
