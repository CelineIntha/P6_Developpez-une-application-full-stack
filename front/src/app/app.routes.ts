import {Routes} from '@angular/router';
import {HomeComponent} from './features/home/home.component';
import {LoginComponent} from "./features/auth/login/login.component";
import {RegisterComponent} from "./features/auth/register/register.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {DashboardComponent} from "./features/dashboard/dashboard.component";
import {NotFoundComponent} from "./features/not-found/not-found.component";
import {guestGuard} from "./core/guards/guest.guard";
import {CreateArticleComponent} from "./features/articles/create-article/create-article.component";
import {ArticleDetailComponent} from "./features/articles/article-detail/article-detail.component";

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [guestGuard],
  },
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'articles/create', component: CreateArticleComponent, canActivate: [AuthGuard]},
  {path: 'articles/:id', component: ArticleDetailComponent, canActivate: [AuthGuard]},
  // {
  //   path: 'articles/create',
  //   loadComponent: () => import('./features/articles/create-article/create-article.component')
  //     .then(m => m.CreateArticleComponent),
  //   canActivate: [AuthGuard]
  // },

  {path: '**', component: NotFoundComponent}
];
