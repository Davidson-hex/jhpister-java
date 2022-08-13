import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILivro } from '../livro.model';

@Component({
  selector: 'jhi-livro-detail',
  templateUrl: './livro-detail.component.html',
})
export class LivroDetailComponent implements OnInit {
  livro: ILivro | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livro }) => {
      this.livro = livro;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
