import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { of, Subject, from } from "rxjs";

import { SurveyService } from "../service/survey.service";
import { ISurvey, Survey } from "../survey.model";
import { IQuestion } from "app/entities/question/question.model";
import { QuestionService } from "app/entities/question/service/question.service";

import { SurveyUpdateComponent } from "./survey-update.component";

describe("Survey Management Update Component", () => {
  let comp: SurveyUpdateComponent;
  let fixture: ComponentFixture<SurveyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let surveyService: SurveyService;
  let questionService: QuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SurveyUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SurveyUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(SurveyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    surveyService = TestBed.inject(SurveyService);
    questionService = TestBed.inject(QuestionService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("Should call Question query and add missing value", () => {
      const survey: ISurvey = { id: 456 };
      const questions: IQuestion[] = [{ id: 24104 }];
      survey.questions = questions;

      const questionCollection: IQuestion[] = [{ id: 5891 }];
      jest
        .spyOn(questionService, "query")
        .mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const additionalQuestions = [...questions];
      const expectedCollection: IQuestion[] = [
        ...additionalQuestions,
        ...questionCollection,
      ];
      jest
        .spyOn(questionService, "addQuestionToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ survey });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(
        questionService.addQuestionToCollectionIfMissing
      ).toHaveBeenCalledWith(questionCollection, ...additionalQuestions);
      expect(comp.questionsSharedCollection).toEqual(expectedCollection);
    });

    it("Should update editForm", () => {
      const survey: ISurvey = { id: 456 };
      const questions: IQuestion = { id: 21411 };
      survey.questions = [questions];

      activatedRoute.data = of({ survey });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(survey));
      expect(comp.questionsSharedCollection).toContain(questions);
    });
  });

  describe("save", () => {
    it("Should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Survey>>();
      const survey = { id: 123 };
      jest.spyOn(surveyService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ survey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: survey }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(surveyService.update).toHaveBeenCalledWith(survey);
      expect(comp.isSaving).toEqual(false);
    });

    it("Should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Survey>>();
      const survey = new Survey();
      jest.spyOn(surveyService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ survey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: survey }));
      saveSubject.complete();

      // THEN
      expect(surveyService.create).toHaveBeenCalledWith(survey);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("Should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Survey>>();
      const survey = { id: 123 };
      jest.spyOn(surveyService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ survey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(surveyService.update).toHaveBeenCalledWith(survey);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe("Tracking relationships identifiers", () => {
    describe("trackQuestionById", () => {
      it("Should return tracked Question primary key", () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuestionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe("Getting selected relationships", () => {
    describe("getSelectedQuestion", () => {
      it("Should return option if no Question is selected", () => {
        const option = { id: 123 };
        const result = comp.getSelectedQuestion(option);
        expect(result === option).toEqual(true);
      });

      it("Should return selected Question for according option", () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedQuestion(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it("Should return option if this Question is not selected", () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedQuestion(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
