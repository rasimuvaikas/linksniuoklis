import { Component, OnChanges, SimpleChanges, Input } from '@angular/core';

@Component({
  selector: 'app-progress',
  templateUrl: './progress.component.html',
  styleUrls: ['./progress.component.css']
})

/**
 * Instant visualisation of learner's progress
 */
export class ProgressComponent implements OnChanges {

  @Input('numNow') numNow: number;
  @Input('overall') overall: { correct: number, incorrect: number };


  numAll: number;

  topNow: string;
  heightNow: string;

  filling: string;

  count: number;

  correctPercent: string;
  incorrectPercent: string;

  background: string;


  constructor() {

  }


  /**
   * Update the progress bar that displays continuous progress of the learner
   * @param score the number of correct answers the user has hit in a row
   */
  setBar(score: number) {
    this.numNow = score;
    if (this.numNow > 10) {
      this.heightNow = 10 * 10 + "%";
      this.topNow = 100 - (10 * 10) + "%";
    } else {
      this.heightNow = this.numNow * 10 + "%";
      this.topNow = 100 - (this.numNow * 10) + "%";
    }
    if (score < 3) {
      this.filling = "grey";
    } else if (score < 5) {
      this.filling = "blue";
    } else if (score < 9) {
      this.filling = "rgb(202, 199, 15)";
    } else if (score < 10) {
      this.filling = "orange";
    } else if (score >= 10) {
      this.filling = "green";
    }

  }

  /**
   * Update the progress bar that displays the proportion of correct answers and mistakes that the user has made during the current session
   * @param update an element that contains the numbers of correct and incorrect answers
   */
  setOverallBar(update: any) {

    var total = update.correct + update.incorrect;

    if (total != 0) {
      this.correctPercent = update.correct / total * 100 + "%";
    }
    else {
      this.correctPercent = "100%";

    }

    this.background = "linear-gradient(to top, limegreen " + this.correctPercent + ", red " + this.correctPercent + ", red 100%)";
  }


  ngOnChanges(changes: SimpleChanges): void {

    for (let i in changes) {

      if (i == 'numNow') {
        this.setBar(changes[i].currentValue);
      }
      if (i == 'overall') {
        this.setOverallBar(changes[i].currentValue);
      }
    }

  }

}
