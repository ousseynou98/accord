import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAccord, Accord } from '../accord.model';
import { AccordService } from '../service/accord.service';
import { ValidationDC } from 'app/entities/enumerations/validation-dc.model';
import { ValidationR } from 'app/entities/enumerations/validation-r.model';

@Component({
  selector: 'jhi-accord-update',
  templateUrl: './accord-update.component.html',
})
export class AccordUpdateComponent implements OnInit {
  isSaving = false;
  validationDCValues = Object.keys(ValidationDC);
  validationRValues = Object.keys(ValidationR);

  editForm = this.fb.group({
    id: [],
    partenaire: [],
    domaine: [],
    description: [],
    date: [],
    dure: [],
    zone: [],
    type: [],
    nature: [],
    validationDircoop: [],
    validationRecteur: [],
  });

  constructor(protected accordService: AccordService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accord }) => {
      if (accord.id === undefined) {
        const today = dayjs().startOf('day');
        accord.date = today;
      }

      this.updateForm(accord);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accord = this.createFromForm();
    if (accord.id !== undefined) {
      this.subscribeToSaveResponse(this.accordService.update(accord));
    } else {
      this.subscribeToSaveResponse(this.accordService.create(accord));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccord>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(accord: IAccord): void {
    this.editForm.patchValue({
      id: accord.id,
      partenaire: accord.partenaire,
      domaine: accord.domaine,
      description: accord.description,
      date: accord.date ? accord.date.format(DATE_TIME_FORMAT) : null,
      dure: accord.dure,
      zone: accord.zone,
      type: accord.type,
      nature: accord.nature,
      validationDircoop: accord.validationDircoop,
      validationRecteur: accord.validationRecteur,
    });
  }

  protected createFromForm(): IAccord {
    return {
      ...new Accord(),
      id: this.editForm.get(['id'])!.value,
      partenaire: this.editForm.get(['partenaire'])!.value,
      domaine: this.editForm.get(['domaine'])!.value,
      description: this.editForm.get(['description'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      dure: this.editForm.get(['dure'])!.value,
      zone: this.editForm.get(['zone'])!.value,
      type: this.editForm.get(['type'])!.value,
      nature: this.editForm.get(['nature'])!.value,
      validationDircoop: this.editForm.get(['validationDircoop'])!.value,
      validationRecteur: this.editForm.get(['validationRecteur'])!.value,
    };
  }
}
