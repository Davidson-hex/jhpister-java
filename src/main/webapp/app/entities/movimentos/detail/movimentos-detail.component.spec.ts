import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MovimentosDetailComponent } from './movimentos-detail.component';

describe('Movimentos Management Detail Component', () => {
  let comp: MovimentosDetailComponent;
  let fixture: ComponentFixture<MovimentosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MovimentosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ movimentos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MovimentosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MovimentosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load movimentos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.movimentos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
