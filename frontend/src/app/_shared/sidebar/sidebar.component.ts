import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { AccountService } from 'src/app/components/authentification/authService/account.service';
import { AuthService } from 'src/app/components/authentification/authService/auth.service';
import { User } from 'src/app/components/user-management/user.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  expand : boolean = false;
  study : boolean = false;
  survey : boolean = false;
  question : boolean = false;
  user: boolean = false;
  report : boolean = false;
  surveyPatient: boolean=false;
  hasAuthority: boolean ;
  isPatient:boolean;
  userInfo:User;
  role:string;

  constructor(
    private authService: AuthService,
    private router: Router,
    private accountService: AccountService,
    private localStorageService: LocalStorageService,
    private sessionStorageService: SessionStorageService,
  ) { }

  ngOnInit(): void {

    this.userInfo = this.localStorageService.retrieve('accountInfo') ? this.localStorageService.retrieve('accountInfo') : this.sessionStorageService.retrieve('accountInfo');

    if(this.userInfo.authorities!.includes("ROLE_ADMIN")){
      this.hasAuthority = true;
      this.role = "ADMIN";
      this.isPatient = false;
    }  else if (this.userInfo.authorities!.includes("ROLE_PATIENT")){
      this.hasAuthority = false;
      this.isPatient = true;
      this.role = "PATIENT";
    }else{
      this.hasAuthority = false;
      this.isPatient = false;
      this.role = "STUDY_COORDINATOR";
    }
    

  }


  toggle(){

    this.expand=!this.expand
    this.study=!this.study
    this.survey=!this.survey
    this.question=!this.question

  }



  logout() {
    
    this.authService.logout().subscribe({ complete: () => this.accountService.authenticate(null) });;
    this.router.navigate(['/auth']);

  }




}
