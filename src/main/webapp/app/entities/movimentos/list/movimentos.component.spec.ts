import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MovimentosService } from '../service/movimentos.service';

import { MovimentosComponent } from './movimentos.component';

describe('Movimentos Management Component', () => {
  let comp: MovimentosComponent;
  let fixture: ComponentFixture<MovimentosComponent>;
  let service: MovimentosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MovimentosComponent],
    })
      .overrideTemplate(MovimentosComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MovimentosComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MovimentosService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.movimentos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
