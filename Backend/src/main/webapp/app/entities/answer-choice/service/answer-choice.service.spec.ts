import { TestBed } from "@angular/core/testing";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";
import dayjs from "dayjs/esm";

import { DATE_TIME_FORMAT } from "app/config/input.constants";
import { IAnswerChoice, AnswerChoice } from "../answer-choice.model";

import { AnswerChoiceService } from "./answer-choice.service";

describe("AnswerChoice Service", () => {
  let service: AnswerChoiceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAnswerChoice;
  let expectedResult: IAnswerChoice | IAnswerChoice[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AnswerChoiceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      text: "AAAAAAA",
      createdBy: "AAAAAAA",
      createdDate: currentDate,
      lastModifiedBy: "AAAAAAA",
      lastModifiedDate: currentDate,
      isArchived: false,
      archivedDate: currentDate,
    };
  });

  describe("Service methods", () => {
    it("should find an element", () => {
      const returnedFromService = Object.assign(
        {
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          archivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it("should create a AnswerChoice", () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          archivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
          archivedDate: currentDate,
        },
        returnedFromService
      );

      service
        .create(new AnswerChoice())
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a AnswerChoice", () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          text: "BBBBBB",
          createdBy: "BBBBBB",
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: "BBBBBB",
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          isArchived: true,
          archivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
          archivedDate: currentDate,
        },
        returnedFromService
      );

      service
        .update(expected)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PUT" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should partial update a AnswerChoice", () => {
      const patchObject = Object.assign(
        {
          text: "BBBBBB",
          createdBy: "BBBBBB",
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          isArchived: true,
          archivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new AnswerChoice()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
          archivedDate: currentDate,
        },
        returnedFromService
      );

      service
        .partialUpdate(patchObject)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PATCH" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should return a list of AnswerChoice", () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          text: "BBBBBB",
          createdBy: "BBBBBB",
          createdDate: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: "BBBBBB",
          lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          isArchived: true,
          archivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          lastModifiedDate: currentDate,
          archivedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it("should delete a AnswerChoice", () => {
      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe("addAnswerChoiceToCollectionIfMissing", () => {
      it("should add a AnswerChoice to an empty array", () => {
        const answerChoice: IAnswerChoice = { id: 123 };
        expectedResult = service.addAnswerChoiceToCollectionIfMissing(
          [],
          answerChoice
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(answerChoice);
      });

      it("should not add a AnswerChoice to an array that contains it", () => {
        const answerChoice: IAnswerChoice = { id: 123 };
        const answerChoiceCollection: IAnswerChoice[] = [
          {
            ...answerChoice,
          },
          { id: 456 },
        ];
        expectedResult = service.addAnswerChoiceToCollectionIfMissing(
          answerChoiceCollection,
          answerChoice
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AnswerChoice to an array that doesn't contain it", () => {
        const answerChoice: IAnswerChoice = { id: 123 };
        const answerChoiceCollection: IAnswerChoice[] = [{ id: 456 }];
        expectedResult = service.addAnswerChoiceToCollectionIfMissing(
          answerChoiceCollection,
          answerChoice
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(answerChoice);
      });

      it("should add only unique AnswerChoice to an array", () => {
        const answerChoiceArray: IAnswerChoice[] = [
          { id: 123 },
          { id: 456 },
          { id: 84381 },
        ];
        const answerChoiceCollection: IAnswerChoice[] = [{ id: 123 }];
        expectedResult = service.addAnswerChoiceToCollectionIfMissing(
          answerChoiceCollection,
          ...answerChoiceArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const answerChoice: IAnswerChoice = { id: 123 };
        const answerChoice2: IAnswerChoice = { id: 456 };
        expectedResult = service.addAnswerChoiceToCollectionIfMissing(
          [],
          answerChoice,
          answerChoice2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(answerChoice);
        expect(expectedResult).toContain(answerChoice2);
      });

      it("should accept null and undefined values", () => {
        const answerChoice: IAnswerChoice = { id: 123 };
        expectedResult = service.addAnswerChoiceToCollectionIfMissing(
          [],
          null,
          answerChoice,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(answerChoice);
      });

      it("should return initial array if no AnswerChoice is added", () => {
        const answerChoiceCollection: IAnswerChoice[] = [{ id: 123 }];
        expectedResult = service.addAnswerChoiceToCollectionIfMissing(
          answerChoiceCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(answerChoiceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
