import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { SERVER_API_MAIL, SERVER_API_URL, SERVER_API_USER, SERVER_API_USER_ADMIN } from "src/app/app.constants";
import { User, UserDTO } from "./user.model";

@Injectable()
export class UserService {

  constructor(private _httpClient: HttpClient) { }

  // GET ALL / ONE

  getAllUser(): Observable<UserDTO[]> {
    return this._httpClient.get<UserDTO[]>(SERVER_API_USER_ADMIN);
  }

  getUserById(login: string): Observable<UserDTO> {
    return this._httpClient.get<UserDTO>(SERVER_API_USER_ADMIN + `/${login}`);
  }


  //Add

  addUser(user: User) {

    return this._httpClient.post<User>(SERVER_API_USER_ADMIN,user) 

  }


 //Update

 updateUser(user: User) {
  return this._httpClient.put(SERVER_API_USER_ADMIN , user)

  }

  //Delete

  deleteUser(login: any) {
    return this._httpClient.delete(SERVER_API_USER_ADMIN + `/${login}`) 

  }


  //Authorities

  authorities(): Observable<string[]> {
    return this._httpClient.get<string[]>(SERVER_API_URL+'/authorities');
  }

  addCredentials(username: string, password: string): Observable<void> {
    const body = { username, password };
    const headers = { 'Content-Type': 'application/json' };
    return this._httpClient.post<void>(SERVER_API_MAIL, body, { headers });
  }
  

  // addCredentials(username: string, password: string): Observable<void> {
  //   const body = { username, password };
  //   return this._httpClient.post<void>(SERVER_API_MAIL, body);
  // }

}
