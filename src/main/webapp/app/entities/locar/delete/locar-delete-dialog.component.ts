import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocar } from '../locar.model';
import { LocarService } from '../service/locar.service';

@Component({
  templateUrl: './locar-delete-dialog.component.html',
})
export class LocarDeleteDialogComponent {
  locar?: ILocar;

  constructor(protected locarService: LocarService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locarService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
