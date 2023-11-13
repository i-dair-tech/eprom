import { Component, OnInit } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { FormBuilder, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { finalize, map } from "rxjs/operators";

import dayjs from "dayjs/esm";
import { DATE_TIME_FORMAT } from "app/config/input.constants";

import { ISurvey, Survey } from "../survey.model";
import { SurveyService } from "../service/survey.service";
import { IQuestion } from "app/entities/question/question.model";
import { QuestionService } from "app/entities/question/service/question.service";

@Component({
  selector: "jhi-survey-update",
  templateUrl: "./survey-update.component.html",
})
export class SurveyUpdateComponent implements OnInit {
  isSaving = false;

  questionsSharedCollection: IQuestion[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    type: [null, [Validators.required]],
    description: [],
    questionOrder: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    isArchived: [],
    archivedDate: [],
    questions: [],
  });

  constructor(
    protected surveyService: SurveyService,
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ survey }) => {
      if (survey.id === undefined) {
        const today = dayjs().startOf("day");
        survey.createdDate = today;
        survey.lastModifiedDate = today;
        survey.archivedDate = today;
      }

      this.updateForm(survey);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const survey = this.createFromForm();
    if (survey.id !== undefined) {
      this.subscribeToSaveResponse(this.surveyService.update(survey));
    } else {
      this.subscribeToSaveResponse(this.surveyService.create(survey));
    }
  }

  trackQuestionById(_index: number, item: IQuestion): number {
    return item.id!;
  }

  getSelectedQuestion(
    option: IQuestion,
    selectedVals?: IQuestion[]
  ): IQuestion {
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
    result: Observable<HttpResponse<ISurvey>>
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

  protected updateForm(survey: ISurvey): void {
    this.editForm.patchValue({
      id: survey.id,
      name: survey.name,
      type: survey.type,
      description: survey.description,
      questionOrder: survey.questionOrder,
      createdBy: survey.createdBy,
      createdDate: survey.createdDate
        ? survey.createdDate.format(DATE_TIME_FORMAT)
        : null,
      lastModifiedBy: survey.lastModifiedBy,
      lastModifiedDate: survey.lastModifiedDate
        ? survey.lastModifiedDate.format(DATE_TIME_FORMAT)
        : null,
      isArchived: survey.isArchived,
      archivedDate: survey.archivedDate
        ? survey.archivedDate.format(DATE_TIME_FORMAT)
        : null,
      questions: survey.questions,
    });

    this.questionsSharedCollection =
      this.questionService.addQuestionToCollectionIfMissing(
        this.questionsSharedCollection,
        ...(survey.questions ?? [])
      );
  }

  protected loadRelationshipsOptions(): void {
    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing(
            questions,
            ...(this.editForm.get("questions")!.value ?? [])
          )
        )
      )
      .subscribe(
        (questions: IQuestion[]) => (this.questionsSharedCollection = questions)
      );
  }

  protected createFromForm(): ISurvey {
    return {
      ...new Survey(),
      id: this.editForm.get(["id"])!.value,
      name: this.editForm.get(["name"])!.value,
      type: this.editForm.get(["type"])!.value,
      description: this.editForm.get(["description"])!.value,
      questionOrder: this.editForm.get(["questionOrder"])!.value,
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
      questions: this.editForm.get(["questions"])!.value,
    };
  }
}
