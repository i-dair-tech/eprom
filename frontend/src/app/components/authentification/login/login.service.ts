import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';


import { Login } from '../models/login.model';
import { Account } from '../models/account.model';
import { AccountService } from '../authService/account.service';
import { AuthService } from '../authService/auth.service';


@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(private accountService: AccountService, private authServerProvider: AuthService) {}

  login(credentials: Login): Observable<void> {
   
     return this.authServerProvider.login(credentials)
  
  }

  logout(): void {
    this.authServerProvider.logout().subscribe({ complete: () => this.accountService.authenticate(null) });
  }

  public logoutWithoutrefrech() {
    this.authServerProvider.logout().subscribe({ complete: () => this.accountService.authenticate(null) });
}





}
