import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SurveyManagementRoutingModule } from './survey-management-routing.module';
import { AddNewSurveyComponent } from './add-new-survey/add-new-survey.component';
import { SurveyListComponent } from './survey-list/survey-list.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { NgxDatatableModule } from '@tusharghoshbd/ngx-datatable';
import { InviteUsersComponent } from './invite-users/invite-users.component';
import { TagInputModule } from 'ngx-chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MAT_MOMENT_DATE_FORMATS, MomentDateAdapter } from '@angular/material-moment-adapter';
import { NgxMatTimepickerModule } from 'ngx-mat-timepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { DetailsSurveyComponent } from './details-survey/details-survey.component';
import { ModalModule } from 'ngx-bootstrap/modal';

@NgModule({
  declarations: [
    AddNewSurveyComponent,
    SurveyListComponent,
    InviteUsersComponent,
    DetailsSurveyComponent,

  ],
  imports: [
    CommonModule,
    SurveyManagementRoutingModule,
    NgxPaginationModule,
    NgxDatatableModule,
    ReactiveFormsModule,
    FormsModule,
    DragDropModule,
    TagInputModule,
    MatDatepickerModule,
    MatIconModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    NgxMatTimepickerModule,
    ModalModule.forRoot(),

  ],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'us-US' },
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },
    { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS },
  ]
})
export class SurveyManagementModule { }
