import {ComponentFixture, TestBed} from '@angular/core/testing';
import {CreateArticleComponent} from './create-article.component';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {ActivatedRoute} from "@angular/router";
import {of} from "rxjs";
import {provideHttpClient} from "@angular/common/http";

describe('CreateArticleComponent', () => {
  let component: CreateArticleComponent;
  let fixture: ComponentFixture<CreateArticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateArticleComponent],
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
    }).compileComponents();

    fixture = TestBed.createComponent(CreateArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
