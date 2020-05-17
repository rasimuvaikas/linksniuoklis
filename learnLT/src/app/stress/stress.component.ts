import { Component, OnInit } from '@angular/core';
import { Selected } from '../selected';
import { Router } from '@angular/router';
import { ConnectService } from '../connect.service';
import { Level } from '../level';
import { LearnerModelService } from '../learner-model.service';
import { Advance } from '../advance';
import { UserInfoService } from '../user-info.service';
import { Score } from '../score';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { RecapComponent } from '../recap/recap.component';

@Component({
  selector: 'app-stress',
  templateUrl: './stress.component.html',
  styleUrls: ['./stress.component.css']
})
/**
 * A component that allows a user to practise accentuation
 */
export class StressComponent implements OnInit {

  username: string;
  progress: Advance;
  lmodel: Level[];

  levels: Level[];
  card: Selected;

  correct: number;

  displayEng: boolean;
  displayInfo: boolean;

  score: number;
  update: { correct: number, incorrect: number }

  time: Date;

  advanced: Level[];
  intermediate: Level[];
  beginner: Level[];

  counterFam: number
  counterAdv: number

  attempts: number

  constructor(private model: LearnerModelService, private route: Router, private con: ConnectService, private user: UserInfoService,
    private _bottomSheet: MatBottomSheet) {

    this.user.currenttime.subscribe(t => this.time = t);

    this.advanced = [];
    this.intermediate = [];
    this.beginner = [];

    this.model.sendFam(0);
    this.model.sendAdv(0);
    
    this.attempts = 0
  }


