import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddNewStudyComponent } from './add-new-study/add-new-study.component';
import { StudyListComponent } from './study-list/study-list.component';
import { UpdateStudyComponent } from './update-study/update-study.component';

const routes: Routes = [
  {
    path: 'list',
    component: StudyListComponent
  },
  {
    path: 'add',
    component: AddNewStudyComponent
  },
  {
    path: 'update',
    component: UpdateStudyComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StudyManagementRoutingModule { }
