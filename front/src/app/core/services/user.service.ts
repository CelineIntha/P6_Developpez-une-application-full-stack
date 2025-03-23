import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../models/user";
import {Observable} from "rxjs";
import {UpdateUser} from "../models/update-user";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})

export class UserService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/me`);
  }

  updateUser(data: UpdateUser): Observable<UpdateUser> {
    return this.http.put(`${this.apiUrl}/users/update`, data);
  }
}
