import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Level } from './level';

@Injectable({
  providedIn: 'root'
})

/**
 * Make learner model accessible for all components,
 * as well as variables that track the frequency of familiar and advanced case sentences,
 * and the score of the progress bar
 */
export class LearnerModelService {

  private subject = new BehaviorSubject<any>("default");
  lvls = this.subject.asObservable();

  private counterAdv = new BehaviorSubject<any>(0);
  cAdv = this.counterAdv.asObservable();

  private counterFam = new BehaviorSubject<any>(0);
  cFam = this.counterFam.asObservable();

  private scoreProgress = new BehaviorSubject<any>(0);
  sc = this.scoreProgress.asObservable();

  private scoreTotal = new BehaviorSubject<any>({correct: 0, incorrect: 0});
  scTotal = this.scoreTotal.asObservable();

  constructor() { }

  sendModel(lvl: Level[]) {
    this.subject.next(lvl);
  }

  sendAdv(currNum: number) {
    this.counterAdv.next(currNum);
  }

  sendFam(currNum: number) {
    this.counterFam.next(currNum);
  }

  sendScore(currScore: number){
    this.scoreProgress.next(currScore);
  }

  sendOverallScore(currScore: any){
    this.scoreTotal.next(currScore);
  }
}
