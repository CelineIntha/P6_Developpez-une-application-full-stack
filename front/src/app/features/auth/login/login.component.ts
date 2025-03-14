import { Component, inject } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import {MatButton, MatIconButton} from "@angular/material/button";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MatButton, MatIconButton, NgOptimizedImage],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm = new FormGroup({
    usernameOrEmail: new FormControl<string | null>(''),
    password: new FormControl<string | null>('')
  });

  login() {
    if (this.loginForm.valid) {
      const credentials = this.loginForm.getRawValue() as { usernameOrEmail: string; password: string };

      console.log('Tentative de connexion :', credentials);

      this.authService.login(credentials).subscribe({
        next: (response) => {
          console.log('Connexion réussie', response);
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error('Erreur de connexion', err);
        }
      });
    }
  }

  goBack() {
    this.router.navigate(['..']);
  }
}
