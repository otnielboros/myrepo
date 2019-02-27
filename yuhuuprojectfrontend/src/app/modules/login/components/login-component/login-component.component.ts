import {Component, OnInit} from '@angular/core';
import {AbstractLoginService} from '../../services/login.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login-component',
  templateUrl: './login-component.component.html',
  styleUrls: ['./login-component.component.scss']
})
export class LoginComponentComponent implements OnInit {

  public username = '';
  public password = '';

  private loginService: AbstractLoginService;

  constructor(loginService: AbstractLoginService,
              private router: Router) {
    this.loginService = loginService;
  }

  ngOnInit() {
  }

  onLogin() {
    this.loginService.login(this.username, this.password).subscribe((isValid: boolean) => {
      if (isValid) {
        this.router.navigateByUrl('/dashboard');
      } else {

      }
    });

  }
}
