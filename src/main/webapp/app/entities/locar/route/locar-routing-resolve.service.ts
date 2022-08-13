import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocar, Locar } from '../locar.model';
import { LocarService } from '../service/locar.service';

@Injectable({ providedIn: 'root' })
export class LocarRoutingResolveService implements Resolve<ILocar> {
  constructor(protected service: LocarService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocar> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((locar: HttpResponse<Locar>) => {
          if (locar.body) {
            return of(locar.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Locar());
  }
}
