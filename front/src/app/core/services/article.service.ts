import {Injectable, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ArticleResponse} from '../models/article-response';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private http = inject(HttpClient);
  // http://localhost:8080/api
  private apiUrl = environment.apiUrl;

  getAllArticles(): Observable<ArticleResponse[]> {
    return this.http.get<ArticleResponse[]>(`${this.apiUrl}/articles`);
  }

  getArticleById(id: number): Observable<ArticleResponse> {
    return this.http.get<ArticleResponse>(`${this.apiUrl}/articles/${id}`);
  }

  createArticle(articleData: { title: string; content: string; topicId: number }) {
    return this.http.post<ArticleResponse>(`${this.apiUrl}/articles`, articleData);
  }

}
