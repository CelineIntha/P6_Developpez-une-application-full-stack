import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {ArticleService} from "../../core/services/article.service";
import {ArticleResponse} from "../../core/models/article-response";
import {DatePipe} from "@angular/common";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {RouterLink} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    DatePipe,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    RouterLink,
    MatTooltip
  ],

  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  articles: ArticleResponse[] = [];
  sortOrder: 'asc' | 'desc' = 'desc';


  constructor(private articleService: ArticleService) {
  }

  ngOnInit(): void {
    this.articleService.getAllArticles().subscribe({
      next: (data: ArticleResponse[]): void => {
        console.log('Articles reçus :', data);
        this.articles = data;
      },
      error: (err: HttpErrorResponse): void => console.error('Erreur récupération articles', err)
    });
  }


  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc';

    this.articles.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
      return this.sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
    });
  }
}
