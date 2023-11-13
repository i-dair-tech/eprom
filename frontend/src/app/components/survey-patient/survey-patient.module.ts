import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SurveyPatientRoutingModule } from './survey-patient-routing.module';
import { SurveysSelectedDetailsComponent } from './surveys-selected-details/surveys-selected-details.component';
import { SurveysSelectedQuestionComponent } from './surveys-selected-question/surveys-selected-question.component';
import { NgxStarRatingModule } from 'ngx-star-rating';







@NgModule({
  declarations: [
  
    SurveysSelectedDetailsComponent,
    SurveysSelectedQuestionComponent
  ],
  imports: [
    CommonModule,
    SurveyPatientRoutingModule,
    NgxPaginationModule,
    NgxDatatableModule,
    ReactiveFormsModule,
    FormsModule,
    NgxStarRatingModule


  ]
})
export class SurveyPatientModule { }
