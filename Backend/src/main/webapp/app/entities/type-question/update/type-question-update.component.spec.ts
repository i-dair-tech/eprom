import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { of, Subject, from } from "rxjs";

import { TypeQuestionService } from "../service/type-question.service";
import { ITypeQuestion, TypeQuestion } from "../type-question.model";

import { TypeQuestionUpdateComponent } from "./type-question-update.component";

describe("TypeQuestion Management Update Component", () => {
  let comp: TypeQuestionUpdateComponent;
  let fixture: ComponentFixture<TypeQuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeQuestionService: TypeQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypeQuestionUpdateComponent],
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
      .overrideTemplate(TypeQuestionUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(TypeQuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeQuestionService = TestBed.inject(TypeQuestionService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("Should update editForm", () => {
      const typeQuestion: ITypeQuestion = { id: 456 };

      activatedRoute.data = of({ typeQuestion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(
        expect.objectContaining(typeQuestion)
      );
    });
  });

  describe("save", () => {
    it("Should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeQuestion>>();
      const typeQuestion = { id: 123 };
      jest.spyOn(typeQuestionService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ typeQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeQuestion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeQuestionService.update).toHaveBeenCalledWith(typeQuestion);
      expect(comp.isSaving).toEqual(false);
    });

    it("Should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeQuestion>>();
      const typeQuestion = new TypeQuestion();
      jest.spyOn(typeQuestionService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ typeQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeQuestion }));
      saveSubject.complete();

      // THEN
      expect(typeQuestionService.create).toHaveBeenCalledWith(typeQuestion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("Should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypeQuestion>>();
      const typeQuestion = { id: 123 };
      jest.spyOn(typeQuestionService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ typeQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(typeQuestionService.update).toHaveBeenCalledWith(typeQuestion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
