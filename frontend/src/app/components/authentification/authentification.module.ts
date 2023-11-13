import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthentificationRoutingModule } from './authentification-routing.module';
import { MatCardModule } from "@angular/material/card";
import { CodeInputModule } from "angular-code-input";
import { ReactiveFormsModule } from "@angular/forms";
import { MatDialogModule } from "@angular/material/dialog";
import { TranslateModule } from "@ngx-translate/core";
import { MatIconModule } from "@angular/material/icon";
import { AuthGuard } from './guards/auth.guard';
import { TokenService } from './token/token.service';
import { AuthService } from './authService/auth.service';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { PasswordStrengthBarComponent } from './reset-password/password-strength-bar/password-strength-bar.component';


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    ResetPasswordComponent,
    PasswordStrengthBarComponent
  ],
  imports: [
    CommonModule,
    AuthentificationRoutingModule,
    MatCardModule,
    CodeInputModule,
    ReactiveFormsModule,
    MatDialogModule,
    TranslateModule,
    MatIconModule,
    
  ],
  providers: [
    AuthGuard,
    TokenService, AuthService
  ],
})
export class AuthentificationModule { }
