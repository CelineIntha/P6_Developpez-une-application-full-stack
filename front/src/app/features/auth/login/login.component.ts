import {Component, inject} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../../core/services/auth.service';
import {Router} from '@angular/router';
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatSnackBar} from "@angular/material/snack-bar";

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
  private _snackBar: MatSnackBar = inject(MatSnackBar);

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
        this.openSnackBar("Authentification réussie !", "OK");
        setTimeout((): void => {
          this.router.navigate(['/dashboard']);
        }, 2000);
      },
      error: (err) => {
        console.error('Erreur de connexion', err);

        if (err.error) {
          this.errorMessage = err.error.message || "Nom d'utilisateur, email ou mot de passe incorrect.";
          return;
        }

        this.errorMessage = "Une erreur est survenue. Veuillez réessayer plus tard.";
      }
    });
  }

  openSnackBar(message: string, action: string): void {
    this._snackBar.open(message, action, {
      duration: 2000,
      verticalPosition: 'top',
      horizontalPosition: 'right',
    });
  }


  goBack(): void {
    this.router.navigate(['..']);
  }
}
