import { Component, inject } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { MatButton, MatIconButton } from "@angular/material/button";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButton, MatIconButton, NgOptimizedImage],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private authService: AuthService = inject(AuthService);
  private fb: FormBuilder = inject(FormBuilder);
  private router: Router = inject(Router);

  errorMessage: string | null = null;

  loginForm: FormGroup = this.fb.group({
    usernameOrEmail: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  login(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.errorMessage = null;

    const credentials = this.loginForm.value;

    this.authService.login(credentials).subscribe({
      next: (): void => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Erreur de connexion', err);

        if (err.error) {
          if (err.error.status === 401 || err.error.status === 404) {
            this.errorMessage = err.error.message || "Nom d'utilisateur, email ou mot de passe incorrect.";

            if (err.error.errors) {
              this.loginForm.setErrors(err.error.errors);
            }
            return;
          }
        }

        this.errorMessage = "Une erreur est survenue. Veuillez réessayer plus tard.";
      }
    });
  }

  goBack() {
    this.router.navigate(['..']);
  }
}
