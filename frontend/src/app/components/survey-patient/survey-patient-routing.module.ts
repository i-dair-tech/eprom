import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SurveysPatientListComponent } from './surveys-patient-list/surveys-patient-list.component';
import { SurveysSelectedDetailsComponent } from './surveys-selected-details/surveys-selected-details.component';
import { SurveysSelectedQuestionComponent } from './surveys-selected-question/surveys-selected-question.component';


const routes: Routes = [
  {
    path: 'list',
    component: SurveysPatientListComponent
  },
  { path: 'details', 
    component: SurveysSelectedDetailsComponent,
  },
  { path: 'questions', 
    component: SurveysSelectedQuestionComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SurveyPatientRoutingModule { }
