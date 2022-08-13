import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILivro, Livro } from '../livro.model';
import { LivroService } from '../service/livro.service';

@Component({
  selector: 'jhi-livro-update',
  templateUrl: './livro-update.component.html',
})
export class LivroUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    autor: [],
    titulo: [],
    dataCriacao: [],
    ativo: [],
    idUsuarioCadastro: [],
    proprietario: [],
  });

  constructor(protected livroService: LivroService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livro }) => {
      this.updateForm(livro);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livro = this.createFromForm();
    if (livro.id !== undefined) {
      this.subscribeToSaveResponse(this.livroService.update(livro));
    } else {
      this.subscribeToSaveResponse(this.livroService.create(livro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivro>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(livro: ILivro): void {
    this.editForm.patchValue({
      id: livro.id,
      autor: livro.autor,
      titulo: livro.titulo,
      dataCriacao: livro.dataCriacao,
      ativo: livro.ativo,
      idUsuarioCadastro: livro.idUsuarioCadastro,
      proprietario: livro.proprietario,
    });
  }

  protected createFromForm(): ILivro {
    return {
      ...new Livro(),
      id: this.editForm.get(['id'])!.value,
      autor: this.editForm.get(['autor'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      dataCriacao: this.editForm.get(['dataCriacao'])!.value,
      ativo: this.editForm.get(['ativo'])!.value,
      idUsuarioCadastro: this.editForm.get(['idUsuarioCadastro'])!.value,
      proprietario: this.editForm.get(['proprietario'])!.value,
    };
  }
}
