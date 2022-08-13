import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MovimentosService } from '../service/movimentos.service';
import { IMovimentos, Movimentos } from '../movimentos.model';

import { MovimentosUpdateComponent } from './movimentos-update.component';

describe('Movimentos Management Update Component', () => {
  let comp: MovimentosUpdateComponent;
  let fixture: ComponentFixture<MovimentosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let movimentosService: MovimentosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MovimentosUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MovimentosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MovimentosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    movimentosService = TestBed.inject(MovimentosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const movimentos: IMovimentos = { id: 456 };

      activatedRoute.data = of({ movimentos });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(movimentos));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Movimentos>>();
      const movimentos = { id: 123 };
      jest.spyOn(movimentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movimentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movimentos }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(movimentosService.update).toHaveBeenCalledWith(movimentos);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Movimentos>>();
      const movimentos = new Movimentos();
      jest.spyOn(movimentosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movimentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movimentos }));
      saveSubject.complete();

      // THEN
      expect(movimentosService.create).toHaveBeenCalledWith(movimentos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Movimentos>>();
      const movimentos = { id: 123 };
      jest.spyOn(movimentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movimentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(movimentosService.update).toHaveBeenCalledWith(movimentos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
