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
    {inflection: "nominative", singular1: 'kẽlias', plural1: 'keliaĩ', singular2: 'vė́jas', plural2: 'vė́jai'},
    {inflection: "genitive", singular1: 'kẽlio', plural1: 'kelių̃', singular2: 'vė́jo', plural2: 'vė́jų'},
    {inflection: "dative", singular1: 'kẽliui', plural1: 'keliáms', singular2: 'vė́jui', plural2: 'vė́jams'},
    {inflection: "accusative", singular1: 'kẽlią', plural1: 'keliùs', singular2: 'vė́ją', plural2: 'vė́jus'},
    {inflection: "instrumental", singular1: 'keliù', plural1: 'keliaĩs', singular2: 'vė́ju', plural2: 'vė́jais'},
    {inflection: "locative", singular1: 'kelyjè', plural1: 'keliuosè', singular2: 'vė́jyje', plural2: 'vė́juose'},
    {inflection: "vocative", singular1: 'kelỹ!', plural1: 'keliaĩ!', singular2: 'vė́jau!', plural2: 'vė́jai!'},

  ];


  declension1mascIS = [
    {inflection: "nominative", singular1: 'skė̃tis', plural1: 'skė̃čiai', singular2: 'peĩlis', plural2: 'peĩliai'},
    {inflection: "genitive", singular1: 'skė̃čio', plural1: 'skė̃čių', singular2: 'peĩlio', plural2: 'peĩlių'},
    {inflection: "dative", singular1: 'skė̃čiui', plural1: 'skė̃čiams', singular2: 'peĩliui', plural2: 'peĩliams'},
    {inflection: "accusative", singular1: 'skė̃tį', plural1: 'skėčiùs', singular2: 'peĩlį', plural2: 'peiliùs'},
    {inflection: "instrumental", singular1: 'skėčiù', plural1: 'skė̃čiais', singular2: 'peiliù', plural2: 'peĩliais'},
    {inflection: "locative", singular1: 'skė̃tyje', plural1: 'skė̃čiuose', singular2: 'peĩlyje', plural2: 'peĩliuose'},
    {inflection: "vocative", singular1: 'skė̃ti!', plural1: 'skė̃čiai!', singular2: 'peĩli!', plural2: 'peĩliai!'},

  ];

  declension1mascYS = [
    {inflection: "nominative", singular1: 'arklỹs', plural1: 'arkliaĩ', singular2: 'gaidỹs', plural2: 'gaidžiaĩ'},
    {inflection: "genitive", singular1: 'árklio', plural1: 'arklių̃', singular2: 'gaĩdžio', plural2: 'gaidžių̃'},
    {inflection: "dative", singular1: 'árkliui', plural1: 'arkliáms', singular2: 'gaĩdžiui', plural2: 'gaidžiáms'},
    {inflection: "accusative", singular1: 'árklį', plural1: 'árklius', singular2: 'gaĩdį', plural2: 'gaidžiùs'},
    {inflection: "instrumental", singular1: 'árkliu', plural1: 'arkliaĩs', singular2: 'gaidžiù', plural2: 'gaidžiaĩs'},
    {inflection: "locative", singular1: 'arklyjè', plural1: 'arkliuosè', singular2: 'gaidyjè', plural2: 'gaidžiuosè'},
    {inflection: "vocative", singular1: 'arklỹ!', plural1: 'arkliaĩ!', singular2: 'gaidỹ!', plural2: 'gaidžiaĩ!'},

  ]


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
    {inflection: "nominative", singular1: 'kaltė̃', plural1: 'kal̃tės', singular2: 'gėlė̃', plural2: 'gė̃lės'},
    {inflection: "genitive", singular1: 'kaltė̃s', plural1: 'kalčių̃', singular2: 'gėlė̃s', plural2: 'gėlių̃'},
    {inflection: "dative", singular1: 'kal̃tei', plural1: 'kaltė́ms', singular2: 'gė̃lei', plural2: 'gėlė́ms'},
    {inflection: "accusative", singular1: 'kal̃tę', plural1: 'kaltès', singular2: 'gė̃lę', plural2: 'gėlès'},
    {inflection: "instrumental", singular1: 'kaltè', plural1: 'kaltėmìs', singular2: 'gėlè', plural2: 'gėlėmìs'},
    {inflection: "locative", singular1: 'kaltėjè', plural1: 'kaltėsè', singular2: 'gėlėjè', plural2: 'gėlėsè'},
    {inflection: "vocative", singular1: 'kal̃te!', plural1: 'kal̃tės!', singular2: 'gė̃le!', plural2: 'gė̃lės!'},

  ];

  declension3masc = [
    {inflection: "nominative", singular1: 'dantìs', plural1: 'dañtys', singular2: 'debesìs', plural2: 'dẽbesys'},
    {inflection: "genitive", singular1: 'dantiẽs', plural1: 'dantų̃', singular2: 'debesiẽs', plural2: 'debesų̃'},
    {inflection: "dative", singular1: 'dañčiui', plural1: 'dantìms', singular2: 'dẽbesiui', plural2: 'debesìms'},
    {inflection: "accusative", singular1: 'dañtį', plural1: 'dantìs', singular2: 'dẽbesį', plural2: 'debesìs'},
    {inflection: "instrumental", singular1: 'dantimì', plural1: 'dantimìs', singular2: 'debesimì', plural2: 'debesimìs'},
    {inflection: "locative", singular1: 'dantyjè', plural1: 'dantysè', singular2: 'debesyjè', plural2: 'debesysè'},
    {inflection: "vocative", singular1: 'dantiẽ!', plural1: 'dañtys!', singular2: 'debesiẽ!', plural2: 'dẽbesys!'},

  ];

  declension3mascVagis = [
    {inflection: "nominative", singular1: 'žvėrìs', plural1: 'žvė́rys', singular2: 'vagìs', plural2: 'vãgys'},
    {inflection: "genitive", singular1: 'žvėriẽs', plural1: 'žvėrių̃', singular2: 'vagiẽs', plural2: 'vagių̃'},
    {inflection: "dative", singular1: 'žvė́riui', plural1: 'žvėrìms', singular2: 'vãgiui', plural2: 'vagìms'},
    {inflection: "accusative", singular1: 'žvė́rį', plural1: 'žvėrìs', singular2: 'vãgį', plural2: 'vagìs'},
    {inflection: "instrumental", singular1: 'žvėrimì', plural1: 'žvėrimìs', singular2: 'vagimì', plural2: 'vagimìs'},
    {inflection: "locative", singular1: 'žvėryjè', plural1: 'žvėrysè', singular2: 'vagyjè', plural2: 'vagysè'},
    {inflection: "vocative", singular1: 'žvėriẽ!', plural1: 'žvė́rys!', singular2: 'vagiẽ!', plural2: 'vãgys!'},

  ];

  declension3femDT = [
    {inflection: "nominative", singular1: 'širdìs', plural1: 'šìrdys', singular2: 'žuvìs', plural2: 'žùvys'},
    {inflection: "genitive", singular1: 'širdiẽs', plural1: 'širdžių̃', singular2: 'žuviẽs', plural2: 'žuvų̃'},
    {inflection: "dative", singular1: 'šìrdžiai', plural1: 'širdìms', singular2: 'žùviai', plural2: 'žuvìms'},
    {inflection: "accusative", singular1: 'šìrdį', plural1: 'šìrdis', singular2: 'žùvį', plural2: 'žuvìs'},
    {inflection: "instrumental", singular1: 'širdimì', plural1: 'širdimìs', singular2: 'žuvimì', plural2: 'žuvimìs'},
    {inflection: "locative", singular1: 'širdyjè', plural1: 'širdysè', singular2: 'žuvyjè', plural2: 'žuvysè'},
    {inflection: "vocative", singular1: 'širdiẽ!', plural1: 'šìrdys!', singular2: 'žuviẽ!', plural2: 'žùvys!'},

  ];


  moteris = [
    {inflection: "nominative", singular: 'móteris', plural: 'móterys'},
    {inflection: "genitive", singular: 'móters', plural: 'móterų'},
    {inflection: "dative", singular: 'móteriai', plural: 'móterims'},
    {inflection: "accusative", singular: 'móterį', plural: 'móteris'},
    {inflection: "instrumental", singular: 'móterimi', plural: 'móterimis'},
    {inflection: "locative", singular: 'móteryje', plural: 'móteryse'},
    {inflection: "vocative", singular: 'móterie!', plural: 'móterys!'},

  ];



  vaisius = [
    {inflection: "nominative", singular1: 'vaĩsius', plural1: 'vaĩsiai', singular2: 'muziẽjus', plural2: 'muziẽjai'},
    {inflection: "genitive", singular1: 'vaĩsiaus', plural1: 'vaĩsių', singular2: 'muziẽjaus', plural2: 'muziẽjų'},
    {inflection: "dative", singular1: 'vaĩsiui', plural1: 'vaĩsiams', singular2: 'muziẽjui', plural2: 'muziẽjams'},
    {inflection: "accusative", singular1: 'vaĩsių', plural1: 'vaisiùs', singular2: 'muziẽjų', plural2: 'muziẽjus'},
    {inflection: "instrumental", singular1: 'vaĩsiumi', plural1: 'vaĩsiais', singular2: 'muziẽjumi', plural2: 'muziẽjais'},
    {inflection: "locative", singular1: 'vaĩsiuje', plural1: 'vaĩsiuose', singular2: 'muziẽjuje', plural2: 'muziẽjuose'},
    {inflection: "vocative", singular1: 'vaĩsiau!', plural1: 'vaĩsiai!', singular2: 'muziẽjau!', plural2: 'muziẽjai!'},

  ];

  dangus = [
    {inflection: "nominative", singular: 'dangùs', plural: 'dañgūs'},
    {inflection: "genitive", singular: 'dangaũs', plural: 'dangų̃'},
    {inflection: "dative", singular: 'dañgui', plural: 'dangùms'},
    {inflection: "accusative", singular: 'dangų̃', plural: 'dangùs'},
    {inflection: "instrumental", singular: 'dangumì', plural: 'dangumìs'},
    {inflection: "locative", singular: 'dangujè', plural: 'danguosè'},
    {inflection: "vocative", singular: 'dangaũ!', plural: 'dañgūs!'},

  ];

  zmogus = [
    {inflection: "nominative", singular: 'žmogùs', plural: 'žmónės'},
    {inflection: "genitive", singular: 'žmogaũs', plural: 'žmonių̃'},
    {inflection: "dative", singular: 'žmógui', plural: 'žmonė́ms'},
    {inflection: "accusative", singular: 'žmógų', plural: 'žmónes'},
    {inflection: "instrumental", singular: 'žmogumì', plural: 'žmonėmìs'},
    {inflection: "locative", singular: 'žmogujè', plural: 'žmonėsè'},
    {inflection: "vocative", singular: 'žmogaũ!', plural: 'žmónės!'},

  ];


  akmuo = [
    {inflection: "nominative", singular1: 'akmuõ', plural1: 'ãkmenys', singular2: 'vanduõ', plural2: 'vándenys'},
    {inflection: "genitive", singular1: 'akmeñs', plural1: 'akmenų̃', singular2: 'vandeñs', plural2: 'vandenų̃'},
    {inflection: "dative", singular1: 'ãkmeniui', plural1: 'akmenìms', singular2: 'vándeniui', plural2: 'vandenìms'},
    {inflection: "accusative", singular1: 'ãkmenį', plural1: 'ãkmenis', singular2: 'vándenį', plural2: 'vándenis'},
    {inflection: "instrumental", singular1: 'ãkmeniu', plural1: 'akmenimìs', singular2: 'vándeniu', plural2: 'muziẽjais'},
    {inflection: "locative", singular1: 'akmenyjè', plural1: 'akmenysè', singular2: 'vandenyjè', plural2: 'vandenimìs'},
    {inflection: "vocative", singular1: 'akmeniẽ!', plural1: 'ãkmenys!', singular2: 'vandeniẽ!', plural2: 'vandenysè!'},

  ];


  suo = [
    {inflection: "nominative", singular: 'šuõ', plural: 'šùnys'},
    {inflection: "genitive", singular: 'šuñs', plural: 'šunų̃'},
    {inflection: "dative", singular: 'šùniui', plural: 'šunìms'},
    {inflection: "accusative", singular: 'šùnį', plural: 'šunìs'},
    {inflection: "instrumental", singular: 'šunimì', plural: 'šunimìs'},
    {inflection: "locative", singular: 'šunyjè', plural: 'šunysè'},
    {inflection: "vocative", singular: 'šuniẽ!', plural: 'šùnys!'},

  ];

  menuo = [
    {inflection: "nominative", singular: 'mė́nuo', plural: 'mė́nesiai'},
    {inflection: "genitive", singular: 'mė́nesio', plural: 'mė́nesių'},
    {inflection: "dative", singular: 'mė́nesiui', plural: 'mė́nesiams'},
    {inflection: "accusative", singular: 'mė́nesį', plural: 'mė́nesius'},
    {inflection: "instrumental", singular: 'mė́nesiu', plural: 'mė́nesiais'},
    {inflection: "locative", singular: 'mė́nesyje', plural: 'mė́nesiuose'},
    {inflection: "vocative", singular: 'mė́nesi!', plural: 'mė́nesiai!'},

  ];


  dukte = [
    {inflection: "nominative", singular: 'duktė̃', plural: 'dùkterys'},
    {inflection: "genitive", singular: 'dukter̃s', plural: 'dukterų̃'},
    {inflection: "dative", singular: 'dùkteriai', plural: 'dukterìms'},
    {inflection: "accusative", singular: 'dùkterį', plural: 'dùkteris'},
    {inflection: "instrumental", singular: 'dukterimì', plural: 'dukterimìs'},
    {inflection: "locative", singular: 'dukteryjè', plural: 'dukterysè'},
    {inflection: "vocative", singular: 'dukteriẽ!', plural: 'dùkterys!'},

  ];

  sesuo = [
    {inflection: "nominative", singular: 'sesuõ', plural: 'sẽserys'},
    {inflection: "genitive", singular: 'seser̃s', plural: 'seserų̃'},
    {inflection: "dative", singular: 'sẽseriai', plural: 'seserìms'},
    {inflection: "accusative", singular: 'sẽserį', plural: 'sẽseris'},
    {inflection: "instrumental", singular: 'seserimì', plural: 'seserimìs'},
    {inflection: "locative", singular: 'seseryjè', plural: 'seserysè'},
    {inflection: "vocative", singular: 'seseriẽ!', plural: 'sẽserys!'},

  ];




  displayedColumns: string[] = ["inflection", "singular", "plural"]
  displayedColumnsDouble: string[] = ["inflection", "singular1", "plural1", "singular2", "plural2" ]


  constructor() { }

  ngOnInit(): void {
  }

}
