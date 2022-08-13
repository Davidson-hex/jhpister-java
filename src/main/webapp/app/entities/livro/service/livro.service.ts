import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILivro, getLivroIdentifier } from '../livro.model';

export type EntityResponseType = HttpResponse<ILivro>;
export type EntityArrayResponseType = HttpResponse<ILivro[]>;

@Injectable({ providedIn: 'root' })
export class LivroService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/livros');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(livro: ILivro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livro);
    return this.http
      .post<ILivro>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(livro: ILivro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livro);
    return this.http
      .put<ILivro>(`${this.resourceUrl}/${getLivroIdentifier(livro) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(livro: ILivro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livro);
    return this.http
      .patch<ILivro>(`${this.resourceUrl}/${getLivroIdentifier(livro) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILivro>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILivro[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLivroToCollectionIfMissing(livroCollection: ILivro[], ...livrosToCheck: (ILivro | null | undefined)[]): ILivro[] {
    const livros: ILivro[] = livrosToCheck.filter(isPresent);
    if (livros.length > 0) {
      const livroCollectionIdentifiers = livroCollection.map(livroItem => getLivroIdentifier(livroItem)!);
      const livrosToAdd = livros.filter(livroItem => {
        const livroIdentifier = getLivroIdentifier(livroItem);
        if (livroIdentifier == null || livroCollectionIdentifiers.includes(livroIdentifier)) {
          return false;
        }
        livroCollectionIdentifiers.push(livroIdentifier);
        return true;
      });
      return [...livrosToAdd, ...livroCollection];
    }
    return livroCollection;
  }

  protected convertDateFromClient(livro: ILivro): ILivro {
    return Object.assign({}, livro, {
      dataCriacao: livro.dataCriacao?.isValid() ? livro.dataCriacao.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataCriacao = res.body.dataCriacao ? dayjs(res.body.dataCriacao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((livro: ILivro) => {
        livro.dataCriacao = livro.dataCriacao ? dayjs(livro.dataCriacao) : undefined;
      });
    }
    return res;
  }
}
