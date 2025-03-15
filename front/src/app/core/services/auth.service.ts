import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {AuthResponse} from "../models/auth-response";
import {RegisterData} from "../models/register-data";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl

  login(credentials: { usernameOrEmail: string; password: string }): Observable<AuthResponse> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        if (response.token) {
          this.saveToken(response.token);
        }
      })
    );
  }

  private saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // TODO : fix any
  register(userData: RegisterData): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/register`, userData);
  }
}
