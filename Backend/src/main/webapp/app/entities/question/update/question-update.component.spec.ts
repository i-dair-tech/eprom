import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { of, Subject, from } from "rxjs";

import { QuestionService } from "../service/question.service";
import { IQuestion, Question } from "../question.model";
import { IAnswerChoice } from "app/entities/answer-choice/answer-choice.model";
import { AnswerChoiceService } from "app/entities/answer-choice/service/answer-choice.service";
import { ITypeQuestion } from "app/entities/type-question/type-question.model";
import { TypeQuestionService } from "app/entities/type-question/service/type-question.service";

import { QuestionUpdateComponent } from "./question-update.component";

describe("Question Management Update Component", () => {
  let comp: QuestionUpdateComponent;
  let fixture: ComponentFixture<QuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionService: QuestionService;
  let answerChoiceService: AnswerChoiceService;
  let typeQuestionService: TypeQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionUpdateComponent],
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
      .overrideTemplate(QuestionUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(QuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionService = TestBed.inject(QuestionService);
    answerChoiceService = TestBed.inject(AnswerChoiceService);
    typeQuestionService = TestBed.inject(TypeQuestionService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("Should call AnswerChoice query and add missing value", () => {
      const question: IQuestion = { id: 456 };
      const answerChoices: IAnswerChoice[] = [{ id: 74936 }];
      question.answerChoices = answerChoices;

      const answerChoiceCollection: IAnswerChoice[] = [{ id: 72774 }];
      jest
        .spyOn(answerChoiceService, "query")
        .mockReturnValue(
          of(new HttpResponse({ body: answerChoiceCollection }))
        );
      const additionalAnswerChoices = [...answerChoices];
      const expectedCollection: IAnswerChoice[] = [
        ...additionalAnswerChoices,
        ...answerChoiceCollection,
      ];
      jest
        .spyOn(answerChoiceService, "addAnswerChoiceToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(answerChoiceService.query).toHaveBeenCalled();
      expect(
        answerChoiceService.addAnswerChoiceToCollectionIfMissing
      ).toHaveBeenCalledWith(
        answerChoiceCollection,
        ...additionalAnswerChoices
      );
      expect(comp.answerChoicesSharedCollection).toEqual(expectedCollection);
    });

    it("Should call TypeQuestion query and add missing value", () => {
      const question: IQuestion = { id: 456 };
      const typeQuestion: ITypeQuestion = { id: 4791 };
      question.typeQuestion = typeQuestion;

      const typeQuestionCollection: ITypeQuestion[] = [{ id: 20475 }];
      jest
        .spyOn(typeQuestionService, "query")
        .mockReturnValue(
          of(new HttpResponse({ body: typeQuestionCollection }))
        );
      const additionalTypeQuestions = [typeQuestion];
      const expectedCollection: ITypeQuestion[] = [
        ...additionalTypeQuestions,
        ...typeQuestionCollection,
      ];
      jest
        .spyOn(typeQuestionService, "addTypeQuestionToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(typeQuestionService.query).toHaveBeenCalled();
      expect(
        typeQuestionService.addTypeQuestionToCollectionIfMissing
      ).toHaveBeenCalledWith(
        typeQuestionCollection,
        ...additionalTypeQuestions
      );
      expect(comp.typeQuestionsSharedCollection).toEqual(expectedCollection);
    });

    it("Should update editForm", () => {
      const question: IQuestion = { id: 456 };
      const answerChoices: IAnswerChoice = { id: 42940 };
      question.answerChoices = [answerChoices];
      const typeQuestion: ITypeQuestion = { id: 63864 };
      question.typeQuestion = typeQuestion;

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(question));
      expect(comp.answerChoicesSharedCollection).toContain(answerChoices);
      expect(comp.typeQuestionsSharedCollection).toContain(typeQuestion);
    });
  });

  describe("save", () => {
    it("Should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Question>>();
      const question = { id: 123 };
      jest.spyOn(questionService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionService.update).toHaveBeenCalledWith(question);
      expect(comp.isSaving).toEqual(false);
    });

    it("Should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Question>>();
      const question = new Question();
      jest.spyOn(questionService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionService.create).toHaveBeenCalledWith(question);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("Should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Question>>();
      const question = { id: 123 };
      jest.spyOn(questionService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(questionService.update).toHaveBeenCalledWith(question);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe("Tracking relationships identifiers", () => {
    describe("trackAnswerChoiceById", () => {
      it("Should return tracked AnswerChoice primary key", () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAnswerChoiceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe("trackTypeQuestionById", () => {
      it("Should return tracked TypeQuestion primary key", () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTypeQuestionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe("Getting selected relationships", () => {
    describe("getSelectedAnswerChoice", () => {
      it("Should return option if no AnswerChoice is selected", () => {
        const option = { id: 123 };
        const result = comp.getSelectedAnswerChoice(option);
        expect(result === option).toEqual(true);
      });

      it("Should return selected AnswerChoice for according option", () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedAnswerChoice(option, [
          selected2,
          selected,
        ]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it("Should return option if this AnswerChoice is not selected", () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedAnswerChoice(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
