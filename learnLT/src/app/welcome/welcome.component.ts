import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../user-info.service';
import { Inflection } from '../inflection';
import { Router } from '@angular/router';
import { LearnerModelService } from '../learner-model.service';
import { Level } from '../level';
import { ConnectService } from '../connect.service';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { RecapComponent } from '../recap/recap.component';
import { MatDialog } from '@angular/material/dialog';
import { AlertComponent } from '../alert/alert.component';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})

/**
 * A component that helps create a learner model
 */
export class WelcomeComponent implements OnInit {

  username: any;

  lmodel: Level[];

  //šitą turbūt reiks ištrint kaip ir getResult iš ngoninit
  userModel: Level[];

  inflectionsSg: Inflection[];
  inflectionsPl: Inflection[];

  advancedSg: Inflection[];
  advancedPl: Inflection[];

  familiarSg: Inflection[];
  familiarPl: Inflection[];

  inflectionsNoviceSg: Inflection[];
  inflectionsNovicePl: Inflection[];

  inflectionsTempSg: Inflection[];
  inflectionsTempPl: Inflection[];



  beginner: boolean;
  advanced: boolean;


  //inflections in the order from most useful (proposed first) to least
  inflections: string[] = ["nominative", "genitive", "accusative", "locative", "instrumental", "dative"];


  declensionsMasc: any;
  declensionsFem: any;
  checked: boolean;

  //missing exception words




  constructor(private user: UserInfoService, private route: Router, private model: LearnerModelService, private connect: ConnectService,
    private _bottomSheet: MatBottomSheet, private dialogue: MatDialog) {
    this.inflectionsSg = [{ number: "singular", infl: "nominative", checked: true },
    { number: "singular", infl: "genitive", checked: true },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.inflectionsPl = [{ number: "plural", infl: "nominative", checked: true },
    { number: "plural", infl: "genitive", checked: true },
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

    this.inflectionsNoviceSg = [{ number: "singular", infl: "nominative", checked: false },
    { number: "singular", infl: "genitive", checked: false },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.inflectionsNovicePl = [{ number: "plural", infl: "nominative", checked: false },
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


    this.declensionsMasc = [{ display: "-(i)as", declension: "1mascIAS", decl: "1st", checked: true },
    { display: "-is, -ys", declension: "1mascIS", decl: "1st", checked: false },
    { display: "-is", declension: "3masc", decl: "3rd", checked: false },
    { display: "-(i)us", declension: "4masc", decl: "4th", checked: false },
    { display: "-uo", declension: "5masc", decl: "5th", checked: false }
    ]

    this.declensionsFem = [{ display: "-(i)a", declension: "2femIA", decl: "2nd", checked: true },
    { display: "-ė", declension: "2femĖ", decl: "2nd", checked: true },
    { display: "-is", declension: "3fem", decl: "3rd", checked: false },
    { display: "sesuo, duktė", declension: "5fem", decl: "5th", checked: false },
    ]

    //missing exception words

  }

  openDialog(d: string): void {
    const dialogRef = this.dialogue.open(AlertComponent, {
      width: '250px',
      data: d
    });
  }


  openBottomSheet(): void {
    this._bottomSheet.open(RecapComponent);
  }

  /**
   * Mark inflections selected as familiar 
   * @param infl the selected inflection
   * @param num the selected number
   */
  onChangeFam(infl: string, num: string) {

    if (num == 'singular') {
      for (let j of this.familiarSg) {
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
      for (let j of this.familiarPl) {
        if (j.infl == infl) {
          if (j.checked == true) {
            j.checked = false;
          } else {
            j.checked = true;
          }
        }
      }
    }


    console.log(JSON.stringify(this.familiarSg)); //to be deleted

  }



  /**
   * Create two temporary arrays of inflections to be marked as checked
   * Determine which inflections should be proposed to the user to practice next
   */
  onLast() {


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


    //determine which singular inflection should be proposed next 
    let foundSg: boolean = false;

    for (let i of this.inflections) {
      for (let j of this.inflectionsTempSg) {
        if (i == j.infl && !j.checked) {
          console.log(j.infl);
          foundSg = true;
          for (let k of this.inflectionsNoviceSg) {
            if (k.infl == j.infl) {
              console.log("k: " + k.infl)
              k.checked = true;
            }
          }
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
          console.log(j.infl);
          foundPl = true;
          for (let k of this.inflectionsNovicePl) {
            if (k.infl == j.infl) {
              console.log("k: " + k.infl)
              k.checked = true;
            }
          }
        }
      }
      if (foundPl) {
        break;
      }
    }


  }



  /**
   * Mark inflections selected as novel to the user
   * @param infl 
   * @param num 
   */
  onChangeNov(infl: string, num: string) {

    if (num == 'singular') {
      for (let j of this.inflectionsNoviceSg) {
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
      for (let j of this.inflectionsNovicePl) {
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
   * Submit the info collected for the user model: put it together into an array of Levels, share with other components and 
   * go to the next component 
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
      this.openDialog("At least one declension type must be marked.");
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

      for (let i of this.inflectionsNoviceSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "novel", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      for (let i of this.inflectionsNovicePl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "novel", declensions: decls };
          this.lmodel.push(temp);
        }
      }

      if (this.lmodel.length == 0) {
        this.openDialog("At least one inflection type must be marked.");
      }

      else {

        this.model.sendModel(this.lmodel);

        this.route.navigate(['exercise'])
      }
    }


  }


  /**
   * Submit the info collected for the user model when the user chose the 'no experience with inflections' option
   */
  onSubmitNew() {

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
      this.openDialog("At least one declension type must be marked.");
    }


    else {

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

      if (this.lmodel.length == 0) {
        this.openDialog("At least one inflection type must be marked.");
      }

      else {

        this.model.sendModel(this.lmodel);

        this.route.navigate(['exercise'])
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

    else {
      this.declensionsFem.forEach(element => {
        element.checked = false;
      });

      this.declensionsMasc.forEach(element => {
        element.checked = false;
      });
    }
  }



  ngOnInit(): void {



    this.user.currentName.subscribe(username => this.username = username)


  }

}
