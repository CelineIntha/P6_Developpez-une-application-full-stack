import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {ArticleService} from "../../core/services/article.service";
import {ArticleResponse} from "../../core/models/article-response";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent, DatePipe
  ],
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  articles: ArticleResponse[] = [];

  constructor(private articleService: ArticleService) {
  }

  ngOnInit(): void {
    this.articleService.getAllArticles().subscribe({
      next: (data) => {
        console.log('Articles reçus :', data); // 👈 ajoute un log ici
        this.articles = data;
      },
      error: (err) => console.error('Erreur récupération articles', err)
    });
  }


  logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }
}
