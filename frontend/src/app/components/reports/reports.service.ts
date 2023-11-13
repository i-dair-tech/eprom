import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { SERVER_API_REPORTS } from "src/app/app.constants";
import { User } from "../user-management/user.model";

@Injectable()
export class ReportService { 

    constructor(private _httpClient: HttpClient) { }

    getQuestionsNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/questions-number');
      }

      getSurveysNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/surveys-number');
      }

      getStudiesNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/studies-number');
      }


      getUsersNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/users-number');
      }

      getAnswersNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/answer-total-number');
      }

      getInvitationsNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/invitation-total-number');
      }

      getInvitationsSentNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/invitations-sent-total');
      }

      getInvitationsNotAnsweredNumber(): Observable<number> {
        return this._httpClient.get<number>(SERVER_API_REPORTS +'/invitations-not-answered-total');
      }
      

      //users

      getPatientsList(): Observable<any> {
        return this._httpClient.get<any>(SERVER_API_REPORTS +'/patients-list');
      }

      getAdminsList(): Observable<any> {
        return this._httpClient.get<any>(SERVER_API_REPORTS +'/admins-list');
      }

      getStudyCoordinatorsList(): Observable<any> {
        return this._httpClient.get<any>(SERVER_API_REPORTS +'/study-coordinators-list');
      }



      getScoreEvolutionPerOwner(owner :string): Observable<any> {
        return this._httpClient.post<any>(SERVER_API_REPORTS +'/score-owner', owner);
      }

      //surveys

      getSurveysCountPerInvitation(): Observable<any> {
        return this._httpClient.get<any>(SERVER_API_REPORTS +'/survey-invitation-count');
      }


      //studies


      countInvitationsBySurvey(id: number): Observable<any>{
      return this._httpClient.post<any>(SERVER_API_REPORTS +'/study-count-invitations', { studyId: id });

      }


      getScoresByOwnersAndSurveysInStudy(id: number): Observable<any>{
        return this._httpClient.post<any>(SERVER_API_REPORTS +'/study-score-per-owner', { studyId: id });
  
        }


  
}