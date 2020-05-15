import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Inflection } from '../inflection';
import { ConnectService } from '../connect.service';
import { Selected } from '../selected';
import { Level } from '../level';
import { FormControl } from '@angular/forms';
import { LearnerModelService } from '../learner-model.service';
import { UserInfoService } from '../user-info.service';
import { Advance } from '../advance';
import { Score } from '../score';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { RecapComponent } from '../recap/recap.component';

@Component({
  selector: 'app-decline',
  templateUrl: './decline.component.html',
  styleUrls: ['./decline.component.css']
})

/**
 * A component that displays cards with exercises to practice declension
 */
export class DeclineComponent implements OnInit {

  username: string;
  progress: Advance;

  time: Date;

  lmodel: Level[];

  levels: Level[];
  card: Selected;

  correct: number;

  displayEng: boolean;
  displayAnswer: boolean;
  displayInfo: boolean;
  show: boolean;

  fib: boolean;
  mult: boolean;

  answer: string = "";

  //a variable to count the number of incorrect answers in a row
  count: number = 0;

  score: number;
  update: { correct: number, incorrect: number }

  advanced: Level[];
  intermediate: Level[];
  beginner: Level[];

  counterFam: number
  counterAdv: number


  answered: boolean;



  constructor(private model: LearnerModelService, private route: Router, private con: ConnectService, private user: UserInfoService,
    private _bottomSheet: MatBottomSheet) {


    this.user.currenttime.subscribe(t => this.time = t);

    this.advanced = [];
    this.intermediate = [];
    this.beginner = [];

    this.model.sendFam(0);
    this.model.sendAdv(0);

  }

  /**
   * Recap bottom sheet
   */
  openBottomSheet(): void {
    this._bottomSheet.open(RecapComponent);
  }

  /**
   * Find the equivalent 'display' option for a declension code
   * @param decl declension code
   */
  getDecl(decl: string) {
    switch (decl) {
      case "1mascIAS": {
        return "1st"
      }
      case "1mascIS": {
        return "1st"
      }
      case "3masc": {
        return "3rd"
      }
      case "4masc": {
        return "4th"
      }
      case "5masc": {
        return "5th"
      }
      case "2femIA": {
        return "2nd"
      }
      case "2femĖ": {
        return "2nd"
      }
      case "3fem": {
        return "3rd"
      }
      case "5fem": {
        return "5th"
      }
      case "exception": {
        return "exception"
      }
    }
  }


