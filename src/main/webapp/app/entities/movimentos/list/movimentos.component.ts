import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMovimentos } from '../movimentos.model';
import { MovimentosService } from '../service/movimentos.service';
import { MovimentosDeleteDialogComponent } from '../delete/movimentos-delete-dialog.component';

@Component({
  selector: 'jhi-movimentos',
  templateUrl: './movimentos.component.html',
})
export class MovimentosComponent implements OnInit {
  movimentos?: IMovimentos[];
  isLoading = false;

  constructor(protected movimentosService: MovimentosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.movimentosService.query().subscribe({
      next: (res: HttpResponse<IMovimentos[]>) => {
        this.isLoading = false;
        this.movimentos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMovimentos): number {
    return item.id!;
  }

  delete(movimentos: IMovimentos): void {
    const modalRef = this.modalService.open(MovimentosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.movimentos = movimentos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
