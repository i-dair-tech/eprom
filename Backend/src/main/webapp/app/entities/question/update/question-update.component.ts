import { Component, OnInit } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { FormBuilder, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { finalize, map } from "rxjs/operators";

import dayjs from "dayjs/esm";
import { DATE_TIME_FORMAT } from "app/config/input.constants";

import { IQuestion, Question } from "../question.model";
import { QuestionService } from "../service/question.service";
import { IAnswerChoice } from "app/entities/answer-choice/answer-choice.model";
import { AnswerChoiceService } from "app/entities/answer-choice/service/answer-choice.service";
import { ITypeQuestion } from "app/entities/type-question/type-question.model";
import { TypeQuestionService } from "app/entities/type-question/service/type-question.service";
import { Language } from "app/entities/enumerations/language.model";

@Component({
  selector: "jhi-question-update",
  templateUrl: "./question-update.component.html",
})
export class QuestionUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  answerChoicesSharedCollection: IAnswerChoice[] = [];
  typeQuestionsSharedCollection: ITypeQuestion[] = [];

  editForm = this.fb.group({
    id: [],
    text: [null, [Validators.required]],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    isArchived: [],
    archivedDate: [],
    language: [null, [Validators.required]],
    answerChoices: [],
    typeQuestion: [],
  });

  constructor(
    protected questionService: QuestionService,
    protected answerChoiceService: AnswerChoiceService,
    protected typeQuestionService: TypeQuestionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ question }) => {
      if (question.id === undefined) {
        const today = dayjs().startOf("day");
        question.createdDate = today;
        question.lastModifiedDate = today;
        question.archivedDate = today;
      }

      this.updateForm(question);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const question = this.createFromForm();
    if (question.id !== undefined) {
      this.subscribeToSaveResponse(this.questionService.update(question));
    } else {
      this.subscribeToSaveResponse(this.questionService.create(question));
    }
  }

  trackAnswerChoiceById(_index: number, item: IAnswerChoice): number {
    return item.id!;
  }

  trackTypeQuestionById(_index: number, item: ITypeQuestion): number {
    return item.id!;
  }

  getSelectedAnswerChoice(
    option: IAnswerChoice,
    selectedVals?: IAnswerChoice[]
  ): IAnswerChoice {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<IQuestion>>
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

  protected updateForm(question: IQuestion): void {
    this.editForm.patchValue({
      id: question.id,
      text: question.text,
      createdBy: question.createdBy,
      createdDate: question.createdDate
        ? question.createdDate.format(DATE_TIME_FORMAT)
        : null,
      lastModifiedBy: question.lastModifiedBy,
      lastModifiedDate: question.lastModifiedDate
        ? question.lastModifiedDate.format(DATE_TIME_FORMAT)
        : null,
      isArchived: question.isArchived,
      archivedDate: question.archivedDate
        ? question.archivedDate.format(DATE_TIME_FORMAT)
        : null,
      language: question.language,
      answerChoices: question.answerChoices,
      typeQuestion: question.typeQuestion,
    });

    this.answerChoicesSharedCollection =
      this.answerChoiceService.addAnswerChoiceToCollectionIfMissing(
        this.answerChoicesSharedCollection,
        ...(question.answerChoices ?? [])
      );
    this.typeQuestionsSharedCollection =
      this.typeQuestionService.addTypeQuestionToCollectionIfMissing(
        this.typeQuestionsSharedCollection,
        question.typeQuestion
      );
  }

  protected loadRelationshipsOptions(): void {
    this.answerChoiceService
      .query()
      .pipe(map((res: HttpResponse<IAnswerChoice[]>) => res.body ?? []))
      .pipe(
        map((answerChoices: IAnswerChoice[]) =>
          this.answerChoiceService.addAnswerChoiceToCollectionIfMissing(
            answerChoices,
            ...(this.editForm.get("answerChoices")!.value ?? [])
          )
        )
      )
      .subscribe(
        (answerChoices: IAnswerChoice[]) =>
          (this.answerChoicesSharedCollection = answerChoices)
      );

    this.typeQuestionService
      .query()
      .pipe(map((res: HttpResponse<ITypeQuestion[]>) => res.body ?? []))
      .pipe(
        map((typeQuestions: ITypeQuestion[]) =>
          this.typeQuestionService.addTypeQuestionToCollectionIfMissing(
            typeQuestions,
            this.editForm.get("typeQuestion")!.value
          )
        )
      )
      .subscribe(
        (typeQuestions: ITypeQuestion[]) =>
          (this.typeQuestionsSharedCollection = typeQuestions)
      );
  }

  protected createFromForm(): IQuestion {
    return {
      ...new Question(),
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
      language: this.editForm.get(["language"])!.value,
      answerChoices: this.editForm.get(["answerChoices"])!.value,
      typeQuestion: this.editForm.get(["typeQuestion"])!.value,
    };
  }
}
