import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LocarComponent } from './list/locar.component';
import { LocarDetailComponent } from './detail/locar-detail.component';
import { LocarUpdateComponent } from './update/locar-update.component';
import { LocarDeleteDialogComponent } from './delete/locar-delete-dialog.component';
import { LocarRoutingModule } from './route/locar-routing.module';

@NgModule({
  imports: [SharedModule, LocarRoutingModule],
  declarations: [LocarComponent, LocarDetailComponent, LocarUpdateComponent, LocarDeleteDialogComponent],
  entryComponents: [LocarDeleteDialogComponent],
})
export class LocarModule {}
