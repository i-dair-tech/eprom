import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpHeaders, HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { of } from "rxjs";

import { TypeQuestionService } from "../service/type-question.service";

import { TypeQuestionComponent } from "./type-question.component";

describe("TypeQuestion Management Component", () => {
  let comp: TypeQuestionComponent;
  let fixture: ComponentFixture<TypeQuestionComponent>;
  let service: TypeQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypeQuestionComponent],
    })
      .overrideTemplate(TypeQuestionComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(TypeQuestionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeQuestionService);

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
    expect(comp.typeQuestions?.[0]).toEqual(
      expect.objectContaining({ id: 123 })
    );
  });
});
