import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

export interface Result {
  result: boolean;
}

@Component({
  selector: 'app-import-dialog',
  templateUrl: './import-dialog.component.html',
  styleUrls: ['./import-dialog.component.css']
})
export class ImportDialogComponent {

  constructor(public dialogRef: MatDialogRef<ImportDialogComponent, Result>) {
  }

  cancel(): void {
    this.dialogRef.close({
      result: false
    });
  }

  yes(): void {
    this.dialogRef.close({
      result: true
    });
  }
}
