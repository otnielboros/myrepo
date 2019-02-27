import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponentComponent} from './components/login-component/login-component.component';
import {RegisterComponentComponent} from './components/register-component/register-component.component';
import {AbstractLoginService} from './services/login.service';
import {environment} from '../../../environments/environment';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [LoginComponentComponent, RegisterComponentComponent],
  exports: [LoginComponentComponent, RegisterComponentComponent],
  providers: [
    {
      provide: AbstractLoginService,
      useClass: environment.loginService
    }
  ]
})
export class LoginModule {
}
