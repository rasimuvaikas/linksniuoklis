import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-belenkas',
  templateUrl: './belenkas.component.html',
  styleUrls: ['./belenkas.component.css']
})
export class BelenkasComponent implements OnInit {

  declension1mascAS = [
    {inflection: "nominative", singular: 'výras', plural: 'výrai'},
    {inflection: "genitive", singular: 'výro', plural: 'výrų'},
    {inflection: "dative", singular: 'výrui', plural: 'výrams'},
    {inflection: "accusative", singular: 'výrą', plural: 'výrus'},
    {inflection: "instrumental", singular: 'výru', plural: 'výrais'},
    {inflection: "locative", singular: 'výre', plural: 'výruose'},
    {inflection: "vocative", singular: 'výre!', plural: 'výrai!'},

  ];

  declension1mascIAS = [
    {inflection: "nominative", singularIAS: 'kẽlias', pluralIAS: 'keliaĩ', singularJAS: 'vė́jas', pluralJAS: 'vė́jai'},
    {inflection: "genitive", singularIAS: 'kẽlio', pluralIAS: 'kelių̃', singularJAS: 'vė́jo', pluralJAS: 'vė́jų'},
    {inflection: "dative", singularIAS: 'kẽliui', pluralIAS: 'keliáms', singularJAS: 'vė́jui', pluralJAS: 'vė́jams'},
    {inflection: "accusative", singularIAS: 'kẽlią', pluralIAS: 'keliùs', singularJAS: 'vė́ją', pluralJAS: 'vė́jus'},
    {inflection: "instrumental", singularIAS: 'keliù', pluralIAS: 'keliaĩs', singularJAS: 'vė́ju', pluralJAS: 'vė́jais'},
    {inflection: "locative", singularIAS: 'kelyjè', pluralIAS: 'keliuosè', singularJAS: 'vė́jyje', pluralJAS: 'vė́juose'},
    {inflection: "vocative", singularIAS: 'kelỹ!', pluralIAS: 'keliaĩ!', singularJAS: 'vė́jau!', pluralJAS: 'vė́jai!'},

  ];

  declension2femA = [
    {inflection: "nominative", singular: 'rankà', plural: 'rañkos'},
    {inflection: "genitive", singular: 'rañkos', plural: 'rañkų'},
    {inflection: "dative", singular: 'rañkai', plural: 'rañkoms'},
    {inflection: "accusative", singular: 'rañką', plural: 'rankàs'},
    {inflection: "instrumental", singular: 'rankà', plural: 'rañkomis'},
    {inflection: "locative", singular: 'rañkoje', plural: 'rañkose'},
    {inflection: "vocative", singular: 'rañka!', plural: 'rañkos!'},

  ];


  declension2fem = [
    {inflection: "nominative", singularDT: 'kaltė̃', pluralDT: 'kal̃tės', singular: 'gėlė̃', plural: 'gė̃lės'},
    {inflection: "genitive", singularDT: 'kaltė̃s', pluralDT: 'kalčių̃', singular: 'gėlė̃s', plural: 'gėlių̃'},
    {inflection: "dative", singularDT: 'kal̃tei', pluralDT: 'kaltė́ms', singular: 'gė̃lei', plural: 'gėlė́ms'},
    {inflection: "accusative", singularDT: 'kal̃tę', pluralDT: 'kaltès', singular: 'gė̃lę', plural: 'gėlès'},
    {inflection: "instrumental", singularDT: 'kaltè', pluralDT: 'kaltėmìs', singular: 'gėlè', plural: 'gėlėmìs'},
    {inflection: "locative", singularDT: 'kaltėjè', pluralDT: 'kaltėsè', singular: 'gėlėjè', plural: 'gėlėsè'},
    {inflection: "vocative", singularDT: 'kal̃te!', pluralDT: 'kal̃tės!', singular: 'gė̃le!', plural: 'gė̃lės!'},

  ];


  displayedColumns: string[] = ["inflection", "singular", "plural"]
  displayedColumnsDouble: string[] = ["inflection", "singularIAS", "pluralIAS", "singularJAS", "pluralJAS" ]
  displayedColumnsDT: string[] = ["inflection", "singularDT", "pluralDT", "singular", "plural" ]

  constructor() { }

  ngOnInit(): void {
  }

}
