import { Component, OnInit } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';

@Component({
  selector: 'app-recap',
  templateUrl: './recap.component.html',
  styleUrls: ['./recap.component.css']
})

/**
 * A bottom sheet element that contains useful grammar information
 */
export class RecapComponent implements OnInit {

  decl:boolean;
  stress:boolean;

  constructor(private _bottomSheetRef: MatBottomSheetRef<RecapComponent>) { }

  ngOnInit(): void {
  }

}
