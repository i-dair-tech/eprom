import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportsRoutingModule } from './reports-routing.module';
import { ReportsComponent } from './reports.component';
import * as echarts from 'echarts';
import { NgxEchartsModule } from 'ngx-echarts';
import { PerStudiesComponent } from './per-studies/per-studies.component';
import { PerSurveysComponent } from './per-surveys/per-surveys.component';
import { PerUsersComponent } from './per-users/per-users.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    ReportsComponent,
    PerStudiesComponent,
    PerSurveysComponent,
    PerUsersComponent
  ],
  imports: [
    FormsModule,
    CommonModule,
    ReportsRoutingModule,
    NgxDatatableModule,
    NgxEchartsModule.forRoot({
      echarts,
    }),
   

  ]
})
export class ReportsModule { }
