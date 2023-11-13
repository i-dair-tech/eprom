import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './components/authentification/guards/auth.guard';
import { content } from './components/content-routes';
import { errorRoute } from './_shared/error/error.route';
import { HomeComponent } from './_shared/home/home.component';

const routes: Routes = [
    
    { path: '', redirectTo: '/home/landingPage', pathMatch: 'full' },
    {
        path: 'home' ,
        component: HomeComponent, canActivate: [AuthGuard],
        children: content
           
    },
    {
        path: 'auth',
        loadChildren: () => import('./components/authentification/authentification.module').then(m => m.AuthentificationModule)
    },
   
    ...errorRoute,
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true,enableTracing: false, scrollPositionRestoration: 'enabled' })],
    exports: [RouterModule]
})
export class AppRoutingModule { }
