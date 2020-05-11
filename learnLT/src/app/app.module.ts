import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { ReactiveFormsModule } from '@angular/forms';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {FormsModule} from '@angular/forms';

import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field'; 
import {MatInputModule} from '@angular/material/input';
import {MatGridListModule} from '@angular/material/grid-list'; 
import {MatDividerModule} from '@angular/material/divider';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';  
import {MatStepperModule} from '@angular/material/stepper'; 
import {MatButtonToggleModule} from '@angular/material/button-toggle'; 
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatDialogModule} from '@angular/material/dialog';  
import {MatTableModule} from '@angular/material/table';


import {MatExpansionModule} from '@angular/material/expansion'; 
import {MatBottomSheetModule} from '@angular/material/bottom-sheet'; 


import {MatListModule} from '@angular/material/list'; 




import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import { AuthGuard } from './auth.guard';
import { WelcomeComponent } from './welcome/welcome.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { DeclineComponent } from './decline/decline.component';
import { StressComponent } from './stress/stress.component';
import { BothComponent } from './both/both.component';
import { ProgressComponent } from './progress/progress.component';


import { FlexLayoutModule } from '@angular/flex-layout';
import { ExerciseComponent } from './exercise/exercise.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { LayoutModule } from '@angular/cdk/layout';
import { RecapComponent } from './recap/recap.component';
import { AlertComponent } from './alert/alert.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    WelcomeComponent,
    PageNotFoundComponent,
    DeclineComponent,
    StressComponent,
    BothComponent,
    ProgressComponent,
    ExerciseComponent,
    DashboardComponent,
    RecapComponent,
    AlertComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatGridListModule,
    MatDividerModule,
    MatSelectModule,
    MatButtonModule,
    MatCheckboxModule,
    MatStepperModule,
    MatButtonToggleModule,
    MatProgressBarModule,
    HttpClientModule,
    FlexLayoutModule,
    ReactiveFormsModule,
    MatMenuModule,
    MatIconModule,
    MatExpansionModule,
    MatBottomSheetModule,
    MatDialogModule,
    MatTableModule,
    MatListModule,
    LayoutModule
  ],
  providers: [AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
