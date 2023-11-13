import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpHeaders, HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { of } from "rxjs";

import { AnswerChoiceService } from "../service/answer-choice.service";

import { AnswerChoiceComponent } from "./answer-choice.component";

describe("AnswerChoice Management Component", () => {
  let comp: AnswerChoiceComponent;
  let fixture: ComponentFixture<AnswerChoiceComponent>;
  let service: AnswerChoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AnswerChoiceComponent],
    })
      .overrideTemplate(AnswerChoiceComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(AnswerChoiceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AnswerChoiceService);

    const headers = new HttpHeaders();
    jest.spyOn(service, "query").mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it("Should call load all on init", () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.answerChoices?.[0]).toEqual(
      expect.objectContaining({ id: 123 })
    );
  });
});
