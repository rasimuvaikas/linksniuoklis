import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})

/**
 * Dialogue Box component, used as an alert box in case the user has not fulfilled certain requirements upon information submission
 */
export class AlertComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<AlertComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) { }


    okay(): void {
      this.dialogRef.close();
    }
 

  ngOnInit(): void {
  }

}
