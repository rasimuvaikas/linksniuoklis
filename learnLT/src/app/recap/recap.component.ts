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

  declension1mascASSg = [
    {inflection: "nominative/vardininkas", form: 'vyras'},
    {inflection: "genitive/kilmininkas", form: 'vyro'},
    {inflection: "dative/naudininkas", form: 'vyrui'},
    {inflection: "accusative/galininkas", form: 'vyrą'},
    {inflection: "instrumental/įnagininkas", form: 'vyru'},
    {inflection: "locative/vietininkas", form: 'vyre'},
    {inflection: "vocative/šauksmininkas", form: 'vyre!'},

  ];

  declension1mascASPl = [
    {inflection: "nominative/vardininkas", form: 'vyrai'},
    {inflection: "genitive/kilmininkas", form: 'vyrų'},
    {inflection: "dative/naudininkas", form: 'vyrams'},
    {inflection: "accusative/galininkas", form: 'vyrus'},
    {inflection: "instrumental/įnagininkas", form: 'vyrais'},
    {inflection: "locative/vietininkas", form: 'vyruose'},
    {inflection: "vocative/šauksmininkas", form: 'vyrai!'},

  ];



  constructor(private _bottomSheetRef: MatBottomSheetRef<RecapComponent>) { }

  ngOnInit(): void {
  }

}
