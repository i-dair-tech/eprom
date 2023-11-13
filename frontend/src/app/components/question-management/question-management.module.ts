import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuestionManagementRoutingModule } from './question-management-routing.module';
import { ListQuestionComponent } from './list-question/list-question.component';
import { AddNewQuestionComponent } from './add-new-question/add-new-question.component';
import { QuestionConfigComponent } from './question-config/question-config.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule } from 'ngx-bootstrap/modal';
import { FilterPipe } from 'src/app/_shared/pipe/filter.pipe';





@NgModule({
  declarations: [
    ListQuestionComponent,
    AddNewQuestionComponent,
    QuestionConfigComponent,


    //pipes
    FilterPipe
 
  ],
  imports: [
    CommonModule,
    QuestionManagementRoutingModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    FormsModule,
    ModalModule.forRoot(),

    
  ],
  exports: [
    FilterPipe
  ]
})
export class QuestionManagementModule { }
