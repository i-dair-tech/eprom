import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatStepperModule } from '@angular/material/stepper';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { RecaptchaV3Module } from 'ng-recaptcha';
import { MaterialExampleModule } from './material.module';
import { MatSliderModule } from '@angular/material/slider';
import { CodeInputModule } from 'angular-code-input';
import { CommonModule, HashLocationStrategy, LocationStrategy, PathLocationStrategy } from "@angular/common";
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { ImageCropperModule } from "ngx-image-cropper";
import { HomeComponent } from './_shared/home/home.component';
import { AuthService } from './components/authentification/authService/auth.service';
import { TokenService } from './components/authentification/token/token.service';
import { AuthGuard } from './components/authentification/guards/auth.guard';

import { NgxWebstorageModule } from 'ngx-webstorage';

import { SidebarComponent } from './_shared/sidebar/sidebar.component';
import { NgxDatatableModule } from '@tusharghoshbd/ngx-datatable';
import { QuestionService } from './components/question-management/question.service';
import { StudyService } from './components/study-management/study.service';
import { SurveyService } from './components/survey-management/survey.service';
import { AuthInterceptor } from './components/authentification/interceptors/auth.interceptor';
import { AuthExpiredInterceptor } from './components/authentification/interceptors/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './components/authentification/interceptors/errorhandler.interceptor';
import { NotificationInterceptor } from './components/authentification/interceptors/notification.interceptor';
import { NgxPaginationModule } from 'ngx-pagination';
import { LogsComponent } from './components/logs/logs.component';
import { LogsService } from './components/logs/logs.service';
import { QuestionsTypeService } from './components/question-management/question-config/questions-type.service';
import { UpdateQuestionComponent } from './components/question-management/update-question/update-question.component';
import { DetailsQuestionComponent } from './components/question-management/details-question/details-question.component';
import { UpdateSurveyComponent } from './components/survey-management/update-survey/update-survey.component';
import { UpdateStudyComponent } from './components/study-management/update-study/update-study.component';
import { UserService } from './components/user-management/user.service';
import { ProfileComponent } from './components/profile/profile.component';
import { PasswordResetFinishService } from './components/authentification/reset-password/reset-password.service';
import { TagInputModule } from 'ngx-chips';
import { SurveysPatientListComponent } from './components/survey-patient/surveys-patient-list/surveys-patient-list.component';
import { SurveyPatientService } from './components/survey-patient/survey-patient.service';
import { MiniFooterComponent } from './_shared/mini-footer/mini-footer.component';
import { MiniHeaderComponent } from './_shared/mini-header/mini-header.component';
import { ReportService } from './components/reports/reports.service';
import * as echarts from 'echarts';
import { NgxEchartsModule } from 'ngx-echarts';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { LandingpageComponent } from './components/landingpage/landingpage.component'
import {InputTextModule} from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import {PanelModule} from 'primeng/panel';
import { MenuModule } from 'primeng/menu';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent, 
    HomeComponent,
    SidebarComponent,
    LogsComponent,
    UpdateQuestionComponent,
    DetailsQuestionComponent,    
    UpdateSurveyComponent,
    UpdateStudyComponent,
    ProfileComponent,
    SurveysPatientListComponent,
    MiniFooterComponent,
    MiniHeaderComponent,
    NotificationsComponent,
    LandingpageComponent,
    ],
  imports: [
    CommonModule,
    MatSliderModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserModule,
    NgxPaginationModule,
    AppRoutingModule,
    MatStepperModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MaterialExampleModule,
    HttpClientModule,
    RecaptchaV3Module,
    CodeInputModule,
    ImageCropperModule,
    TagInputModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient],
      },
    }),
    ToastrModule.forRoot({
      positionClass: 'toast-top-center'
    }),
    NgxWebstorageModule.forRoot({separator: '-', caseSensitive: true }),
    NgxDatatableModule,
    NgxEchartsModule.forRoot({
      echarts,
    }),
    ModalModule.forRoot(),
    
    ToastrModule.forRoot({
      closeButton: true,
      timeOut: 15000, // 15 seconds
      progressBar: true,
    }),
    InputTextModule,
    OverlayPanelModule,
		TableModule,
    ButtonModule,
    PanelModule,
    MenuModule

  ],

  providers: [
    AuthGuard,
    TokenService,
    AuthService,
    QuestionService,
    StudyService,
    SurveyService,
    LogsService,
    QuestionsTypeService,
    UserService,
    PasswordResetFinishService,
    SurveyPatientService,
    ReportService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthExpiredInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NotificationInterceptor,
      multi: true,
    },
    { provide: LocationStrategy, useClass: HashLocationStrategy }

   ],
  bootstrap: [AppComponent]
})
export class AppModule { }
