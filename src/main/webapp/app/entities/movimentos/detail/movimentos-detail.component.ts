import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMovimentos } from '../movimentos.model';

@Component({
  selector: 'jhi-movimentos-detail',
  templateUrl: './movimentos-detail.component.html',
})
export class MovimentosDetailComponent implements OnInit {
  movimentos: IMovimentos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ movimentos }) => {
      this.movimentos = movimentos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
