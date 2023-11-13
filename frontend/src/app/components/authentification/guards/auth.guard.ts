import { Injectable, isDevMode } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AccountService } from '../authService/account.service';
import { StateStorageService } from '../authService/state-storage.service';
import { TokenService } from '../token/token.service';
import { map } from 'rxjs/operators';


@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private accountService: AccountService, private stateStorageService: StateStorageService) {}
    //The Angular CanActivate guard decides, if a route can be activated . 
    //We use this guard, when we want to check on some condition, 
    //before activating the component or showing it to the user. This allows us to cancel the navigation.
    // this wil only check if there's a token
    // this doesn't means that the token is valid!
    // optionally you can also validate the token on the server
    // each time this guard is invoked
    // but that ofcourse could be a lot of calls
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.accountService.identity().pipe(
          map(account => {
            if (account) {
              const authorities = route.data['authorities'];
    
              if (!authorities || authorities.length === 0 || this.accountService.hasAnyAuthority(authorities)) {
                return true;
              }
    
              if (isDevMode()) {
                console.error('User has not any of required authorities: ', authorities);
              }
              this.router.navigate(['accessdenied']);
              return false;
            }
    
            this.stateStorageService.storeUrl(state.url);
            this.router.navigate(['auth/login']);
            return false;
          })
        );
      }
    
}
