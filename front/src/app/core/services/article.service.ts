import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Article} from '../models/article';
import {environment} from '../../../environments/environment';
import {ArticleComment} from "../models/article-comment";

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getAllArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/articles`);
  }

  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/articles/${id}`);
  }

  createArticle(articleData: { title: string; content: string; topicId: number }): Observable<Article> {
    return this.http.post<Article>(`${this.apiUrl}/articles`, articleData);
  }

  addComment(articleId: number, content: string): Observable<ArticleComment> {
    return this.http.post<ArticleComment>(
      `${this.apiUrl}/articles/${articleId}/comments`,
      {content}
    );
  }

}
