import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudyManagementRoutingModule } from './study-management-routing.module';
import { StudyListComponent } from './study-list/study-list.component';
import { AddNewStudyComponent } from './add-new-study/add-new-study.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { DetailsStudyComponent } from './details-study/details-study.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import {PanelModule} from 'primeng/panel';
import { MenuModule } from 'primeng/menu';


@NgModule({
  declarations: [
    StudyListComponent,
    AddNewStudyComponent,
    DetailsStudyComponent,
    
  ],
  imports: [
    CommonModule,
    StudyManagementRoutingModule,
    NgxPaginationModule,
    NgxDatatableModule,
    ReactiveFormsModule,
    FormsModule,
    DragDropModule,
    ModalModule.forRoot(),
    ButtonModule,
    TableModule,
    OverlayPanelModule,
    PanelModule,
    MenuModule

  ]
})
export class StudyManagementModule { }
