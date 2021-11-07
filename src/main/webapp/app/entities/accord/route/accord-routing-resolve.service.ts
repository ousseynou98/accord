import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccord, Accord } from '../accord.model';
import { AccordService } from '../service/accord.service';

@Injectable({ providedIn: 'root' })
export class AccordRoutingResolveService implements Resolve<IAccord> {
  constructor(protected service: AccordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accord: HttpResponse<Accord>) => {
          if (accord.body) {
            return of(accord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Accord());
  }
}
