import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocarComponent } from '../list/locar.component';
import { LocarDetailComponent } from '../detail/locar-detail.component';
import { LocarUpdateComponent } from '../update/locar-update.component';
import { LocarRoutingResolveService } from './locar-routing-resolve.service';

const locarRoute: Routes = [
  {
    path: '',
    component: LocarComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocarDetailComponent,
    resolve: {
      locar: LocarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocarUpdateComponent,
    resolve: {
      locar: LocarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocarUpdateComponent,
    resolve: {
      locar: LocarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(locarRoute)],
  exports: [RouterModule],
})
export class LocarRoutingModule {}
