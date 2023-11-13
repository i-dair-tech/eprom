import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { Observable, ReplaySubject, of } from 'rxjs';
import { shareReplay, tap, catchError } from 'rxjs/operators';


import { ApplicationConfigService } from '../config/application-config.service';
import { Account } from '../models/account.model';
import { StateStorageService } from './state-storage.service';
import { environment } from 'src/environments/environment';
import { SERVER_API_URL } from 'src/app/app.constants';



@Injectable({ providedIn: 'root' })
export class AccountService {
  private userIdentity: Account | null = null;
  private authenticationState = new ReplaySubject<Account | null>(1);
  private accountCache$?: Observable<Account> | null;

  constructor(
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private http: HttpClient,
    private stateStorageService: StateStorageService,
    private router: Router,
    private applicationConfigService: ApplicationConfigService,
    private localStorageService: LocalStorageService, 
  ) {}

  save(account: Account): Observable<{}> {
    return this.http.post(this.applicationConfigService.getEndpointFor('api/account'), account);
  }

  authenticate(identity: Account | null): void {

    this.userIdentity = identity;
    this.authenticationState.next(this.userIdentity);
    if (!identity) {
      this.accountCache$ = null;
    }
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    if (!this.userIdentity) {
      return false;
    }
    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }
    return this.userIdentity.authorities.some((authority: string) => authorities.includes(authority));
  }


  isAuthenticated(): boolean {
    return this.userIdentity !== null;
  }

  getAuthenticationState(): Observable<Account | null> {
    return this.authenticationState.asObservable();
  }

  public fetch(): Observable<Account> {

   let token = this.localStorageService.retrieve('authenticationToken')?this.localStorageService.retrieve('authenticationToken'):this.sessionStorageService.retrieve('authenticationToken')

    let header = new HttpHeaders().set(
      "Authorization", "Bearer "+ token
      
    ).set('Content-Type', 'application/json');
    
       return this.http.get<Account>(SERVER_API_URL+`/account`, {headers:header})
      
  }



  private navigateToStoredUrl(): void {
    // previousState can be set in the authExpiredInterceptor and in the userRouteAccessService
    // if login is successful, go to stored previousState and clear previousState
    const previousUrl = this.stateStorageService.getUrl();
    if (previousUrl) {
      this.stateStorageService.clearUrl();
      this.router.navigateByUrl(previousUrl);
    }
  }


  identity(force?: boolean): Observable<Account | null> {
    if (!this.accountCache$ || force) {
      this.accountCache$ = this.fetch().pipe(
        tap((account: Account) => {
          this.authenticate(account);

          // After retrieve the account info, the language will be changed to
          // the user's preferred language configured in the account setting
          // unless user have choosed other language in the current session
          if (!this.sessionStorageService.retrieve('locale')) {
            this.translateService.use(account.langKey);
          }

          this.navigateToStoredUrl();
        }),
        shareReplay()
      );
    }
    return this.accountCache$.pipe(catchError(() => of(null)));
  }



}
