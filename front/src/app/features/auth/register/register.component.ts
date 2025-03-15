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
  errorMessage: string | null = null;
  private authService: AuthService = inject(AuthService);
  private fb: FormBuilder = inject(FormBuilder);
  private router: Router = inject(Router);

  registerForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    password: ['', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(255)
    ]]
  });

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.errorMessage = null;

    // TODO: avoid any
    const formData: any = this.registerForm.value;

    this.authService.register(formData).subscribe({
      next: (): void => {
        alert('Inscription réussie');
        this.router.navigate(['/login']);
      },
      // TODO : avoid any
      error: (err: any): void => {
        console.error('Erreur lors de l’inscription', err);

        if (err.error) {
          if (err.error.status === 400 || err.error.status === 409) {
            this.errorMessage = err.error.message || "Certains champs sont invalides.";

            if (err.error.errors) {
              this.registerForm.setErrors(err.error.errors);
            }
            return;
          }
        }

        this.errorMessage = "Une erreur est survenue lors de l'inscription. Veuillez réessayer plus tard.";
      }
    });
  }

  goBack(): void {
    this.router.navigate(['..']);
  }
}
