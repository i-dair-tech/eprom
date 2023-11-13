import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CheckRequiredField } from 'src/app/_shared/form.helper';
import { AuthService } from '../authService/auth.service';
import { TokenService } from '../token/token.service';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  returnUrl!: string;
  processing: Boolean = false;
  error: Boolean = false;
  checkField = CheckRequiredField;
  authenticationError = false;


  constructor(
    private authService: AuthService,
    private router: Router,
    private token: TokenService,
    private route: ActivatedRoute,
    private loginService: LoginService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.initForm();
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    if (this.token.get()) {
      this.router.navigateByUrl('/home/landingPage');
    } else {
      this.initForm();
    }
  }

  private initForm() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', Validators.required),
    });
  }

  onSubmitButtonClicked() {
    this.error = false;
    this.processing = false;
    if (this.loginForm.valid) {
      this
      this.login();
    }
  }

  private login() {
    this.processing = true;

    this.loginService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.authenticationError = false;
        if (!this.router.getCurrentNavigation()) {
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['/home/landingPage']);
          this.toastr.success('Logged in successfully ! Welcome !');
        }
      },
      error: () => {
        this.authenticationError = true;
        this.toastr.error('Failed to log in ! Check your credentials or connection..');
      }
    });

  }

  handleLoginSuccess(data: any) {
    this.processing = false;
    this.error = false;
    this.token.handle(data.id_token);
    this.authService.changeAuthStatus(true);
    this.router.navigateByUrl(this.returnUrl);
  }

  private handleLoginError() {
    this.processing = false;
    this.error = true;
  }

  visible: boolean = true;
  changetype: boolean = true;
  viewpass() {
    this.visible = !this.visible;
    this.changetype = !this.changetype;
  }

}
