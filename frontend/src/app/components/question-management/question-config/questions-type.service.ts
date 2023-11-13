/* eslint-disable @typescript-eslint/member-ordering */
/* eslint-disable @typescript-eslint/restrict-template-expressions */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_TYPE_QUESTION } from '../../../app.constants';
import { Observable } from 'rxjs/internal/Observable';
import { TypeQuestion } from '../_models/questions-type.model';


@Injectable({
  providedIn: 'root'
})
export class QuestionsTypeService {

  constructor(private _httpClient: HttpClient) {
  }

  formData: TypeQuestion;

   httpOptions = {
    headers: new HttpHeaders({
     'Content-Type': 'application/json'
      })
    };
    

  // GET

  getQuestionTypesAll(): Observable<TypeQuestion[]> {
    return this._httpClient.get<TypeQuestion[]>(SERVER_API_TYPE_QUESTION)
 
   }
 
   getOneQuestionType(id: any) {
     return this._httpClient.get(SERVER_API_TYPE_QUESTION + `/${id}`) 
 
   }
 
 
   // ADD
 
   addQuestionType(questionType: TypeQuestion) {
      return this._httpClient.post(SERVER_API_TYPE_QUESTION,questionType) 
   }
 
 
  // Update
 
  updateQuestionsType() {
      return this._httpClient.put(SERVER_API_TYPE_QUESTION+`/${this.formData.id}`,this.formData)
   }
 
   // Delete
 
   deleteQuestionsType(id: any) {
      return this._httpClient.delete(SERVER_API_TYPE_QUESTION+`/${id}`) 
 
   }
   

}