  /**
   * Assign value to an element.
   * Return 1 if the element equals the noun, 0 if not.
   * @param entry the current element
   * @param noun the noun it should match
   */
  assign(entry: string, noun: string) {
    if (entry == noun) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * Check is selected noun form is correct
   * If answer is correct, update the progress bar score, 
   * set answered to true to display the stressed version of the correct noun before updating the card to a new one
   * If answer is incorrect, update the progress bar score
   */
  onChange(sentence: any) {
    if (this.correct == 1) {
      this.answer = ""; //reset answer
      this.count = 0; //reset the counter
      this.displayAnswer = false; //reset answer display boolean

      this.score = this.score + 1;
      this.answered = true;

      this.advance(sentence);

      this.model.sendScore(this.score);

      var send: Score = {
        username: this.username, time: this.time.toDateString() + " " + this.time.toLocaleTimeString(),
        inflection: sentence.inflection, number: sentence.number, declension: sentence.declension, correct: 1, incorrect: 0
      };
      this.con.postScore(send).subscribe(data => { this.update = data; this.model.sendOverallScore(this.update) });
      setTimeout(() => { this.ngOnInit(); }, 1000);
    }

    else {
      this.score = 0;
      this.model.sendScore(this.score);
      var send: Score = {
        username: this.username, time: this.time.toDateString() + " " + this.time.toLocaleTimeString(),
        inflection: sentence.inflection, number: sentence.number, declension: sentence.declension, correct: 0, incorrect: 1
      };
      this.con.postScore(send).subscribe(data => { this.update = data; this.model.sendOverallScore(this.update) });
    }
  }

  /**
   * Advance the learner through levels if they have completed a certain amount of exercises
   * @param sentence the sentence used in the exercise
   */
  advance(sentence: any) {

    sentence.username = this.username;
    let level: string;
    let declensions: string[];
    this.levels.forEach(el => {
      if (el.infl == sentence.inflection && el.number == sentence.number) {
        level = el.level;
        declensions = el.declensions;
      }
    })

    sentence.level = level;

    this.con.postProgress(sentence).subscribe(data => {
      this.progress = data;
      console.log(data);

      //the user can move to the next level if they have completed a certain number of exercises
      if (level == "beginner") {
        if (this.progress.total >= 30) { 
          if (this.progress.declensions.length > 4) {
            let completed = true;//check if the user has completed at least 5 exercises for each declension, if there are 5 or more sentences in that declension group
            for (let i = 0; i < this.progress.declensions.length; i++) {
                if (this.progress.declensions[i]["count"] >= 5) { 
                  if(Object.keys(this.progress.declensions[i]).indexOf("count") == 0){ //find the index of the count key to get the index of the key of the number of exercises completed
                    if(this.progress.declensions[i][1] < 5){ 
                      completed = false;
                    }
                  }
                  else{
                    if(this.progress.declensions[i][0] < 5){
                      completed = false;
                    }
                  }
                }
                else{//there are less than 5 sentences in a declension group. check if the user has completed exercises for the few sentences that exist
                  if(Object.keys(this.progress.declensions[i]).indexOf("count") == 0){
                    if(this.progress.declensions[i][1] < this.progress.declensions[i]["count"]){
                      completed = false;
                    }
                  }
                  else{
                    if(this.progress.declensions[i][0] < this.progress.declensions[i]["count"]){
                      completed = false;
                    }
                  }
                }
            }

            //if all conditions are satisfied, move the user up a level for the specific inflection
            if (completed) {
              let lvls: Level[] = [];

              level = "intermediate";

              let lvl: Level = { number: sentence.number, level: level, infl: sentence.inflection, username: this.username, declensions: declensions }
              lvls.push(lvl);
              //update the learner model: both in the database, and service
              this.con.postModel(lvls).subscribe(data => { this.lmodel = data; this.model.sendModel(this.lmodel); console.log(this.lmodel) });

            }
          }
        }
      }
      else if (level == "intermediate") {
        if (this.progress.total >= 30) {
          if (this.progress.declensions.length >= this.progress.total_declensions) {
            let completed = true;

            for (let i = 0; i < this.progress.declensions.length; i++) {
              if (this.progress.declensions[i]["count"] >= 5) { 
                if(Object.keys(this.progress.declensions[i]).indexOf("count") == 0){ //find the index of the count key to get the index of the key of the number of exercises completed
                  if(this.progress.declensions[i][1] < 5){ 
                    completed = false;
                  }
                }
                else{
                  if(this.progress.declensions[i][0] < 5){
                    completed = false;
                  }
                }
              }
              else{//there are less than 5 sentences in a declension group. check if the user has completed exercises for the few sentences that exist
                if(Object.keys(this.progress.declensions[i]).indexOf("count") == 0){
                  if(this.progress.declensions[i][1] < this.progress.declensions[i]["count"]){
                    completed = false;
                  }
                }
                else{
                  if(this.progress.declensions[i][0] < this.progress.declensions[i]["count"]){
                    completed = false;
                  }
                }
              }
            }


            //if all conditions are satisfied, move the user up a level for the specific inflection
            if (completed) {
              let lvls: Level[] = [];

              level = "advanced";

              let lvl: Level = { number: sentence.number, level: level, infl: sentence.inflection, username: this.username, declensions: declensions }
              lvls.push(lvl);
              //update the learner model: both in the database, and service
              this.con.postModel(lvls).subscribe(data => { this.lmodel = data; this.model.sendModel(this.lmodel); console.log(this.lmodel) });
            }
          }
        }
      }
    })

  }

  /**
   * Check if typed in noun form is correct
   * If answer is correct, update the progress bar score, 
   * set answered to true to display the stressed version of the correct noun before updating the card to a new one
   * If answer is incorrect, update the progress bar score
   * @param noun the correct noun form
   */
  onSubmit(noun: string, sentence: any) {
    if (noun.toLowerCase() == this.answer.toLowerCase()) {

      this.answer = ""; //reset answer
      this.correct = 1; //set correct to 1 to display "correct" message
      this.count = 0; //resent the counter
      this.displayAnswer = false; //reset answer display boolean
      this.score = this.score + 1;
      this.answered = true;
      this.model.sendScore(this.score);
      this.advance(sentence);

      var send: Score = {
        username: this.username, time: this.time.toDateString() + " " + this.time.toLocaleTimeString(),
        inflection: sentence.inflection, number: sentence.number, declension: sentence.declension, correct: 1, incorrect: 0
      };
      this.con.postScore(send).subscribe(data => { this.update = data; this.model.sendOverallScore(this.update) });


      setTimeout(() => { this.ngOnInit(); }, 1000);
    } else {
      this.score = 0;
      this.model.sendScore(this.score);

      var send: Score = {
        username: this.username, time: this.time.toDateString() + " " + this.time.toLocaleTimeString(),
        inflection: sentence.inflection, number: sentence.number, declension: sentence.declension, correct: 0, incorrect: 1
      };
      this.con.postScore(send).subscribe(data => { this.update = data; this.model.sendOverallScore(this.update) });

      this.count = this.count + 1;
      console.log(this.count);
      this.correct = 0;  //set correct to 0 to display "incorrect" message
      if (this.count > 2) {
        this.displayAnswer = true; //display the correct answer if the user fails to enter the correct one 3 times
      }
    }
  }


  ngOnInit(): void {

    //int score is used to update a progress bar in the progress component
    this.model.sc.subscribe(data => this.score = data);
    //int update is used to update a progress bar in the progress component
    this.model.scTotal.subscribe(data => this.update = data);


    this.user.currentName.subscribe(username => this.username = username); //get username

    this.answered = false;
    this.show = false;

    this.correct = 3; //reset correct so that neither 'correct' nor 'incorrect' would be displayed

    //get the learner model and group it into arrays of different levels
    this.model.lvls.subscribe(lvl => {
      this.levels = lvl;
      for (let i of this.levels) {
        if (i.level == "advanced") {
          this.advanced.push(i);
        } else if (i.level == "intermediate") {
          this.intermediate.push(i);
        } else if (i.level == "beginner") {
          this.beginner.push(i);
        }
      }
    });

    //get variables that get incremented to keep track of when intermediate or advanced level inflection noun should be displayed
    this.model.cFam.subscribe(count => this.counterFam = count); //intermediate
    this.model.cAdv.subscribe(count => this.counterAdv = count); //advanced


    if (this.counterFam > 2 && this.intermediate.length > 0) { //display a sentence with a noun in a intermediate level case every 3rd sentence

      let i = this.intermediate[Math.floor(Math.random() * ((this.intermediate.length - 1) - 0 + 1) + 0)]; //choose a random case that belongs to the intermediate category
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        if(this.card.simple == null && this.intermediate.length > 1){
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
          this.ngOnInit();
        } 
        else{
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
        } 
        console.log(JSON.parse(car))
      });

      console.log("fam: ", i);


    } else if (this.counterAdv > 4 && this.advanced.length > 0) { //display a sentence with a noun in an advanced level case every 5th sentence

      let i = this.advanced[Math.floor(Math.random() * ((this.advanced.length - 1) - 0 + 1) + 0)]; //choose a random case that belongs to the advanced category
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        if(this.card.simple == null && this.advanced.length > 1){
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(0); //reset the variable to start counting from 0 again
          this.ngOnInit();
        }
        else{
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(0); //reset the variable to start counting from 0 again
        } 
        console.log(JSON.parse(car))
      });

      console.log("adv: ", i);

    }

    else if (this.beginner.length > 0) {
      let i = this.beginner[Math.floor(Math.random() * ((this.beginner.length - 1) - 0 + 1) + 0)]; //choose a random case that belongs to the beginner category
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        if(this.card.simple == null && this.beginner.length > 1){
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(this.counterAdv + 1);
          this.ngOnInit();
        }
        else{
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(this.counterAdv + 1);
        }   
        console.log(JSON.parse(car))
      });

      console.log("nov: ", i);
    }

    //if beginner is empty, try other arrays
    else if (this.intermediate.length > 0) {
      let i = this.intermediate[Math.floor(Math.random() * ((this.intermediate.length - 1) - 0 + 1) + 0)];
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        if(this.card.simple == null && this.intermediate.length > 1){
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
          this.ngOnInit();
        }
        else{
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
        }   
        console.log(JSON.parse(car))
      });
      console.log("fam: ", i);
    }

    else if (this.advanced.length > 0) {
      let i = this.advanced[Math.floor(Math.random() * ((this.advanced.length - 1) - 0 + 1) + 0)];
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        if(this.card.simple == null && this.advanced.length > 1){
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.model.sendFam(this.counterFam + 1); //reset the variable to start counting from 0 again
          this.model.sendAdv(0);
          this.ngOnInit();
        }
        else{
          this.model.sendFam(this.counterFam + 1); //reset the variable to start counting from 0 again
          this.model.sendAdv(0);
        }   
        console.log(JSON.parse(car))
      });

      console.log("adv: ", i);
    }




  }
}
