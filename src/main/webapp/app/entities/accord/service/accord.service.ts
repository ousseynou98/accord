import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccord, getAccordIdentifier } from '../accord.model';

export type EntityResponseType = HttpResponse<IAccord>;
export type EntityArrayResponseType = HttpResponse<IAccord[]>;

@Injectable({ providedIn: 'root' })
export class AccordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accords');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accord: IAccord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accord);
    return this.http
      .post<IAccord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(accord: IAccord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accord);
    return this.http
      .put<IAccord>(`${this.resourceUrl}/${getAccordIdentifier(accord) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(accord: IAccord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accord);
    return this.http
      .patch<IAccord>(`${this.resourceUrl}/${getAccordIdentifier(accord) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAccord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAccordToCollectionIfMissing(accordCollection: IAccord[], ...accordsToCheck: (IAccord | null | undefined)[]): IAccord[] {
    const accords: IAccord[] = accordsToCheck.filter(isPresent);
    if (accords.length > 0) {
      const accordCollectionIdentifiers = accordCollection.map(accordItem => getAccordIdentifier(accordItem)!);
      const accordsToAdd = accords.filter(accordItem => {
        const accordIdentifier = getAccordIdentifier(accordItem);
        if (accordIdentifier == null || accordCollectionIdentifiers.includes(accordIdentifier)) {
          return false;
        }
        accordCollectionIdentifiers.push(accordIdentifier);
        return true;
      });
      return [...accordsToAdd, ...accordCollection];
    }
    return accordCollection;
  }

  protected convertDateFromClient(accord: IAccord): IAccord {
    return Object.assign({}, accord, {
      date: accord.date?.isValid() ? accord.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((accord: IAccord) => {
        accord.date = accord.date ? dayjs(accord.date) : undefined;
      });
    }
    return res;
  }
}
