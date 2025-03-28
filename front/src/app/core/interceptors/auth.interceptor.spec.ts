import {TestBed} from '@angular/core/testing';
import {provideHttpClientTesting} from '@angular/common/http/testing';

import {AuthInterceptor} from './auth.interceptor';
import {AuthService} from '../services/auth.service';

describe('AuthInterceptor', (): void => {
  let interceptor: AuthInterceptor;

  beforeEach((): void => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(),
        AuthInterceptor,
        {
          provide: AuthService,
          useValue: {
            getToken: (): string => 'fake-jwt-token'
          }
        }
      ]
    });

    interceptor = TestBed.inject(AuthInterceptor);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });
});
