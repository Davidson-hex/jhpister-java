import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocarDetailComponent } from './locar-detail.component';

describe('Locar Management Detail Component', () => {
  let comp: LocarDetailComponent;
  let fixture: ComponentFixture<LocarDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LocarDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ locar: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LocarDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LocarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load locar on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.locar).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
