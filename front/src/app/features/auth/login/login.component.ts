import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
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
}
