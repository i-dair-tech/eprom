import { Routes } from '@angular/router';
import { Authority } from '../_shared/config/authority.constants';
import { AuthGuard } from './authentification/guards/auth.guard';
import { LandingpageComponent } from './landingpage/landingpage.component';
import { LogsComponent } from './logs/logs.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { ProfileComponent } from './profile/profile.component';

export const content: Routes = [


    {
        path: 'study',
        loadChildren: () => import('./study-management/study-management.module').then(m => m.StudyManagementModule),
        canActivate: [AuthGuard],
        data: {
            authorities: [Authority.ADMIN,Authority.STUDY_COORDINATOR]
           },
    },
    {
        path: 'question',
        loadChildren: () => import('./question-management/question-management.module').then(m => m.QuestionManagementModule),
        canActivate: [AuthGuard],
        data: {
            authorities: [Authority.ADMIN,Authority.STUDY_COORDINATOR]
           },
    },
    {
        path: 'reports',
        loadChildren: () => import('./reports/reports.module').then(m => m.ReportsModule),
        canActivate: [AuthGuard],
        data: {
            authorities: [Authority.ADMIN,Authority.STUDY_COORDINATOR]
           },
     
    },
    {
        path: 'survey',
        loadChildren: () => import('./survey-management/survey-management.module').then(m => m.SurveyManagementModule),
        canActivate: [AuthGuard],
        data: {
            authorities: [Authority.ADMIN,Authority.STUDY_COORDINATOR]
           },
      
    },
    {
        path: 'user',
         canActivate: [AuthGuard],
         data: {
            authorities: [Authority.ADMIN],
           },
        loadChildren: () => import('./user-management/user-management.module').then(m => m.UserManagementModule)
    },
    {
        path: 'logs',
        component: LogsComponent,
        canActivate: [AuthGuard],
        data: {
            authorities: [Authority.ADMIN,Authority.STUDY_COORDINATOR]
           },
    },
    {
        path: 'survey-patient',
        loadChildren: () => import('./survey-patient/survey-patient.module').then(m => m.SurveyPatientModule),
        canActivate: [AuthGuard],
        data: {
            authorities: [Authority.PATIENT]
           }
           
    },
    
    {
        path: 'profile',
        component: ProfileComponent
    },

    {
        path: 'notification',
        component: NotificationsComponent,
        canActivate: [AuthGuard],
        data: {
            authorities: [Authority.ADMIN,Authority.STUDY_COORDINATOR]
           },
    },
    {
        path: 'landingPage',
        component: LandingpageComponent
    }
];