import { Component, OnInit } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { FormBuilder, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { finalize } from "rxjs/operators";

import dayjs from "dayjs/esm";
import { DATE_TIME_FORMAT } from "app/config/input.constants";

import { ITypeQuestion, TypeQuestion } from "../type-question.model";
import { TypeQuestionService } from "../service/type-question.service";

@Component({
  selector: "jhi-type-question-update",
  templateUrl: "./type-question-update.component.html",
})
export class TypeQuestionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    text: [null, [Validators.required]],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    isArchived: [],
    archivedDate: [],
  });

  constructor(
    protected typeQuestionService: TypeQuestionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeQuestion }) => {
      if (typeQuestion.id === undefined) {
        const today = dayjs().startOf("day");
        typeQuestion.createdDate = today;
        typeQuestion.lastModifiedDate = today;
        typeQuestion.archivedDate = today;
      }

      this.updateForm(typeQuestion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeQuestion = this.createFromForm();
    if (typeQuestion.id !== undefined) {
      this.subscribeToSaveResponse(
        this.typeQuestionService.update(typeQuestion)
      );
    } else {
      this.subscribeToSaveResponse(
        this.typeQuestionService.create(typeQuestion)
      );
    }
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<ITypeQuestion>>
  ): void {
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

  protected updateForm(typeQuestion: ITypeQuestion): void {
    this.editForm.patchValue({
      id: typeQuestion.id,
      text: typeQuestion.text,
      createdBy: typeQuestion.createdBy,
      createdDate: typeQuestion.createdDate
        ? typeQuestion.createdDate.format(DATE_TIME_FORMAT)
        : null,
      lastModifiedBy: typeQuestion.lastModifiedBy,
      lastModifiedDate: typeQuestion.lastModifiedDate
        ? typeQuestion.lastModifiedDate.format(DATE_TIME_FORMAT)
        : null,
      isArchived: typeQuestion.isArchived,
      archivedDate: typeQuestion.archivedDate
        ? typeQuestion.archivedDate.format(DATE_TIME_FORMAT)
        : null,
    });
  }

  protected createFromForm(): ITypeQuestion {
    return {
      ...new TypeQuestion(),
      id: this.editForm.get(["id"])!.value,
      text: this.editForm.get(["text"])!.value,
      createdBy: this.editForm.get(["createdBy"])!.value,
      createdDate: this.editForm.get(["createdDate"])!.value
        ? dayjs(this.editForm.get(["createdDate"])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(["lastModifiedBy"])!.value,
      lastModifiedDate: this.editForm.get(["lastModifiedDate"])!.value
        ? dayjs(
            this.editForm.get(["lastModifiedDate"])!.value,
            DATE_TIME_FORMAT
          )
        : undefined,
      isArchived: this.editForm.get(["isArchived"])!.value,
      archivedDate: this.editForm.get(["archivedDate"])!.value
        ? dayjs(this.editForm.get(["archivedDate"])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
