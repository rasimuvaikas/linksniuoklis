import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { FormsModule } from '@angular/forms';
import { UserInfoService } from '../user-info.service';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Level } from '../level';
import { ConnectService } from '../connect.service';
import { LearnerModelService } from '../learner-model.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;

  lmodel: Level[];
  


  constructor(private user: UserInfoService, private route: Router, private auth: AuthService, private conn: ConnectService, private model: LearnerModelService) {

    this.lmodel = [];
  

  }

  onSubmit(formContent) {
    this.username = formContent.username;
    this.user.sendName(this.username); //make username accessible to other components

    //check if user is using a username
    //if not, redirect them back to the start page
    //if yes, check if it is there is a learner model created for the user
    //if no, send user to welcome component to create a learner model
    //else, send them to dashboard
    if (this.auth.isUserLoggedIn(this.username)) {

      //check if the user has an existing learner model
      this.conn.getResults(this.username).subscribe(res => {
        
        console.log(JSON.parse(res));
      this.lmodel = JSON.parse(res); 
      console.log("gavau: ", JSON.parse(res));

        if (this.lmodel.length == 1 && this.lmodel[0].infl == null) {
          this.route.navigate(['welcome']);
          
        } else {
          console.log("jau buvo")
          this.model.sendModel(JSON.parse(res));
          this.route.navigate(['dashboard']);
        }
      });



    }
    else {
      this.route.navigate(['start']);
    }
  }

  ngOnInit() {
    this.user.currentName.subscribe(username => this.username = username)
    this.user.sendTime(new Date());
  }

}
