import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { of, Subject, from } from "rxjs";

import { AnswerService } from "../service/answer.service";
import { IAnswer, Answer } from "../answer.model";
import { IQuestion } from "app/entities/question/question.model";
import { QuestionService } from "app/entities/question/service/question.service";

import { AnswerUpdateComponent } from "./answer-update.component";

describe("Answer Management Update Component", () => {
  let comp: AnswerUpdateComponent;
  let fixture: ComponentFixture<AnswerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let answerService: AnswerService;
  let questionService: QuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AnswerUpdateComponent],
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
      .overrideTemplate(AnswerUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(AnswerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    answerService = TestBed.inject(AnswerService);
    questionService = TestBed.inject(QuestionService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("Should call question query and add missing value", () => {
      const answer: IAnswer = { id: 456 };
      const question: IQuestion = { id: 68600 };
      answer.question = question;

      const questionCollection: IQuestion[] = [{ id: 1479 }];
      jest
        .spyOn(questionService, "query")
        .mockReturnValue(of(new HttpResponse({ body: questionCollection })));
      const expectedCollection: IQuestion[] = [question, ...questionCollection];
      jest
        .spyOn(questionService, "addQuestionToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(questionService.query).toHaveBeenCalled();
      expect(
        questionService.addQuestionToCollectionIfMissing
      ).toHaveBeenCalledWith(questionCollection, question);
      expect(comp.questionsCollection).toEqual(expectedCollection);
    });

    it("Should update editForm", () => {
      const answer: IAnswer = { id: 456 };
      const question: IQuestion = { id: 62932 };
      answer.question = question;

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(answer));
      expect(comp.questionsCollection).toContain(question);
    });
  });

  describe("save", () => {
    it("Should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Answer>>();
      const answer = { id: 123 };
      jest.spyOn(answerService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(answerService.update).toHaveBeenCalledWith(answer);
      expect(comp.isSaving).toEqual(false);
    });

    it("Should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Answer>>();
      const answer = new Answer();
      jest.spyOn(answerService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(answerService.create).toHaveBeenCalledWith(answer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("Should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Answer>>();
      const answer = { id: 123 };
      jest.spyOn(answerService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(answerService.update).toHaveBeenCalledWith(answer);
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
});
