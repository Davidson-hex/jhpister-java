import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MovimentosComponent } from './list/movimentos.component';
import { MovimentosDetailComponent } from './detail/movimentos-detail.component';
import { MovimentosUpdateComponent } from './update/movimentos-update.component';
import { MovimentosDeleteDialogComponent } from './delete/movimentos-delete-dialog.component';
import { MovimentosRoutingModule } from './route/movimentos-routing.module';

@NgModule({
  imports: [SharedModule, MovimentosRoutingModule],
  declarations: [MovimentosComponent, MovimentosDetailComponent, MovimentosUpdateComponent, MovimentosDeleteDialogComponent],
  entryComponents: [MovimentosDeleteDialogComponent],
})
export class MovimentosModule {}