  /**
   * Recap bottom sheet
   */
  openBottomSheet(): void {
    this._bottomSheet.open(RecapComponent);
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
 * Advance the learner through levels if they have completed a certain amount of exercises
 * @param sentence the sentence used in the exercise
 */
advance(sentence: any) {

  //get sentence info
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

  //update the progress table
  this.con.postProgress(sentence).subscribe(data => {
    this.progress = data;

    //the user can move to the next level if they have completed a certain number of exercises
    if (level == "beginner") {
      if (this.progress.total >= 30) {
        if (this.progress.declensions.length > 4 || this.progress.declensions.length == this.progress.total_declensions) { //some inflection do not contain nouns in 4 declensions
          let advance = false
          let decls = 0
          for (let i = 0; i < this.progress.declensions.length; i++) {
            for (var j in this.progress.declensions[i]) {
              if (j != 'count') {
                if (this.progress.declensions[i][j] >= 5) {
                  decls = decls + 1
                  if (decls == 5 || decls == this.progress.total_declensions) { //check if the user has completed at least 5 exercises for each declensions
                    advance = true
                    break
                  }
                }
              }
            }

          }

          //if all conditions are satisfied, move the user up a level for the specific inflection
          if (advance) {
            let lvls: Level[] = [];

            level = "intermediate";

            let lvl: Level = { number: sentence.number, level: level, infl: sentence.inflection, username: this.username, declensions: declensions }
            lvls.push(lvl);
            //update the learner model: both in the database, and service
            this.con.postModel(lvls).subscribe(data => { this.lmodel = data; this.model.sendModel(this.lmodel); });

          }
        }
      }
    }
    else if (level == "intermediate") {
      if (this.progress.total >= 30) {
        let advance = true
        if (this.progress.declensions.length >= this.progress.total_declensions) {
          for (let i = 0; i < this.progress.declensions.length; i++) {
            for (var j in this.progress.declensions[i]) {
              if (j != 'count') {
                if (this.progress.declensions[i][j] < 5) {
                  advance = false
                  break
                }
              }
            }
          }


          //if all conditions are satisfied, move the user up a level for the specific inflection
          if (advance) {
            let lvls: Level[] = [];

            level = "advanced";

            let lvl: Level = { number: sentence.number, level: level, infl: sentence.inflection, username: this.username, declensions: declensions }
            lvls.push(lvl);
            //update the learner model: both in the database, and service
            this.con.postModel(lvls).subscribe(data => { this.lmodel = data; this.model.sendModel(this.lmodel);});
          }
        }
      }
    }
  })

}


  /**
   * Check is selected noun form is correct
   * If answer is correct, update the progress bar score and load a new card
   * If answer is incorrect, update the progress bar score
   */
  onChange(sentence: any) {
    if (this.correct == 1) {
      this.score = this.score + 1;
      this.advance(sentence);
      this.model.sendScore(this.score);

      //update score table and progress bar
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

      //update score table and progress bar
      var send: Score = {
        username: this.username, time: this.time.toDateString() + " " + this.time.toLocaleTimeString(),
        inflection: sentence.inflection, number: sentence.number, declension: sentence.declension, correct: 0, incorrect: 1
      };
      this.con.postScore(send).subscribe(data => { this.update = data; this.model.sendOverallScore(this.update) });
    }
  }

  ngOnInit(): void {

    //int score is used to update a progress bar in the progress component
    this.model.sc.subscribe(data => this.score = data);
    //int update is used to update a progress bar in the progress component
    this.model.scTotal.subscribe(data => this.update = data);

    this.user.currentName.subscribe(username => this.username = username); //get username

    this.correct = 3; //reset correct so that neither 'correct' nor 'incorrect' would be displayed

    //get learner model and divide all inflections into levels
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
        //add attempts counter to prevent infinite loops
        if (this.card.simple == null && (this.intermediate.length > 1 || i.declensions.length > 1) && this.attempts < 4) {
          this.attempts = this.attempts + 1
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.attempts = 0
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
          this.ngOnInit();
        }
        else {
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
        }
      });


    } else if (this.counterAdv > 4 && this.advanced.length > 0) { //display a sentence with a noun in an advanced level case every 5th sentence

      let i = this.advanced[Math.floor(Math.random() * ((this.advanced.length - 1) - 0 + 1) + 0)]; //choose a random case that belongs to the advanced category
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        //add attempts counter to prevent infinite loops
        if (this.card.simple == null && (this.advanced.length > 1 || i.declensions.length > 1) && this.attempts < 4) {
          this.attempts = this.attempts + 1
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.attempts = 0
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(0); //reset the variable to start counting from 0 again
          this.ngOnInit();
        }
        else {
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(0); //reset the variable to start counting from 0 again
        }
      });

    }

    else if (this.beginner.length > 0) {
      let i = this.beginner[Math.floor(Math.random() * ((this.beginner.length - 1) - 0 + 1) + 0)]; //choose a random case that belongs to the beginner category
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        //add attempts counter to prevent infinite loops
        if (this.card.simple == null && (this.beginner.length > 1 || i.declensions.length > 1)  && this.attempts < 4) {
          this.attempts = this.attempts + 1
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.attempts = 0
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(this.counterAdv + 1);
          this.ngOnInit();
        }
        else {
          this.model.sendFam(this.counterFam + 1);
          this.model.sendAdv(this.counterAdv + 1);
        }
      });
    }

    //if beginner is empty, try other arrays
    else if (this.intermediate.length > 0) {
      let i = this.intermediate[Math.floor(Math.random() * ((this.intermediate.length - 1) - 0 + 1) + 0)];
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        //add attempts counter to prevent infinite loops
        if (this.card.simple == null && (this.intermediate.length > 1 || i.declensions.length > 1) && this.attempts < 4) {
          this.attempts = this.attempts + 1
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.attempts = 0
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
          this.ngOnInit();
        }
        else {
          this.model.sendFam(0); //reset the variable to start counting from 0 again
          this.model.sendAdv(this.counterAdv + 1);
        }
      });
    }

    else if (this.advanced.length > 0) {
      let i = this.advanced[Math.floor(Math.random() * ((this.advanced.length - 1) - 0 + 1) + 0)];
      this.con.getCard(i.infl, i.number, i.declensions).subscribe(car => {
        this.card = JSON.parse(car);
        //add attempts counter to prevent infinite loops
        if (this.card.simple == null && (this.advanced.length > 1 || i.declensions.length > 1) && this.attempts < 4) {
          this.attempts = this.attempts + 1
          this.ngOnInit();
        }
        else if (this.card.simple == null) {
          this.attempts = 0
          this.model.sendFam(this.counterFam + 1); //reset the variable to start counting from 0 again
          this.model.sendAdv(0);
          this.ngOnInit();
        }
        else {
          this.model.sendFam(this.counterFam + 1); //reset the variable to start counting from 0 again
          this.model.sendAdv(0);
        }
      });
    }



  }

}
