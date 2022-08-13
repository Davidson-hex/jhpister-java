import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMovimentos, Movimentos } from '../movimentos.model';

import { MovimentosService } from './movimentos.service';

describe('Movimentos Service', () => {
  let service: MovimentosService;
  let httpMock: HttpTestingController;
  let elemDefault: IMovimentos;
  let expectedResult: IMovimentos | IMovimentos[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MovimentosService);
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

    it('should create a Movimentos', () => {
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

      service.create(new Movimentos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Movimentos', () => {
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

    it('should partial update a Movimentos', () => {
      const patchObject = Object.assign(
        {
          idUsuario: 1,
          locacao: 1,
          dataLocacao: currentDate.format(DATE_FORMAT),
          previsaoDevolucao: currentDate.format(DATE_FORMAT),
          status: 'BBBBBB',
          proprietario: 'BBBBBB',
        },
        new Movimentos()
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

    it('should return a list of Movimentos', () => {
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

    it('should delete a Movimentos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMovimentosToCollectionIfMissing', () => {
      it('should add a Movimentos to an empty array', () => {
        const movimentos: IMovimentos = { id: 123 };
        expectedResult = service.addMovimentosToCollectionIfMissing([], movimentos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(movimentos);
      });

      it('should not add a Movimentos to an array that contains it', () => {
        const movimentos: IMovimentos = { id: 123 };
        const movimentosCollection: IMovimentos[] = [
          {
            ...movimentos,
          },
          { id: 456 },
        ];
        expectedResult = service.addMovimentosToCollectionIfMissing(movimentosCollection, movimentos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Movimentos to an array that doesn't contain it", () => {
        const movimentos: IMovimentos = { id: 123 };
        const movimentosCollection: IMovimentos[] = [{ id: 456 }];
        expectedResult = service.addMovimentosToCollectionIfMissing(movimentosCollection, movimentos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(movimentos);
      });

      it('should add only unique Movimentos to an array', () => {
        const movimentosArray: IMovimentos[] = [{ id: 123 }, { id: 456 }, { id: 39604 }];
        const movimentosCollection: IMovimentos[] = [{ id: 123 }];
        expectedResult = service.addMovimentosToCollectionIfMissing(movimentosCollection, ...movimentosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const movimentos: IMovimentos = { id: 123 };
        const movimentos2: IMovimentos = { id: 456 };
        expectedResult = service.addMovimentosToCollectionIfMissing([], movimentos, movimentos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(movimentos);
        expect(expectedResult).toContain(movimentos2);
      });

      it('should accept null and undefined values', () => {
        const movimentos: IMovimentos = { id: 123 };
        expectedResult = service.addMovimentosToCollectionIfMissing([], null, movimentos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(movimentos);
      });

      it('should return initial array if no Movimentos is added', () => {
        const movimentosCollection: IMovimentos[] = [{ id: 123 }];
        expectedResult = service.addMovimentosToCollectionIfMissing(movimentosCollection, undefined, null);
        expect(expectedResult).toEqual(movimentosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
