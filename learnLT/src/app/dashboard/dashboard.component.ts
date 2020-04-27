import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../user-info.service';
import { Level } from '../level';
import { LearnerModelService } from '../learner-model.service';
import { Router } from '@angular/router';
import { ConnectService } from '../connect.service';
import { RecapComponent } from '../recap/recap.component';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { MatDialog } from '@angular/material/dialog';
import { AlertComponent } from '../alert/alert.component';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  decls: any;
  infls: any;

  empty: boolean;

  time: Date;

  markSg: string;
  markPl: string;

  declMasc: string;
  declFem: string;

  lmodel: Level[];

  username: string;
  levels: Level[];
  inflectionsSg: { number: string; infl: string; checked: boolean; }[];
  inflectionsPl: { number: string; infl: string; checked: boolean; }[];
  advancedSg: { number: string; infl: string; checked: boolean; }[];
  advancedPl: { number: string; infl: string; checked: boolean; }[];
  familiarSg: { number: string; infl: string; checked: boolean; }[];
  familiarPl: { number: string; infl: string; checked: boolean; }[];

  declensionsMasc: any;
  declensionsFem: any;


  checked: boolean;

  //inflections in the order from most useful (proposed first) to least
  inflections: string[] = ["nominative", "genitive", "accusative", "locative", "instrumental", "dative"];

  //declensions in the order from most useful (proposed first) to least
  declensions: string[] = ["1mascIAS", "2femIA", "1mascIS", "2femĖ", "3masc", "3fem", "4masc", "5masc", "5fem"];


  inflectionsTempSg: { number: string; infl: string; checked: boolean; }[];
  inflectionsTempPl: { number: string; infl: string; checked: boolean; }[];

  constructor(private user: UserInfoService, private model: LearnerModelService, private route: Router, private con: ConnectService,
    private _bottomSheet: MatBottomSheet, private dialogue: MatDialog) {

    this.inflectionsSg = [{ number: "singular", infl: "nominative", checked: false },
    { number: "singular", infl: "genitive", checked: false },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.inflectionsPl = [{ number: "plural", infl: "nominative", checked: false },
    { number: "plural", infl: "genitive", checked: false },
    { number: "plural", infl: "dative", checked: false },
    { number: "plural", infl: "accusative", checked: false },
    { number: "plural", infl: "instrumental", checked: false },
    { number: "plural", infl: "locative", checked: false }];

    this.advancedSg = [{ number: "singular", infl: "nominative", checked: false },
    { number: "singular", infl: "genitive", checked: false },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.advancedPl = [{ number: "plural", infl: "nominative", checked: false },
    { number: "plural", infl: "genitive", checked: false },
    { number: "plural", infl: "dative", checked: false },
    { number: "plural", infl: "accusative", checked: false },
    { number: "plural", infl: "instrumental", checked: false },
    { number: "plural", infl: "locative", checked: false }];

    this.familiarSg = [{ number: "singular", infl: "nominative", checked: false },
    { number: "singular", infl: "genitive", checked: false },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.familiarPl = [{ number: "plural", infl: "nominative", checked: false },
    { number: "plural", infl: "genitive", checked: false },
    { number: "plural", infl: "dative", checked: false },
    { number: "plural", infl: "accusative", checked: false },
    { number: "plural", infl: "instrumental", checked: false },
    { number: "plural", infl: "locative", checked: false }];

    this.inflectionsTempSg = [{ number: "singular", infl: "nominative", checked: false },
    { number: "singular", infl: "genitive", checked: false },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.inflectionsTempPl = [{ number: "plural", infl: "nominative", checked: false },
    { number: "plural", infl: "genitive", checked: false },
    { number: "plural", infl: "dative", checked: false },
    { number: "plural", infl: "accusative", checked: false },
    { number: "plural", infl: "instrumental", checked: false },
    { number: "plural", infl: "locative", checked: false }];


    this.lmodel = [];


    this.declensionsMasc = [{ display: "-(i)as", declension: "1mascIAS", decl: "1st", checked: false },
    { display: "-is, -ys", declension: "1mascIS", decl: "1st", checked: false },
    { display: "-is", declension: "3masc", decl: "3rd", checked: false },
    { display: "-(i)us", declension: "4masc", decl: "4th", checked: false },
    { display: "-uo", declension: "5masc", decl: "5th", checked: false }
    ]

    this.declensionsFem = [{ display: "-(i)a", declension: "2femIA", decl: "2nd", checked: false },
    { display: "-ė", declension: "2femĖ", decl: "2nd", checked: false },
    { display: "-is", declension: "3fem", decl: "3rd", checked: false },
    { display: "sesuo, duktė", declension: "5fem", decl: "5th", checked: false },
    ]
  }

  /**
   * Alert box
   */
  openDialog(): void {
    const dialogRef = this.dialogue.open(AlertComponent, {
      width: '250px',
      data: "At least one declension type must be marked."
    });
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
  getDisplay(decl: string) {
    switch (decl) {
      case "1mascIAS": {
        return "-(i)as"
      }
      case "1mascIS": {
        return "-is, -ys"
      }
      case "3masc": {
        return "-is"
      }
      case "4masc": {
        return "-(i)us"
      }
      case "5masc": {
        return "-uo"
      }
      case "2femIA": {
        return "-(i)a"
      }
      case "2femĖ": {
        return "-ė"
      }
      case "3fem": {
        return "-is"
      }
      case "5fem": {
        return "sesuo, duktė"
      }
    }
  }

  /**
   * Find the equivalent declension number for a declension code
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
    }
  }


  /**
   * Mark all declensions at once
   */
  selectAll() {
    if (!this.checked) {
      this.declensionsFem.forEach(element => {
        element.checked = true;
      });

      this.declensionsMasc.forEach(element => {
        element.checked = true;
      });
    }
  }

  /**
   * Create two temporary arrays of inflections to be marked as checked
   * Determine which inflections should be proposed to the user to practice next
   */
  propose() {


    for (let i of this.familiarSg) {
      if (i.checked == true) {
        for (let j of this.inflectionsTempSg) {
          if (j.infl == i.infl) {
            j.checked = i.checked;
          }
        }
      }
    }

    for (let i of this.advancedSg) {
      if (i.checked == true) {
        for (let j of this.inflectionsTempSg) {
          if (j.infl == i.infl) {
            j.checked = i.checked;
          }
        }
      }
    }

    for (let i of this.familiarPl) {
      if (i.checked == true) {
        for (let j of this.inflectionsTempPl) {
          if (j.infl == i.infl) {
            j.checked = i.checked;
          }
        }
      }
    }

    for (let i of this.advancedPl) {
      if (i.checked == true) {
        for (let j of this.inflectionsTempPl) {
          if (j.infl == i.infl) {
            j.checked = i.checked;
          }
        }
      }
    }


    for (let i of this.inflectionsSg) {
      if (i.checked == true) {
        for (let j of this.inflectionsTempSg) {
          if (j.infl == i.infl) {
            j.checked = i.checked;
          }
        }
      }
    }

    for (let i of this.inflectionsPl) {
      if (i.checked == true) {
        for (let j of this.inflectionsTempPl) {
          if (j.infl == i.infl) {
            j.checked = i.checked;
          }
        }
      }
    }




    //determine which singular inflection should be proposed next 
    let foundSg: boolean = false;

    for (let i of this.inflections) {
      for (let j of this.inflectionsTempSg) {
        if (i == j.infl && !j.checked) {
          foundSg = true;
          this.markSg = i;
        }
      }
      if (foundSg) {
        break;
      }
    }

    //determine which plural inflection should be proposed next 
    let foundPl: boolean = false;
    for (let i of this.inflections) {
      for (let j of this.inflectionsTempPl) {
        if (i == j.infl && !j.checked) {
          foundPl = true;
          this.markPl = i;
        }
      }
      if (foundPl) {
        break;
      }
    }


    //determine which masculine declension should be proposed next 
    let dSg: boolean = false;

    for (let i of this.declensions) {
      for (let j of this.declensionsMasc) {
        if (i == j.declension && !j.checked) {
          dSg = true;
          this.declMasc = i;
        }
      }
      if (dSg) {
        break;
      }
    }

    //determine which feminine declension should be proposed next 
    let dPl: boolean = false;
    for (let i of this.declensions) {
      for (let j of this.declensionsFem) {
        if (i == j.declension && !j.checked) {
          dPl = true;
          this.declFem = i;
        }
      }
      if (dPl) {
        break;
      }
    }


  }

  /**
 * Mark inflections selected as novel to the user
 * @param infl inflection
 * @param num number
 */
  onChangeNov(infl: string, num: string) {

    if (num == 'singular') {
      for (let j of this.inflectionsSg) {
        if (j.infl == infl) {
          if (j.checked == true) {
            j.checked = false;
          } else {
            j.checked = true;
          }
        }
      }
    }

    else {
      for (let j of this.inflectionsPl) {
        if (j.infl == infl) {
          if (j.checked == true) {
            j.checked = false;
          } else {
            j.checked = true;
          }
        }
      }
    }

  }



  /**
   * Check if the user has made any changes to the dashboard, change learner model if so, move to the exercise component
   */
  onSubmit() {
    let decls: string[] = [];

    this.declensionsFem.forEach(element => {
      if (element.checked) {
        decls.push(element.declension)
      }
    });

    this.declensionsMasc.forEach(element => {
      if (element.checked) {
        decls.push(element.declension)
      }
    });

    if (decls.length == 0) {
      this.openDialog();
    }


    else {

      for (let i of this.advancedSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "advanced", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      for (let i of this.advancedPl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "advanced", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      for (let i of this.familiarSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "familiar", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      for (let i of this.familiarPl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "familiar", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      for (let i of this.inflectionsSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "novel", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      for (let i of this.inflectionsPl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "novel", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      this.model.sendModel(this.lmodel);

      this.route.navigate(['exercise'])
    }
  }


  ngOnInit(): void {



    this.user.currentName.subscribe(username => this.username = username); //get username
    this.user.currenttime.subscribe(t => this.time = t); //get the time of the current session
    this.model.lvls.subscribe(lvls => this.levels = lvls); //get learner model


    //get user's results from previous sessions
    this.con.getScore(this.username, this.time.toString()).subscribe(data => {
      console.log(JSON.parse(data)); let response = JSON.parse(data);
      this.decls = response['declension']; this.infls = response['inflections'];
      if (this.decls == null && this.infls == null) {
        this.empty = true;
      }
    });

    //mark all the checkboxes that are part of the learner model
    for (let i of this.levels) {
      if (i.level == "novel") {
        if (i.number == "singular") {
          for (let j of this.inflectionsSg) {
            if (j.infl == i.infl) {
              j.checked = true;
            }
          }
        } else {
          for (let j of this.inflectionsPl) {
            if (j.infl == i.infl) {
              j.checked = true;
            }
          }
        }
      } else if (i.level == "familiar") {
        if (i.number == "singular") {
          for (let j of this.familiarSg) {
            if (j.infl == i.infl) {
              j.checked = true;
            }
          }
        } else {
          for (let j of this.familiarPl) {
            if (j.infl == i.infl) {
              j.checked = true;
            }
          }
        }
      } else if (i.level == "advanced") {
        if (i.number == "singular") {
          for (let j of this.advancedSg) {
            if (j.infl == i.infl) {
              j.checked = true;
            }
          }
        } else {
          for (let j of this.advancedPl) {
            if (j.infl == i.infl) {
              j.checked = true;
            }
          }
        }
      }
    }

    for (let i of this.levels) {
      for (let j of i.declensions) {
        this.declensionsFem.forEach(element => {
          if (element.declension == j) {
            element.checked = true;
          }
        });

        this.declensionsMasc.forEach(element => {
          if (element.declension == j) {
            element.checked = true;
          }
        });

      }
    }

    //propose new inflection & declensions to practice
    this.propose();
  }

}
