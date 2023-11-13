import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { SERVER_API_QUESTION } from "src/app/app.constants";
import { Question } from "./_models/question.model";

@Injectable()
export class QuestionService {

  constructor(private _httpClient: HttpClient) { }

  // GET ALL / ONE

  getAllQuestion(): Observable<Question[]> {
    return this._httpClient.get<Question[]>(SERVER_API_QUESTION);
  }

  getQuestionById(id: number): Observable<Question> {
    return this._httpClient.get<Question>(SERVER_API_QUESTION + `/${id}`);
  }


  //Add

  addQuestion(question: Question) {

    return this._httpClient.post(SERVER_API_QUESTION,question) 

  }


 //Update

 updateQuestion(question: Question) {
  return this._httpClient.put(SERVER_API_QUESTION +`/ ${question.id}`, question)

  }

  //Delete

  deleteQuestion(id: any) {
    return this._httpClient.delete(SERVER_API_QUESTION + `/${id}`) 

  }


}
