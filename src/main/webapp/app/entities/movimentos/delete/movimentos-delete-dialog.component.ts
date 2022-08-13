import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMovimentos } from '../movimentos.model';
import { MovimentosService } from '../service/movimentos.service';

@Component({
  templateUrl: './movimentos-delete-dialog.component.html',
})
export class MovimentosDeleteDialogComponent {
  movimentos?: IMovimentos;

  constructor(protected movimentosService: MovimentosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.movimentosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
