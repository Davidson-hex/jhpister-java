import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILocar, Locar } from '../locar.model';
import { LocarService } from '../service/locar.service';

@Component({
  selector: 'jhi-locar-update',
  templateUrl: './locar-update.component.html',
})
export class LocarUpdateComponent implements OnInit {
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

  constructor(protected locarService: LocarService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locar }) => {
      this.updateForm(locar);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locar = this.createFromForm();
    if (locar.id !== undefined) {
      this.subscribeToSaveResponse(this.locarService.update(locar));
    } else {
      this.subscribeToSaveResponse(this.locarService.create(locar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocar>>): void {
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

  protected updateForm(locar: ILocar): void {
    this.editForm.patchValue({
      id: locar.id,
      idUsuario: locar.idUsuario,
      idLivro: locar.idLivro,
      locacao: locar.locacao,
      dataLocacao: locar.dataLocacao,
      previsaoDevolucao: locar.previsaoDevolucao,
      dataDevolucao: locar.dataDevolucao,
      devolucao: locar.devolucao,
      status: locar.status,
      proprietario: locar.proprietario,
    });
  }

  protected createFromForm(): ILocar {
    return {
      ...new Locar(),
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
