import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
/**
 * Keep track of user's info
 */
export class UserInfoService {

  private subject = new BehaviorSubject<any>("default");
  currentName = this.subject.asObservable();

  private time = new BehaviorSubject<any>("default");
  currenttime = this.time.asObservable();

  constructor() { }

  sendName(name: string) {
    this.subject.next(name);
}

/**
 * Keep track of when the user logged in
 * @param t the time the user logged in
 */
sendTime(t: Date){
  this.time.next(t);
}


}
