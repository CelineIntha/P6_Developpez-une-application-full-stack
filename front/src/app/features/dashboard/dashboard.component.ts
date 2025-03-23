import {Component, inject, OnInit} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {ArticleService} from "../../core/services/article.service";
import {Article} from "../../core/models/article";
import {DatePipe} from "@angular/common";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {RouterLink} from "@angular/router";
import {MatTooltip} from "@angular/material/tooltip";
import {User} from "../../core/models/user";
import {UserService} from "../../core/services/user.service";

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
  userService: UserService = inject(UserService);
  articleService: ArticleService = inject(ArticleService);

  articles: Article[] = [];
  sortOrder: 'asc' | 'desc' = 'desc';
  user: User | null = null;

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: User): void => {
        this.user = user;
        this.articleService.getAllArticles().subscribe({
          next: (data: Article[]): void => {
            this.articles = data.filter((article: Article) =>
              article.author === this.user?.username || this.user?.subscribedTopics.some(topic => topic.id === article.topicId)
            );
          }
        });
      },
      error: err => console.error('Erreur récupération utilisateur', err)
    });
  }


  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'desc' ? 'asc' : 'desc';

    this.articles.sort((a: Article, b: Article): number => {
      const dateA: number = new Date(a.createdAt).getTime();
      const dateB: number = new Date(b.createdAt).getTime();
      return this.sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
    });
  }
}
