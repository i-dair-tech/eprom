import { Observable } from 'rxjs';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../../../../app/app.constants';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(
        private localStorageService: LocalStorageService,
        private sessionStorageService: SessionStorageService,
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!request || !request.url || (/^http/.test(request.url) && !(SERVER_API_URL && request.url.startsWith(SERVER_API_URL)))) {
            return next.handle(request);
        }

        const token = this.localStorageService.retrieve('authenticationToken') || this.sessionStorageService.retrieve('authenticationToken');

        if (!!token) {
            request = request.clone({
                setHeaders: {
                    Authorization: 'Bearer ' + token,
                }
            });
        }
        return next.handle(request);
    }
}
