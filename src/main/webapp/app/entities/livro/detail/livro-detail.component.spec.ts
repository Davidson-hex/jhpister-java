import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LivroDetailComponent } from './livro-detail.component';

describe('Livro Management Detail Component', () => {
  let comp: LivroDetailComponent;
  let fixture: ComponentFixture<LivroDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LivroDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ livro: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LivroDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LivroDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load livro on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.livro).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
