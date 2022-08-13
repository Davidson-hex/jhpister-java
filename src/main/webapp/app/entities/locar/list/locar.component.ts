import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocar } from '../locar.model';
import { LocarService } from '../service/locar.service';
import { LocarDeleteDialogComponent } from '../delete/locar-delete-dialog.component';

@Component({
  selector: 'jhi-locar',
  templateUrl: './locar.component.html',
})
export class LocarComponent implements OnInit {
  locars?: ILocar[];
  isLoading = false;

  constructor(protected locarService: LocarService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.locarService.query().subscribe({
      next: (res: HttpResponse<ILocar[]>) => {
        this.isLoading = false;
        this.locars = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILocar): number {
    return item.id!;
  }

  delete(locar: ILocar): void {
    const modalRef = this.modalService.open(LocarDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.locar = locar;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
