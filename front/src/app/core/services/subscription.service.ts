import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../environments/environment";
import {TopicSubscription} from "../models/topic-subscription";

@Injectable({providedIn: 'root'})
export class SubscriptionService {
  private http: HttpClient = inject(HttpClient);
  private apiUrl: string = `${environment.apiUrl}/subscriptions`;

  getUserSubscriptions(): Observable<TopicSubscription[]> {
    return this.http.get<TopicSubscription[]>(this.apiUrl);
  }

  subscribe(topicId: number): Observable<TopicSubscription> {
    return this.http.post<TopicSubscription>(this.apiUrl, {topicId});
  }

  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${topicId}`);
  }
}
