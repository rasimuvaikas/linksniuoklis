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

  url:string;

  constructor(private http: HttpClient) { 
    this.url = "http://localhost:8080/Decliner/"
  }

  /*authenticate(username):Observable<any>{
    return this.http.post(this.url, username);
  }*/

  getCards(inflection="accusative", number="singular", declensions=["1masc"]):Observable<any>{

    let i = declensions[Math.floor(Math.random() * ((declensions.length - 1) - 0 + 1) + 0)];

    let httpParams = new HttpParams()
    .append("inflection", inflection)
    .append("number", number)
    .append("declension", i);

    
    return this.http.get(this.url + "Share", {params: httpParams, responseType: "text"});
  }

  /*sendData(result:Selected):Observable<any>{
    return this.http.post(this.url + "Evaluation", result)
  }*/

  /**
   * Post a sentence that was used in a successfully completed exercise to update the learner's progress table
   * @param sent  the sentence used in the exercise
   */
  postProgress(sent: any):Observable<any>{

    return this.http.post(this.url + "Share", sent);
  }



  getResults(username:string):Observable<any>{

    let nameParam = new HttpParams()
    .append("username", username);

    return this.http.get(this.url + "Learner", {params: nameParam, responseType: "text"});
  }

  postModel(model: Level[]):Observable<any>{
    return this.http.post(this.url + "Learner", model);
  }


  postScore(score: Score):Observable<any>{
    return this.http.post(this.url + "Score", score);

  }

  getScore(username: string, time: string):Observable<any>{
    let httpParams = new HttpParams()
    .append("username", username)
    .append("time", time);
    return this.http.get(this.url + "Score", {params: httpParams, responseType: "text"});

  }
  
}
