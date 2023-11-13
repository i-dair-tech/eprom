import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'src/app/app.constants';


@Injectable({ providedIn: 'root' })
export class PasswordResetFinishService {


  constructor(private _httpClient: HttpClient) {}

  save(key: string, newPassword: string): Observable<{}> {
    return this._httpClient.post<any>(SERVER_API_URL + '/account/reset-password/finish', { key, newPassword });
  }
}
