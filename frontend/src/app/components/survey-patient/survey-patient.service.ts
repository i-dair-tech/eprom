import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { SERVER_API_ANSWER, SERVER_API_INVITATION, SERVER_API_SURVEY, SERVER_API_SURVEY_PER_PATIENT } from "src/app/app.constants";
import { Invitation } from "../survey-management/_models/invitation.model";
import { Survey } from "../survey-management/_models/survey.model";
import { Answer, FinalAnswer } from "./_models/answer.model";
import { SurveyPatient } from "./_models/survey-patient.model";



@Injectable()
export class SurveyPatientService {

  constructor(private _httpClient: HttpClient) { }

  // GET ALL / ONE

  getAllSurveysByPatient(email: string): Observable<SurveyPatient[]> {
    return this._httpClient.get<SurveyPatient[]>(SERVER_API_SURVEY_PER_PATIENT+ `/${email}`);
  }


  getSurveyStatus(email: string): Observable<any[]> {
    return this._httpClient.get<any[]>(SERVER_API_INVITATION + `/distinct/${email}`);
  }

  getSurveyById(id: number): Observable<Survey> {
    return this._httpClient.get<Survey>(SERVER_API_SURVEY + `/${id}`);
  }


  // save answers

  saveAnswers(answers: FinalAnswer): Observable<FinalAnswer> {
    return this._httpClient.post<FinalAnswer>(SERVER_API_ANSWER, answers);
  }

}
