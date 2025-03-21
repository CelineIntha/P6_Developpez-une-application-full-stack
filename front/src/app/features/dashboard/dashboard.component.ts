import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {ArticleService} from "../../core/services/article.service";
import {ArticleResponse} from "../../core/models/article-response";
import {DatePipe} from "@angular/common";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    DatePipe,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    RouterLink
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
      next: (data: ArticleResponse[]) => {
        console.log('Articles reçus :', data);
        this.articles = data;
      },
      error: (err) => console.error('Erreur récupération articles', err)
    });
  }


  setSortOrder(order: 'asc' | 'desc') {
    this.sortOrder = order;

    this.articles.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();

      return order === 'asc' ? dateA - dateB : dateB - dateA;
    });
  }


  logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }

}
