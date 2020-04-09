import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isLoggedIn:boolean;

  constructor() {

    this.isLoggedIn = false;
   }

  /*logIn(name){
    
    return this.http.post('localhost:8080/javax.servlet-api/Assignment', {"username":name});
  }*/

  isUserLoggedIn(username)
  {
    if(username != null || username != '')
    {
      this.isLoggedIn = true;
      return true;
    }
    else{
      return false;
    }
  }

  isLoggedin()
  {
    return this.isLoggedIn;
  }
}
