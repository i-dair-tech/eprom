import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import { of } from "rxjs";

import { AnswerChoiceDetailComponent } from "./answer-choice-detail.component";

describe("AnswerChoice Management Detail Component", () => {
  let comp: AnswerChoiceDetailComponent;
  let fixture: ComponentFixture<AnswerChoiceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnswerChoiceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ answerChoice: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AnswerChoiceDetailComponent, "")
      .compileComponents();
    fixture = TestBed.createComponent(AnswerChoiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe("OnInit", () => {
    it("Should load answerChoice on init", () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.answerChoice).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
