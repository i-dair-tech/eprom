import { Component, OnInit } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { finalize } from "rxjs/operators";

import dayjs from "dayjs/esm";
import { DATE_TIME_FORMAT } from "app/config/input.constants";

import { IAnswerChoice, AnswerChoice } from "../answer-choice.model";
import { AnswerChoiceService } from "../service/answer-choice.service";

@Component({
  selector: "jhi-answer-choice-update",
  templateUrl: "./answer-choice-update.component.html",
})
export class AnswerChoiceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    text: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    isArchived: [],
    archivedDate: [],
  });

  constructor(
    protected answerChoiceService: AnswerChoiceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answerChoice }) => {
      if (answerChoice.id === undefined) {
        const today = dayjs().startOf("day");
        answerChoice.createdDate = today;
        answerChoice.lastModifiedDate = today;
        answerChoice.archivedDate = today;
      }

      this.updateForm(answerChoice);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answerChoice = this.createFromForm();
    if (answerChoice.id !== undefined) {
      this.subscribeToSaveResponse(
        this.answerChoiceService.update(answerChoice)
      );
    } else {
      this.subscribeToSaveResponse(
        this.answerChoiceService.create(answerChoice)
      );
    }
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<IAnswerChoice>>
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

  protected updateForm(answerChoice: IAnswerChoice): void {
    this.editForm.patchValue({
      id: answerChoice.id,
      text: answerChoice.text,
      createdBy: answerChoice.createdBy,
      createdDate: answerChoice.createdDate
        ? answerChoice.createdDate.format(DATE_TIME_FORMAT)
        : null,
      lastModifiedBy: answerChoice.lastModifiedBy,
      lastModifiedDate: answerChoice.lastModifiedDate
        ? answerChoice.lastModifiedDate.format(DATE_TIME_FORMAT)
        : null,
      isArchived: answerChoice.isArchived,
      archivedDate: answerChoice.archivedDate
        ? answerChoice.archivedDate.format(DATE_TIME_FORMAT)
        : null,
    });
  }

  protected createFromForm(): IAnswerChoice {
    return {
      ...new AnswerChoice(),
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
