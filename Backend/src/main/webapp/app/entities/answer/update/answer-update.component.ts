import { Component, OnInit } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { finalize, map } from "rxjs/operators";

import dayjs from "dayjs/esm";
import { DATE_TIME_FORMAT } from "app/config/input.constants";

import { IAnswer, Answer } from "../answer.model";
import { AnswerService } from "../service/answer.service";
import { IQuestion } from "app/entities/question/question.model";
import { QuestionService } from "app/entities/question/service/question.service";

@Component({
  selector: "jhi-answer-update",
  templateUrl: "./answer-update.component.html",
})
export class AnswerUpdateComponent implements OnInit {
  isSaving = false;

  questionsCollection: IQuestion[] = [];

  editForm = this.fb.group({
    id: [],
    text: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    isArchived: [],
    archivedDate: [],
    question: [],
  });

  constructor(
    protected answerService: AnswerService,
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answer }) => {
      if (answer.id === undefined) {
        const today = dayjs().startOf("day");
        answer.createdDate = today;
        answer.lastModifiedDate = today;
        answer.archivedDate = today;
      }

      this.updateForm(answer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answer = this.createFromForm();
    if (answer.id !== undefined) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  trackQuestionById(_index: number, item: IQuestion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<IAnswer>>
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

  protected updateForm(answer: IAnswer): void {
    this.editForm.patchValue({
      id: answer.id,
      text: answer.text,
      createdBy: answer.createdBy,
      createdDate: answer.createdDate
        ? answer.createdDate.format(DATE_TIME_FORMAT)
        : null,
      lastModifiedBy: answer.lastModifiedBy,
      lastModifiedDate: answer.lastModifiedDate
        ? answer.lastModifiedDate.format(DATE_TIME_FORMAT)
        : null,
      isArchived: answer.isArchived,
      archivedDate: answer.archivedDate
        ? answer.archivedDate.format(DATE_TIME_FORMAT)
        : null,
      question: answer.question,
    });

    this.questionsCollection =
      this.questionService.addQuestionToCollectionIfMissing(
        this.questionsCollection,
        answer.question
      );
  }

  protected loadRelationshipsOptions(): void {
    this.questionService
      .query({ filter: "answer-is-null" })
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing(
            questions,
            this.editForm.get("question")!.value
          )
        )
      )
      .subscribe(
        (questions: IQuestion[]) => (this.questionsCollection = questions)
      );
  }

  protected createFromForm(): IAnswer {
    return {
      ...new Answer(),
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
      question: this.editForm.get(["question"])!.value,
    };
  }
}
