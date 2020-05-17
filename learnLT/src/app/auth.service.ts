import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
/**
 * Ensure user is logged in
 */
export class AuthService {

  private isLoggedIn:boolean;

  constructor() {

    this.isLoggedIn = false;
   }

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
