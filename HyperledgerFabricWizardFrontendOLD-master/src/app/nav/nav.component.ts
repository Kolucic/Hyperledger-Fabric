import {Component, Input} from '@angular/core';
import {AppComponent} from '../app.component';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent {

  @Input() title: string;
  @Input() rootComponent: AppComponent;

  constructor() {
  }

}
