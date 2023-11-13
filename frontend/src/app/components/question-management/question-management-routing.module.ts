import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddNewQuestionComponent } from './add-new-question/add-new-question.component';
import { ListQuestionComponent } from './list-question/list-question.component';
import { QuestionConfigComponent } from './question-config/question-config.component';
import { UpdateQuestionComponent } from './update-question/update-question.component';

const routes: Routes = [
  {
    path: 'list',
    component: ListQuestionComponent
  },
  {
    path: 'add',
    component: AddNewQuestionComponent
  },
  {
    path: 'configuration',
    component: QuestionConfigComponent
  },
  {
      path: 'update',
      component: UpdateQuestionComponent,
      data: {
        animation: 'QuestionsUpdateComponent'
      }
    },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QuestionManagementRoutingModule { }
