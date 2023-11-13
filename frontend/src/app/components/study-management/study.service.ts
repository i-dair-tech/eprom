import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { SERVER_API_QUESTION, SERVER_API_STUDY } from "src/app/app.constants";
import { Study } from "./study.model";

@Injectable()
export class StudyService {

  constructor(private _httpClient: HttpClient) { }

  // GET ALL / ONE

  getAllStudies(): Observable<Study[]> {
    return this._httpClient.get<Study[]>(SERVER_API_STUDY);
  }

  getStudyById(id: number): Observable<Study> {
    return this._httpClient.get<Study>(SERVER_API_STUDY + `/${id}`);
  }


  // CREATE

  addStudy(study: Study): Observable<Study> {
    return this._httpClient.post<Study>(SERVER_API_STUDY, study);
  }

  //UPDATE

  updateStudy(study: Study): Observable<Study> {
    return this._httpClient.put<Study>(SERVER_API_STUDY +`/ ${study.id}`, study);
  }

  // DELETE

  deleteStudy(id: number): Observable<Study> {
    return this._httpClient.delete<Study>(SERVER_API_STUDY + `/${id}`);
  }

}
