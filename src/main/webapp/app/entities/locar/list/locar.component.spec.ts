import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LocarService } from '../service/locar.service';

import { LocarComponent } from './locar.component';

describe('Locar Management Component', () => {
  let comp: LocarComponent;
  let fixture: ComponentFixture<LocarComponent>;
  let service: LocarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LocarComponent],
    })
      .overrideTemplate(LocarComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocarComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LocarService);

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
    expect(comp.locars?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
