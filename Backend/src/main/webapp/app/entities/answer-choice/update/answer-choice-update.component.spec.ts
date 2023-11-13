import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { of, Subject, from } from "rxjs";

import { AnswerChoiceService } from "../service/answer-choice.service";
import { IAnswerChoice, AnswerChoice } from "../answer-choice.model";

import { AnswerChoiceUpdateComponent } from "./answer-choice-update.component";

describe("AnswerChoice Management Update Component", () => {
  let comp: AnswerChoiceUpdateComponent;
  let fixture: ComponentFixture<AnswerChoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let answerChoiceService: AnswerChoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AnswerChoiceUpdateComponent],
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
      .overrideTemplate(AnswerChoiceUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(AnswerChoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    answerChoiceService = TestBed.inject(AnswerChoiceService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("Should update editForm", () => {
      const answerChoice: IAnswerChoice = { id: 456 };

      activatedRoute.data = of({ answerChoice });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(
        expect.objectContaining(answerChoice)
      );
    });
  });

  describe("save", () => {
    it("Should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AnswerChoice>>();
      const answerChoice = { id: 123 };
      jest.spyOn(answerChoiceService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ answerChoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answerChoice }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(answerChoiceService.update).toHaveBeenCalledWith(answerChoice);
      expect(comp.isSaving).toEqual(false);
    });

    it("Should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AnswerChoice>>();
      const answerChoice = new AnswerChoice();
      jest.spyOn(answerChoiceService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ answerChoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answerChoice }));
      saveSubject.complete();

      // THEN
      expect(answerChoiceService.create).toHaveBeenCalledWith(answerChoice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("Should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AnswerChoice>>();
      const answerChoice = { id: 123 };
      jest.spyOn(answerChoiceService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ answerChoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(answerChoiceService.update).toHaveBeenCalledWith(answerChoice);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
