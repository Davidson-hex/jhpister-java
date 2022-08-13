import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMovimentos, Movimentos } from '../movimentos.model';
import { MovimentosService } from '../service/movimentos.service';

@Component({
  selector: 'jhi-movimentos-update',
  templateUrl: './movimentos-update.component.html',
})
export class MovimentosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idUsuario: [],
    idLivro: [],
    locacao: [],
    dataLocacao: [],
    previsaoDevolucao: [],
    dataDevolucao: [],
    devolucao: [],
    status: [],
    proprietario: [],
  });

  constructor(protected movimentosService: MovimentosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ movimentos }) => {
      this.updateForm(movimentos);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const movimentos = this.createFromForm();
    if (movimentos.id !== undefined) {
      this.subscribeToSaveResponse(this.movimentosService.update(movimentos));
    } else {
      this.subscribeToSaveResponse(this.movimentosService.create(movimentos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovimentos>>): void {
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

  protected updateForm(movimentos: IMovimentos): void {
    this.editForm.patchValue({
      id: movimentos.id,
      idUsuario: movimentos.idUsuario,
      idLivro: movimentos.idLivro,
      locacao: movimentos.locacao,
      dataLocacao: movimentos.dataLocacao,
      previsaoDevolucao: movimentos.previsaoDevolucao,
      dataDevolucao: movimentos.dataDevolucao,
      devolucao: movimentos.devolucao,
      status: movimentos.status,
      proprietario: movimentos.proprietario,
    });
  }

  protected createFromForm(): IMovimentos {
    return {
      ...new Movimentos(),
      id: this.editForm.get(['id'])!.value,
      idUsuario: this.editForm.get(['idUsuario'])!.value,
      idLivro: this.editForm.get(['idLivro'])!.value,
      locacao: this.editForm.get(['locacao'])!.value,
      dataLocacao: this.editForm.get(['dataLocacao'])!.value,
      previsaoDevolucao: this.editForm.get(['previsaoDevolucao'])!.value,
      dataDevolucao: this.editForm.get(['dataDevolucao'])!.value,
      devolucao: this.editForm.get(['devolucao'])!.value,
      status: this.editForm.get(['status'])!.value,
      proprietario: this.editForm.get(['proprietario'])!.value,
    };
  }
}
