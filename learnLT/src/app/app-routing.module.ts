import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { AuthGuard } from './auth.guard';
import { DeclineComponent } from './decline/decline.component';
import { StressComponent } from './stress/stress.component';
import { BothComponent } from './both/both.component';
import { ProgressComponent } from './progress/progress.component';
import { ExerciseComponent } from './exercise/exercise.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RecapComponent } from './recap/recap.component';



const routes: Routes = [
  {path: 'start', component: LoginComponent},
  {path: 'welcome',  canActivate: [AuthGuard], component: WelcomeComponent},
  {path: 'exercise', canActivate: [AuthGuard], component: ExerciseComponent},
  {path: 'decline', canActivate: [AuthGuard], component: DeclineComponent},
  {path: 'stress', canActivate: [AuthGuard], component: StressComponent},
  {path: 'both', canActivate: [AuthGuard], component: BothComponent},
  {path: 'dashboard', canActivate: [AuthGuard], component: DashboardComponent},
  {path: 'progress', canActivate: [AuthGuard], component: ProgressComponent},  
  {path: '', redirectTo: '/start', pathMatch: 'full'},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
