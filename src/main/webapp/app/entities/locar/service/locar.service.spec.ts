import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILocar, Locar } from '../locar.model';

import { LocarService } from './locar.service';

describe('Locar Service', () => {
  let service: LocarService;
  let httpMock: HttpTestingController;
  let elemDefault: ILocar;
  let expectedResult: ILocar | ILocar[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LocarService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      idUsuario: 0,
      idLivro: 0,
      locacao: 0,
      dataLocacao: currentDate,
      previsaoDevolucao: currentDate,
      dataDevolucao: currentDate,
      devolucao: 0,
      status: 'AAAAAAA',
      proprietario: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dataLocacao: currentDate.format(DATE_FORMAT),
          previsaoDevolucao: currentDate.format(DATE_FORMAT),
          dataDevolucao: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Locar', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dataLocacao: currentDate.format(DATE_FORMAT),
          previsaoDevolucao: currentDate.format(DATE_FORMAT),
          dataDevolucao: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataLocacao: currentDate,
          previsaoDevolucao: currentDate,
          dataDevolucao: currentDate,
        },
        returnedFromService
      );

      service.create(new Locar()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Locar', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idUsuario: 1,
          idLivro: 1,
          locacao: 1,
          dataLocacao: currentDate.format(DATE_FORMAT),
          previsaoDevolucao: currentDate.format(DATE_FORMAT),
          dataDevolucao: currentDate.format(DATE_FORMAT),
          devolucao: 1,
          status: 'BBBBBB',
          proprietario: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataLocacao: currentDate,
          previsaoDevolucao: currentDate,
          dataDevolucao: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Locar', () => {
      const patchObject = Object.assign(
        {
          locacao: 1,
          dataDevolucao: currentDate.format(DATE_FORMAT),
          status: 'BBBBBB',
          proprietario: 'BBBBBB',
        },
        new Locar()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dataLocacao: currentDate,
          previsaoDevolucao: currentDate,
          dataDevolucao: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Locar', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idUsuario: 1,
          idLivro: 1,
          locacao: 1,
          dataLocacao: currentDate.format(DATE_FORMAT),
          previsaoDevolucao: currentDate.format(DATE_FORMAT),
          dataDevolucao: currentDate.format(DATE_FORMAT),
          devolucao: 1,
          status: 'BBBBBB',
          proprietario: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataLocacao: currentDate,
          previsaoDevolucao: currentDate,
          dataDevolucao: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Locar', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLocarToCollectionIfMissing', () => {
      it('should add a Locar to an empty array', () => {
        const locar: ILocar = { id: 123 };
        expectedResult = service.addLocarToCollectionIfMissing([], locar);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locar);
      });

      it('should not add a Locar to an array that contains it', () => {
        const locar: ILocar = { id: 123 };
        const locarCollection: ILocar[] = [
          {
            ...locar,
          },
          { id: 456 },
        ];
        expectedResult = service.addLocarToCollectionIfMissing(locarCollection, locar);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Locar to an array that doesn't contain it", () => {
        const locar: ILocar = { id: 123 };
        const locarCollection: ILocar[] = [{ id: 456 }];
        expectedResult = service.addLocarToCollectionIfMissing(locarCollection, locar);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locar);
      });

      it('should add only unique Locar to an array', () => {
        const locarArray: ILocar[] = [{ id: 123 }, { id: 456 }, { id: 16123 }];
        const locarCollection: ILocar[] = [{ id: 123 }];
        expectedResult = service.addLocarToCollectionIfMissing(locarCollection, ...locarArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const locar: ILocar = { id: 123 };
        const locar2: ILocar = { id: 456 };
        expectedResult = service.addLocarToCollectionIfMissing([], locar, locar2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locar);
        expect(expectedResult).toContain(locar2);
      });

      it('should accept null and undefined values', () => {
        const locar: ILocar = { id: 123 };
        expectedResult = service.addLocarToCollectionIfMissing([], null, locar, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locar);
      });

      it('should return initial array if no Locar is added', () => {
        const locarCollection: ILocar[] = [{ id: 123 }];
        expectedResult = service.addLocarToCollectionIfMissing(locarCollection, undefined, null);
        expect(expectedResult).toEqual(locarCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
