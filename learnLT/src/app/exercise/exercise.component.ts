import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../user-info.service';
import { Router } from '@angular/router';
import { LearnerModelService } from '../learner-model.service';
import { Level } from '../level';
import { ConnectService } from '../connect.service';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { RecapComponent } from '../recap/recap.component';

@Component({
  selector: 'app-exercise',
  templateUrl: './exercise.component.html',
  styleUrls: ['./exercise.component.css']
})

/**
 * A component that lets user choose which skill they would like to practice:
 * declension, accentuation, or both
 */
export class ExerciseComponent implements OnInit {


  lmodel: Level[];
  currentModel: Level[];

  constructor(private user: UserInfoService, private route: Router, private model: LearnerModelService, private connect: ConnectService,
    private _bottomSheet: MatBottomSheet) { }


  /**
   * Display bottom sheet with recap info
   */
  openBottomSheet(): void {
    this._bottomSheet.open(RecapComponent);
  }

  /**
   * Practise declension
   */
  onSubmitDecl() {

    this.route.navigate(['decline']);

  }

  /**
 * Practise accentuation
 */
  onSubmitStress() {

    this.route.navigate(['stress']);

  }

  /**
 * Practise both declension and accentuation
 */
  onSubmitBoth() {

    this.route.navigate(['both']);

  }

  ngOnInit(): void {


    this.model.lvls.subscribe(data => { this.lmodel = data; console.log('exercise ', JSON.stringify(data)); });


    //update the learner model table
    this.connect.postModel(this.lmodel).subscribe(dt => { this.currentModel = dt;  this.model.sendModel(this.currentModel); console.log(dt) });

  }

}
