import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpErrorResponse, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { EventManager, EventWithContent } from 'src/app/_shared/utils/event-manager.service';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {
  constructor(private _router: Router, private eventManager: EventManager) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(
        (event: HttpEvent<any>) => { },
        (err: any) => {
          if (err instanceof HttpErrorResponse) {
            if (!(err.status === 401 && (err.message === '' || (err.url && err.url.includes('api/account'))))) {
              this.eventManager.broadcast(new EventWithContent('httpError', err));
            }
            if (err.status === 403) {
              this._router.navigate(['/error/403'])
            }
            if (err.status === 401) {
              this._router.navigate(['auth/login'])
            }
          }
        }
      )
    );
  }
}
