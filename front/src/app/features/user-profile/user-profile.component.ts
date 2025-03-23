import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../../core/services/user.service';
import {AuthService} from '../../core/services/auth.service';
import {SubscriptionService} from '../../core/services/subscription.service';
import {TopicService} from '../../core/services/topic.service';

import {User} from '../../core/models/user';
import {Topic} from '../../core/models/topic';
import {TopicSubscription} from '../../core/models/topic-subscription';
import {UpdateUser} from '../../core/models/update-user';

import {HttpErrorResponse} from '@angular/common/http';
import {NavbarComponent} from '../../shared/components/navbar/navbar.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
  imports: [
    ReactiveFormsModule,
    NavbarComponent,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ]
})
export class UserProfileComponent implements OnInit {
  private fb: FormBuilder = inject(FormBuilder);
  private userService: UserService = inject(UserService);
  private authService: AuthService = inject(AuthService);
  private subscriptionService: SubscriptionService = inject(SubscriptionService);
  private topicService: TopicService = inject(TopicService);
  private router: Router = inject(Router);

  profileForm!: FormGroup<{
    username: FormControl<string>;
    email: FormControl<string>;
    password: FormControl<string>;
  }>;

  errorMessage: string | null = null;

  topics: Topic[] = [];
  subscribedTopicIds: number[] = [];
  subscribedTopics: Topic[] = [];

  ngOnInit(): void {
    this.topicService.getAllTopics().subscribe({
      next: (topics: Topic[]) => {
        this.topics = topics;
        this.loadUserSubscriptions();
      }
    });

    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.profileForm = this.fb.group({
          username: this.fb.control(user.username, {
            nonNullable: true,
            validators: [Validators.minLength(3)]
          }),
          email: this.fb.control(user.email, {
            nonNullable: true,
            validators: [Validators.email]
          }),
          password: this.fb.control('', {
            nonNullable: true,
            validators: [
              Validators.minLength(8),
              Validators.pattern(
                '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$'
              )
            ]
          })
        });
      },
      error: () => {
        this.errorMessage = 'Impossible de charger le profil utilisateur.';
      }
    });
  }

  /**
   * Récupère les abonnements de l'utilisateur depuis l'API.
   * Met à jour la liste des thèmes abonnés (`subscribedTopicIds`)
   * et filtre la liste complète des thèmes (`topics`) pour obtenir
   * la liste des abonnements actuels (`subscribedTopics`).
   */
  private loadUserSubscriptions(): void {
    this.subscriptionService.getUserSubscriptions().subscribe({
      next: (subscriptions: TopicSubscription[]) => {
        this.subscribedTopicIds = subscriptions.map((subscription: TopicSubscription) => subscription.topicId);
        this.updateSubscribedTopics();
      }
    });
  }


  /**
   * Met à jour la liste des thèmes abonnés (`subscribedTopics`)
   * en filtrant tous les topics pour ne conserver que ceux dont l'ID
   * est présent dans `subscribedTopicIds`.
   */
  updateSubscribedTopics(): void {
    this.subscribedTopics = this.topics.filter((topic: Topic) =>
      this.subscribedTopicIds.includes(topic.id)
    );
  }

  /**
   * Vérifie si l'utilisateur est abonné à un thème.
   *
   * @param topicId - L'identifiant du thème à vérifier
   * @returns true si l'utilisateur est abonné, false sinon
   */
  isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIds.includes(topicId);
  }


  /**
   * Désabonne l'utilisateur du thème et met à jour la liste des abonnements.
   *
   * @param topicId - L'identifiant du thème pour le désabonnement
   */
  unsubscribe(topicId: number): void {
    this.subscriptionService.unsubscribe(topicId).subscribe({
      next: () => {
        this.subscribedTopicIds = this.subscribedTopicIds.filter(
          id => id !== topicId
        );
        this.updateSubscribedTopics();
      }
    });
  }


  /**
   * Envoi le formulaire pour mettre à jour le profil utilisateur.
   * Si le formulaire est invalide ou qu'il n'a pas été modifié, rien ne se passe.
   * En cas de succès, l'utilisateur est déconnecté et redirigé vers l'accueil.
   * En cas d'erreur, un message est affiché.
   */
  onSubmit(): void {
    if (this.profileForm.invalid) return;
    if (this.profileForm.pristine) {
      this.errorMessage = 'Aucune modification détectée.';
      return;
    }

    const payload: UpdateUser = this.profileForm.value;

    this.userService.updateUser(payload).subscribe({
      next: () => {
        this.authService.logout();
        this.router.navigate(['/']);
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage =
          err.error?.message || 'Erreur lors de la mise à jour.';
      }
    });
  }
}
