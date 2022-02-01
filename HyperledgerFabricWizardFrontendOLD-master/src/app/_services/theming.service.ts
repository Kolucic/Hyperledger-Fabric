import {ApplicationRef, Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable()
export class ThemingService {
  themes = ['dark-theme', 'light-theme'];
  theme = new BehaviorSubject('light-theme');

  constructor(private ref: ApplicationRef) {
    const darkModeOn = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
    if (darkModeOn) {
      this.theme.next('dark-theme');
    }
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
      this.theme.next(e.matches ? 'dark-theme' : 'light-theme');
      this.ref.tick();
    });
  }
}
