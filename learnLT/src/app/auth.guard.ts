import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
/**
 * Ensure the user is logged in to access other components
 */
export class AuthGuard implements CanActivate {
  constructor(private aS:AuthService, private router: Router)
  {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if(this.aS.isLoggedin() == true){
    return true;    
    }
    else{
      console.log("Cannot acces");
      this.router.navigate(['start']);
      return false;
    
    }
  }
  
}
