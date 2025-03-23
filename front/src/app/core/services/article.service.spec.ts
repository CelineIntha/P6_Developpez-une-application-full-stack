import {TestBed} from '@angular/core/testing';

import {ArticleService} from './article.service';
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideHttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {of} from "rxjs";

describe('ArticleService', () => {
  let service: ArticleService;

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
    service = TestBed.inject(ArticleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
