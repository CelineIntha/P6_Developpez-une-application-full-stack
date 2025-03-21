import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticleService} from '../../../core/services/article.service';
import {ArticleResponse} from '../../../core/models/article-response';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {
  private route: ActivatedRoute = inject(ActivatedRoute);
  private articleService: ArticleService = inject(ArticleService);
  private fb: FormBuilder = inject(FormBuilder);
  private router: Router = inject(Router);

  article: ArticleResponse | null = null;
  commentForm: FormGroup = this.fb.group({
    content: ['', [Validators.required]]
  });

  ngOnInit(): void {
    const id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.articleService.getArticleById(id).subscribe({
      next: (article: ArticleResponse): ArticleResponse => this.article = article,
      error: (): Promise<boolean> => this.router.navigate(['/dashboard'])
    });
  }

  submitComment(): void {
    if (this.commentForm.invalid || !this.article) return;

    const newComment = {
      content: this.commentForm.value.content,
      articleId: this.article.id
    };


    this.article.comments.push({
      id: Date.now(),
      content: newComment.content,
      author: 'Vous',
      createdAt: new Date()
    });

    this.commentForm.reset();
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
