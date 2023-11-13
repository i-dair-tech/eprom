
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SERVER_API_LOGS, SERVER_API_URL } from 'src/app/app.constants';
import { Log } from './logs.model';

@Injectable({
  providedIn: 'root'
})
export class LogsService {

  constructor(private _httpClient: HttpClient) { }


  // GET

  getAllLogs(): Observable<Log[]> {
    return this._httpClient.get<Log[]>(SERVER_API_LOGS)
 
   }



}
