import {Component, Input, OnInit} from '@angular/core';
import {Network} from '../_models/network';
import * as FileSaver from 'file-saver';
import {Server} from '../_services/server';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.css']
})
export class SummaryComponent implements OnInit {
  @Input() network: Network;
  disabled = false;

  constructor(private server: Server, private snackbar: MatSnackBar) {
  }

  ngOnInit(): void {
  }

  download(): void {
    this.disabled = true;
    this.server.send(this.network).subscribe(s => {
      this.disabled = false;
      FileSaver.saveAs(s.body, s.headers.get('Content-Disposition').split('filename=')[1]);
    }, e => {
      this.disabled = false;
      this.snackbar.open('Network error', null, {
        duration: 2000
      });
    });
  }

}
