import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';

@Injectable()
export abstract class AbstractLoginService {

  authToken: String;

  public abstract login(username: String, password: String): Observable<boolean>;

  public logout() {

  }
}


export class MockLoginService implements AbstractLoginService {
  authToken = ' ';

  login(username: String, password: String): Observable<boolean> {
    if (username === 'test' && password === 'test') {
      return of(true);
    } else {
      return of(false);
    }
  }

  logout() {
  }


}

export class ServerLoginService implements AbstractLoginService {
  authToken = ' ';

  login(username: String, password: String): Observable<boolean> {
      return null;
  }

  logout() {
  }


}
