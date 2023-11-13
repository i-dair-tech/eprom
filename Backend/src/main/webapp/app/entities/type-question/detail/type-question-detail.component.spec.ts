import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import { of } from "rxjs";

import { TypeQuestionDetailComponent } from "./type-question-detail.component";

describe("TypeQuestion Management Detail Component", () => {
  let comp: TypeQuestionDetailComponent;
  let fixture: ComponentFixture<TypeQuestionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypeQuestionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typeQuestion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypeQuestionDetailComponent, "")
      .compileComponents();
    fixture = TestBed.createComponent(TypeQuestionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe("OnInit", () => {
    it("Should load typeQuestion on init", () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typeQuestion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
