import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {NgOptimizedImage} from "@angular/common";
import {MatButton, MatIconButton} from "@angular/material/button";
import {AuthService} from "../../../core/services/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  imports: [
    ReactiveFormsModule,
    NgOptimizedImage,
    MatIconButton,
    MatButton
  ],
  standalone: true
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string | null = null;
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router= inject(Router);


  constructor() {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$')
        ]
      ]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    const formData = this.registerForm.value;
    this.authService.register(formData).subscribe({
      next: () => {
        alert('Inscription réussie');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.errorMessage = error.error.message || 'Une erreur est survenue lors de l’inscription.';
      }
    });
  }

  goBack() {
    this.router.navigate(['..']);
  }
}
