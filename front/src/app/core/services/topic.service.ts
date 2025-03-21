import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/environment";
import {Topic} from "../models/topic";

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getAllTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.apiUrl}/topics`);
  }
}
