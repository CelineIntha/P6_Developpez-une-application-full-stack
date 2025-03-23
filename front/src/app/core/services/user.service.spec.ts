import {TestBed} from '@angular/core/testing';

import {UserService} from './user.service';
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideHttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {of} from "rxjs";

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ...provideHttpClientTesting(),
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({})
          }
        }
      ]
    });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
