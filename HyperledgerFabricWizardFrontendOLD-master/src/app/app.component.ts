import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {Network} from './_models/network';
import {ThemingService} from './_services/theming.service';
import {DOCUMENT} from '@angular/common';
import * as FileSaver from 'file-saver';
import {MatDialog} from '@angular/material/dialog';
import {ImportDialogComponent} from './import-dialog/import-dialog.component';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Client, Orderer, Peer} from './_models/entity';
import {Ca} from './_models/ca';
import {Consortium} from './_models/consortium';
import {Channel} from './_models/channel';
import {Org} from './_models/org';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'HyperledgerFabric Wizard';
  network: Network;
  @ViewChild('import') input: ElementRef;
  file: File;

  // @HostBinding('class') public cssClass: string;

  constructor(private themingService: ThemingService,
              @Inject(DOCUMENT) private document: Document, private dialog: MatDialog, private snackbar: MatSnackBar) {
    const network = localStorage.getItem('json');
    if (network) {
      this.network = Network.parse(JSON.parse(network));
      localStorage.clear();
    } else {
      this.network = new Network();
    }
  }

  ngOnInit(): void {
    this.themingService.theme.subscribe((theme: string) => {
      // this.cssClass = theme;
      this.themingService.themes.forEach(t => {
        this.document.body.classList.remove(t);
      });
      this.document.body.classList.add(theme);
    });
  }

  export(): void {
    const blob = new Blob([JSON.stringify(this.network)], {type: 'text/json;charset=utf-8'});
    FileSaver.saveAs(blob, 'config.json');
  }

  import(): void {
    this.input.nativeElement.click();
  }

  fileToImportSelected(file: File): void {
    if (file.type !== 'application/json') {
      this.snackbar.open('Unsupported file', null, {
        duration: 2000
      });
      return;
    }
    this.file = file;
    this.dialog.open(ImportDialogComponent)
      .afterClosed()
      .subscribe(d => {
        if (d.result) {
          this.loadData();
          this.file = undefined;
        } else {
          this.file = undefined;
        }
      });
    this.input.nativeElement.value = '';
  }

  loadData(): void {
    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      try {
        const json = JSON.parse(fileReader.result as string);
        localStorage.setItem('json', JSON.stringify(json));
        location.reload();
      } catch (e) {
        localStorage.clear();
        this.snackbar.open('Unsupported file', null, {
          duration: 2000
        });
      }
    };
    fileReader.readAsText(this.file);
  }
}
