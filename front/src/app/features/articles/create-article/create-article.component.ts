import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { ArticleService } from '../../../core/services/article.service';
import {TopicService} from "../../../core/services/topic.service";
import {Topic} from "../../../core/models/topic";


@Component({
  selector: 'app-create-article',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private articleService = inject(ArticleService);
  private topicService = inject(TopicService);

  articleForm: FormGroup = this.fb.group({
    title: ['', [Validators.required]],
    content: ['', [Validators.required]],
    topicId: ['', [Validators.required]]
  });

  topics: Topic[] = [];
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.topicService.getAllTopics().subscribe({
      next: (data) => this.topics = data,
      error: () => this.errorMessage = "Erreur lors du chargement des thèmes"
    });
  }

  submitArticle(): void {
    if (this.articleForm.invalid) return;

    this.articleService.createArticle(this.articleForm.value).subscribe({
      next: () => {
        this.snackBar.open("Article créé avec succès !", "Fermer", { duration: 2000 });
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Erreur création article', err);
        this.errorMessage = err.error?.message || "Erreur lors de la création de l'article.";
      }
    });
  }

  goBack(): void {
    this.router.navigate(['..']);
  }
}
