import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ArticleDetailComponent} from './article-detail.component';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {ActivatedRoute, provideRouter} from '@angular/router';
import {provideHttpClient} from "@angular/common/http";

describe('ArticleDetailComponent', () => {
  let component: ArticleDetailComponent;
  let fixture: ComponentFixture<ArticleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleDetailComponent],
      providers: [
        ...provideHttpClientTesting(),
        provideHttpClient(),
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => {
                }
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
