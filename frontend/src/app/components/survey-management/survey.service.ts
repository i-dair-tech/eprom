import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import {SERVER_API_INVITATION, SERVER_API_SURVEY } from "src/app/app.constants";
import { Invitation } from "./_models/invitation.model";
import { Survey } from "./_models/survey.model";

@Injectable()
export class SurveyService {

  constructor(private _httpClient: HttpClient) { }

  // GET ALL / ONE

  getAllSurveys(): Observable<Survey[]> {
    return this._httpClient.get<Survey[]>(SERVER_API_SURVEY);
  }

  getSurveyById(id: number): Observable<Survey> {
    return this._httpClient.get<Survey>(SERVER_API_SURVEY + `/${id}`);
  }


  // CREATE

  addSurvey(survey: Survey): Observable<Survey> {
    return this._httpClient.post<Survey>(SERVER_API_SURVEY, survey);
  }

  //UPDATE

  updateSurvey(survey: Survey): Observable<Survey> {
    return this._httpClient.put<Survey>(SERVER_API_SURVEY+`/ ${survey.id}`, survey);
  }

  // DELETE

  deleteSurvey(id: number): Observable<Survey> {
    return this._httpClient.delete<Survey>(SERVER_API_SURVEY + `/${id}`);
  }


  // SEND INVITATION

  sendInvitation(invitation: any): Observable<any> {

    // const headers = new HttpHeaders();
    // headers.set('Content-Type', 'multipart/form-data');
    
    return this._httpClient.post<any>(SERVER_API_INVITATION, invitation  );
  }


}
