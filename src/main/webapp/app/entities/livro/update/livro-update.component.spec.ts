import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LivroService } from '../service/livro.service';
import { ILivro, Livro } from '../livro.model';

import { LivroUpdateComponent } from './livro-update.component';

describe('Livro Management Update Component', () => {
  let comp: LivroUpdateComponent;
  let fixture: ComponentFixture<LivroUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let livroService: LivroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LivroUpdateComponent],
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
      .overrideTemplate(LivroUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivroUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    livroService = TestBed.inject(LivroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const livro: ILivro = { id: 456 };

      activatedRoute.data = of({ livro });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(livro));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livro>>();
      const livro = { id: 123 };
      jest.spyOn(livroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livro }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(livroService.update).toHaveBeenCalledWith(livro);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livro>>();
      const livro = new Livro();
      jest.spyOn(livroService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livro }));
      saveSubject.complete();

      // THEN
      expect(livroService.create).toHaveBeenCalledWith(livro);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livro>>();
      const livro = { id: 123 };
      jest.spyOn(livroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(livroService.update).toHaveBeenCalledWith(livro);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
