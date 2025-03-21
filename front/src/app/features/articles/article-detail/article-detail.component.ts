import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticleService} from '../../../core/services/article.service';
import {Article} from '../../../core/models/article';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {ArticleComment} from "../../../core/models/article-comment";
import {MatTooltip} from "@angular/material/tooltip";

@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatTooltip
  ],
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {
  private route: ActivatedRoute = inject(ActivatedRoute);
  private articleService: ArticleService = inject(ArticleService);
  private fb: FormBuilder = inject(FormBuilder);
  private router: Router = inject(Router);

  article: Article | null = null;

  commentForm: FormGroup = this.fb.group({
    content: ['', Validators.required]
  });

  ngOnInit(): void {
    const id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.articleService.getArticleById(id).subscribe({
      next: (article: Article) => {
        this.article = article;
      },
      error: () => {
        this.router.navigate(['/dashboard']);
      }
    });
  }

  submitComment(): void {
    if (this.commentForm.invalid || !this.article) return;

    const content = this.commentForm.value.content;

    this.articleService.addComment(this.article.id, content).subscribe({
      next: (newComment: ArticleComment) => {
        this.article?.comments.push(newComment);
        this.commentForm.reset();
      },
      error: () => {
        console.error('Erreur lors de l\'ajout du commentaire');
      }
    });

  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
