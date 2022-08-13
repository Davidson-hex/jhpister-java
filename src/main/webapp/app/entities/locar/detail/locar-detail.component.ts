import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocar } from '../locar.model';

@Component({
  selector: 'jhi-locar-detail',
  templateUrl: './locar-detail.component.html',
})
export class LocarDetailComponent implements OnInit {
  locar: ILocar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locar }) => {
      this.locar = locar;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
