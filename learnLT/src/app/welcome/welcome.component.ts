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

  //šitą turbūt reiks ištrint kaip ir getResult iš ngoninit
  userModel: Level[];

  inflectionsSg: Inflection[];
  inflectionsPl: Inflection[];

  advancedSg: Inflection[];
  advancedPl: Inflection[];

  intermediateSg: Inflection[];
  intermediatePl: Inflection[];

  inflectionsBeginnerSg: Inflection[];
  inflectionsBeginnerPl: Inflection[];

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

    this.intermediateSg = [{ number: "singular", infl: "nominative", checked: false },
    { number: "singular", infl: "genitive", checked: false },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.intermediatePl = [{ number: "plural", infl: "nominative", checked: false },
    { number: "plural", infl: "genitive", checked: false },
    { number: "plural", infl: "dative", checked: false },
    { number: "plural", infl: "accusative", checked: false },
    { number: "plural", infl: "instrumental", checked: false },
    { number: "plural", infl: "locative", checked: false }];

    this.inflectionsBeginnerSg = [{ number: "singular", infl: "nominative", checked: false },
    { number: "singular", infl: "genitive", checked: false },
    { number: "singular", infl: "dative", checked: false },
    { number: "singular", infl: "accusative", checked: false },
    { number: "singular", infl: "instrumental", checked: false },
    { number: "singular", infl: "locative", checked: false }];

    this.inflectionsBeginnerPl = [{ number: "plural", infl: "nominative", checked: false },
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

    this.declensionsMasc = [{ display: "-(i)as", declension: "1mascIAS", decl: "1st", checked: true },
    { display: "-is, -ys", declension: "1mascIS", decl: "1st", checked: false },
    { display: "-is", declension: "3masc", decl: "3rd", checked: false },
    { display: "-(i)us", declension: "4masc", decl: "4th", checked: false },
    { display: "-uo", declension: "5masc", decl: "5th", checked: false },
    { display: "exceptions", declension: "exception", decl: "", checked: false }
    ]

    this.declensionsFem = [{ display: "-(i)a", declension: "2femIA", decl: "2nd", checked: true },
    { display: "-ė", declension: "2femĖ", decl: "2nd", checked: true },
    { display: "-is", declension: "3fem", decl: "3rd", checked: false },
    { display: "sesuo, duktė", declension: "5fem", decl: "5th", checked: false },
    ]

    //missing exception words

  }

  /**
   * Alert box
   */
  openDialog(d: string): void {
    const dialogRef = this.dialogue.open(AlertComponent, {
      width: '250px',
      data: d
    });
  }

  /**
   * Recap bottom sheet
   */
  openBottomSheet(): void {
    this._bottomSheet.open(RecapComponent);
  }

  /**
   * Mark inflections selected as intermediate level 
   * @param infl the selected inflection
   * @param num the selected number
   */
  onChangeFam(infl: string, num: string) {

    if (num == 'singular') {
      for (let j of this.intermediateSg) {
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
      for (let j of this.intermediatePl) {
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
   * Create two temporary arrays of inflections to be marked as checked
   * Determine which inflections should be proposed to the user to practice next
   */
  onLast() {


    for (let i of this.intermediateSg) {
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

    for (let i of this.intermediatePl) {
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
          for (let k of this.inflectionsBeginnerSg) {
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
          for (let k of this.inflectionsBeginnerPl) {
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
   * Mark inflections selected as beginner level by the user
   * @param infl inflection
   * @param num number
   */
  onChangeNov(infl: string, num: string) {

    if (num == 'singular') {
      for (let j of this.inflectionsBeginnerSg) {
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
      for (let j of this.inflectionsBeginnerPl) {
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

    //make sure the user has chosen at least one declension type
    if (decls.length == 0) {
      this.openDialog("At least one declension type must be marked.");
    }


    else {

      let lmodel: Level[] = []


      for (let i of this.advancedSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "advanced", declensions: decls };
          lmodel.push(temp);
        }
      }

      for (let i of this.advancedPl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "advanced", declensions: decls };
          lmodel.push(temp);
        }
      }

      for (let i of this.intermediateSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "intermediate", declensions: decls };
          lmodel.push(temp);
        }
      }

      for (let i of this.intermediatePl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "intermediate", declensions: decls };
          lmodel.push(temp);
        }
      }

      for (let i of this.inflectionsBeginnerSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "beginner", declensions: decls };
          lmodel.push(temp);
        }
      }

      for (let i of this.inflectionsBeginnerPl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "beginner", declensions: decls };
          lmodel.push(temp);
        }
      }

      console.log('modelis', lmodel)

      //make sure the user has chosen at least one inflection type
      if (lmodel.length == 0) {
        this.openDialog("At least one inflection type must be marked.");
      }

      else {
        //check if any sentences exist in any of the selected declensions or declensions 
        let found = false
        for (let i of lmodel) {
          for (let j of i.declensions) {
            this.connect.getCard(i.infl, i.number, [j]).subscribe(card => {
              console.log(card); let car = JSON.parse(card); console.log(car.simple);
              if (car.simple != null) {
                console.log('found')
                found = true
                console.log('čia found', found)
              }
            })
          }
        }

        //delay the processing of the found boolean as getCard takes time to update everything
        setTimeout(() => {
          if (found) {
            this.model.sendModel(lmodel);

            this.route.navigate(['exercise'])
          }

          else {
            this.openDialog("Oops, no examples of such inflection or declension could be found. Please select another combination");
          }
        }, 1000);
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

    //make sure the user has chosen at least one declension type
    if (decls.length == 0) {
      this.openDialog("At least one declension type must be marked.");
    }


    else {

      let lmodel: Level[] = []

      for (let i of this.inflectionsSg) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "singular", infl: i.infl, level: "beginner", declensions: decls };
          lmodel.push(temp);
        }
      }

      for (let i of this.inflectionsPl) {
        if (i.checked) {
          let temp: Level = { username: this.username, number: "plural", infl: i.infl, level: "beginner", declensions: decls };
          lmodel.push(temp);
        }
      }

      console.log('modelis', lmodel)
      //make sure the user has chosen at least one inflection type
      if (lmodel.length == 0) {
        this.openDialog("At least one inflection type must be marked.");
      }

      else {
        //check if any sentences exist in any of the selected declensions or declensions 
        let found = false
        for (let i of lmodel) {
          for (let j of i.declensions) {
            this.connect.getCard(i.infl, i.number, [j]).subscribe(card => {
              console.log(card); let car = JSON.parse(card); console.log(car.simple);
              if (car.simple != null) {
                console.log('found')
                found = true
                console.log('čia found', found)
              }
            })
          }
        }

        //delay the processing of the found boolean as getCard takes time to update everything
        setTimeout(() => {
          if (found) {
            this.model.sendModel(lmodel);

            this.route.navigate(['exercise'])
          }

          else {
            this.openDialog("Oops, no examples of such inflection or declension could be found. Please select another combination");
          }
        }, 1000);
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

    this.user.currentName.subscribe(username => this.username = username) //get username
  }

}
