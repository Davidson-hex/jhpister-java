import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMovimentos, getMovimentosIdentifier } from '../movimentos.model';

export type EntityResponseType = HttpResponse<IMovimentos>;
export type EntityArrayResponseType = HttpResponse<IMovimentos[]>;

@Injectable({ providedIn: 'root' })
export class MovimentosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/movimentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(movimentos: IMovimentos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movimentos);
    return this.http
      .post<IMovimentos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(movimentos: IMovimentos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movimentos);
    return this.http
      .put<IMovimentos>(`${this.resourceUrl}/${getMovimentosIdentifier(movimentos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(movimentos: IMovimentos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movimentos);
    return this.http
      .patch<IMovimentos>(`${this.resourceUrl}/${getMovimentosIdentifier(movimentos) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMovimentos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMovimentos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMovimentosToCollectionIfMissing(
    movimentosCollection: IMovimentos[],
    ...movimentosToCheck: (IMovimentos | null | undefined)[]
  ): IMovimentos[] {
    const movimentos: IMovimentos[] = movimentosToCheck.filter(isPresent);
    if (movimentos.length > 0) {
      const movimentosCollectionIdentifiers = movimentosCollection.map(movimentosItem => getMovimentosIdentifier(movimentosItem)!);
      const movimentosToAdd = movimentos.filter(movimentosItem => {
        const movimentosIdentifier = getMovimentosIdentifier(movimentosItem);
        if (movimentosIdentifier == null || movimentosCollectionIdentifiers.includes(movimentosIdentifier)) {
          return false;
        }
        movimentosCollectionIdentifiers.push(movimentosIdentifier);
        return true;
      });
      return [...movimentosToAdd, ...movimentosCollection];
    }
    return movimentosCollection;
  }

  protected convertDateFromClient(movimentos: IMovimentos): IMovimentos {
    return Object.assign({}, movimentos, {
      dataLocacao: movimentos.dataLocacao?.isValid() ? movimentos.dataLocacao.format(DATE_FORMAT) : undefined,
      previsaoDevolucao: movimentos.previsaoDevolucao?.isValid() ? movimentos.previsaoDevolucao.format(DATE_FORMAT) : undefined,
      dataDevolucao: movimentos.dataDevolucao?.isValid() ? movimentos.dataDevolucao.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataLocacao = res.body.dataLocacao ? dayjs(res.body.dataLocacao) : undefined;
      res.body.previsaoDevolucao = res.body.previsaoDevolucao ? dayjs(res.body.previsaoDevolucao) : undefined;
      res.body.dataDevolucao = res.body.dataDevolucao ? dayjs(res.body.dataDevolucao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((movimentos: IMovimentos) => {
        movimentos.dataLocacao = movimentos.dataLocacao ? dayjs(movimentos.dataLocacao) : undefined;
        movimentos.previsaoDevolucao = movimentos.previsaoDevolucao ? dayjs(movimentos.previsaoDevolucao) : undefined;
        movimentos.dataDevolucao = movimentos.dataDevolucao ? dayjs(movimentos.dataDevolucao) : undefined;
      });
    }
    return res;
  }
}
