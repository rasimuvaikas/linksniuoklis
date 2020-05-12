import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Level } from './level';
import { Selected } from './selected';
import { UserInfoService } from './user-info.service';
import { Score } from './score';

@Injectable({
  providedIn: 'root'
})
export class ConnectService {

  url: string;

  constructor(private http: HttpClient) {
    this.url = "http://localhost:8080/Decliner/"
  }

/**
 * Retrieve a sentence with a noun of a specific inflection, number and declension
 * @param inflection noun's case
 * @param number noun's number
 * @param declensions all the declensions the user is currently practising
 */
  getCard(inflection = "accusative", number = "singular", declensions = ["1masc"]): Observable<any> {

    //choose a random declension
    let i = declensions[Math.floor(Math.random() * ((declensions.length - 1) - 0 + 1) + 0)]; 

    let httpParams = new HttpParams()
      .append("inflection", inflection)
      .append("number", number)
      .append("declension", i);

      console.log("connect sent a request with ", i);


    return this.http.get(this.url + "Share", { params: httpParams, responseType: "text" });
  }


  /**
   * Post a sentence that was used in a successfully completed exercise to update the learner's progress table
   * @param sent  the sentence used in the exercise
   */
  postProgress(sent: any): Observable<any> {

    return this.http.post(this.url + "Share", sent);
  }


  /**
   * Retrieve the learner model of a particular user
   * @param username 
   */
  getModel(username: string): Observable<any> {

    let nameParam = new HttpParams()
      .append("username", username);

    return this.http.get(this.url + "Learner", { params: nameParam, responseType: "text" });
  }

  /**
   * Update a particular user's learner model
   * @param model 
   */
  postModel(model: Level[]): Observable<any> {
    return this.http.post(this.url + "Learner", model);
  }


  /**
   * Update the scores table with a new score for a particular inflection/declension for the current learning session
   * @param score includes the username, the time of the current session, noun's inflection, number, declension, and whether the answer was correct or incorrect
   */
  postScore(score: Score): Observable<any> {
    return this.http.post(this.url + "Score", score);

  }

  /**
   * Get user's results achieved during the previous sessions
   * @param username username
   * @param time time of the current session
   */
  getScore(username: string, time: string): Observable<any> {
    let httpParams = new HttpParams()
      .append("username", username)
      .append("time", time);
    return this.http.get(this.url + "Score", { params: httpParams, responseType: "text" });

  }

}
