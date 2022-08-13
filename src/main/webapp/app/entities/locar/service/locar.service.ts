import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocar, getLocarIdentifier } from '../locar.model';

export type EntityResponseType = HttpResponse<ILocar>;
export type EntityArrayResponseType = HttpResponse<ILocar[]>;

@Injectable({ providedIn: 'root' })
export class LocarService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/locars');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(locar: ILocar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locar);
    return this.http
      .post<ILocar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(locar: ILocar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locar);
    return this.http
      .put<ILocar>(`${this.resourceUrl}/${getLocarIdentifier(locar) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(locar: ILocar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locar);
    return this.http
      .patch<ILocar>(`${this.resourceUrl}/${getLocarIdentifier(locar) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILocar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILocar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocarToCollectionIfMissing(locarCollection: ILocar[], ...locarsToCheck: (ILocar | null | undefined)[]): ILocar[] {
    const locars: ILocar[] = locarsToCheck.filter(isPresent);
    if (locars.length > 0) {
      const locarCollectionIdentifiers = locarCollection.map(locarItem => getLocarIdentifier(locarItem)!);
      const locarsToAdd = locars.filter(locarItem => {
        const locarIdentifier = getLocarIdentifier(locarItem);
        if (locarIdentifier == null || locarCollectionIdentifiers.includes(locarIdentifier)) {
          return false;
        }
        locarCollectionIdentifiers.push(locarIdentifier);
        return true;
      });
      return [...locarsToAdd, ...locarCollection];
    }
    return locarCollection;
  }

  protected convertDateFromClient(locar: ILocar): ILocar {
    return Object.assign({}, locar, {
      dataLocacao: locar.dataLocacao?.isValid() ? locar.dataLocacao.format(DATE_FORMAT) : undefined,
      previsaoDevolucao: locar.previsaoDevolucao?.isValid() ? locar.previsaoDevolucao.format(DATE_FORMAT) : undefined,
      dataDevolucao: locar.dataDevolucao?.isValid() ? locar.dataDevolucao.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((locar: ILocar) => {
        locar.dataLocacao = locar.dataLocacao ? dayjs(locar.dataLocacao) : undefined;
        locar.previsaoDevolucao = locar.previsaoDevolucao ? dayjs(locar.previsaoDevolucao) : undefined;
        locar.dataDevolucao = locar.dataDevolucao ? dayjs(locar.dataDevolucao) : undefined;
      });
    }
    return res;
  }
}
