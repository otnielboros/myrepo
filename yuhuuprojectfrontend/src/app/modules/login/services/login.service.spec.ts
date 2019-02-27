import { TestBed } from '@angular/core/testing';

import { AbstractLoginService } from './login.service';

describe('LoginService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AbstractLoginService = TestBed.get(AbstractLoginService);
    expect(service).toBeTruthy();
  });
});
