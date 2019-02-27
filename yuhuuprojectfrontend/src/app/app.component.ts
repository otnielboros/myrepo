import {Component, Inject} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(@Inject(DOCUMENT) private _document: HTMLDocument) {
    _document.getElementById('titleID').innerText = environment.titleEnvironment;
  }
}
