import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LocarService } from '../service/locar.service';
import { ILocar, Locar } from '../locar.model';

import { LocarUpdateComponent } from './locar-update.component';

describe('Locar Management Update Component', () => {
  let comp: LocarUpdateComponent;
  let fixture: ComponentFixture<LocarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let locarService: LocarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LocarUpdateComponent],
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
      .overrideTemplate(LocarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    locarService = TestBed.inject(LocarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const locar: ILocar = { id: 456 };

      activatedRoute.data = of({ locar });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(locar));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Locar>>();
      const locar = { id: 123 };
      jest.spyOn(locarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locar }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(locarService.update).toHaveBeenCalledWith(locar);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Locar>>();
      const locar = new Locar();
      jest.spyOn(locarService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locar }));
      saveSubject.complete();

      // THEN
      expect(locarService.create).toHaveBeenCalledWith(locar);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Locar>>();
      const locar = { id: 123 };
      jest.spyOn(locarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(locarService.update).toHaveBeenCalledWith(locar);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
