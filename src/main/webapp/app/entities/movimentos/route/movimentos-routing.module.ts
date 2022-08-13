import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MovimentosComponent } from '../list/movimentos.component';
import { MovimentosDetailComponent } from '../detail/movimentos-detail.component';
import { MovimentosUpdateComponent } from '../update/movimentos-update.component';
import { MovimentosRoutingResolveService } from './movimentos-routing-resolve.service';

const movimentosRoute: Routes = [
  {
    path: '',
    component: MovimentosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MovimentosDetailComponent,
    resolve: {
      movimentos: MovimentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MovimentosUpdateComponent,
    resolve: {
      movimentos: MovimentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MovimentosUpdateComponent,
    resolve: {
      movimentos: MovimentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(movimentosRoute)],
  exports: [RouterModule],
})
export class MovimentosRoutingModule {}
