import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApplicationConfigService } from '../config/application-config.service';
import { Login } from '../models/login.model';
import { TokenService } from '../token/token.service';
import { map, mergeMap } from 'rxjs/operators';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { AccountService } from './account.service';
import { Account } from '../models/account.model';
import { SERVER_API_URL } from 'src/app/app.constants';


type JwtToken = {
  id_token: string;
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, 
    private token: TokenService, 
    private route: Router,
    private applicationConfigService: ApplicationConfigService,
    private localStorageService: LocalStorageService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService
    ) { }

    getToken(): string {
      const tokenInLocalStorage: string | null = this.localStorageService.retrieve('authenticationToken');
      const tokenInSessionStorage: string | null = this.sessionStorageService.retrieve('authenticationToken');
      return tokenInLocalStorage ?? tokenInSessionStorage ?? '';
    }

  private loggedIn = new BehaviorSubject<boolean>(this.token.loggedIn());
  authStatus = this.loggedIn.asObservable();

  changeAuthStatus(value: boolean) {
    this.loggedIn.next(value);
  }

  register(data: any) {
    return this.http.post(SERVER_API_URL + `/register`, data)
  }

  // login(data: any) {
  //   return this.http.post(`${environment['apiBaseUrl']}/authenticate`, data)
  // }

  login(credentials: Login ): Observable<void> {
    return this.http
      .post<JwtToken>(environment.apiBaseUrl+`/authenticate`, credentials)
      .pipe(map(response => this.authenticateSuccess(response, credentials.rememberMe)))
      
  }

  // logout() {
  //   this.token.remove();
  //   this.changeAuthStatus(false);
  //   this.route.navigateByUrl('/auth/login')
  // }

  logout(): Observable<void> {
    return new Observable(observer => {
      this.localStorageService.clear('authenticationToken');
      this.sessionStorageService.clear('authenticationToken');
      observer.complete();
    });
  }



  private authenticateSuccess(response: JwtToken, rememberMe: boolean): void {
    const jwt = response.id_token;
    if (!rememberMe) {
      this.localStorageService.store('authenticationToken', jwt);
      this.sessionStorageService.clear('authenticationToken');
    } else {
      this.sessionStorageService.store('authenticationToken', jwt);
      this.localStorageService.clear('authenticationToken');
    }

    this.accountService.fetch().subscribe((account:Account)=>{


      if (!rememberMe) {
        this.localStorageService.store('accountInfo', account);
        this.sessionStorageService.clear('accountInfo');
      } else {
        this.sessionStorageService.store('accountInfo', account);
        this.localStorageService.clear('accountInfo');
      }

     })
  }
}
