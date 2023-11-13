import { TestBed } from "@angular/core/testing";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";
import dayjs from "dayjs/esm";

import { DATE_TIME_FORMAT } from "app/config/input.constants";
import { IAnswer, Answer } from "../answer.model";

import { AnswerService } from "./answer.service";

describe("Answer Service", () => {
  let service: AnswerService;
  let httpMock: HttpTestingController;
  let elemDefault: IAnswer;
  let expectedResult: IAnswer | IAnswer[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AnswerService);
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

    it("should create a Answer", () => {
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
        .create(new Answer())
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a Answer", () => {
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

    it("should partial update a Answer", () => {
      const patchObject = Object.assign(
        {
          text: "BBBBBB",
          createdBy: "BBBBBB",
          lastModifiedBy: "BBBBBB",
          isArchived: true,
        },
        new Answer()
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

    it("should return a list of Answer", () => {
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

    it("should delete a Answer", () => {
      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe("addAnswerToCollectionIfMissing", () => {
      it("should add a Answer to an empty array", () => {
        const answer: IAnswer = { id: 123 };
        expectedResult = service.addAnswerToCollectionIfMissing([], answer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(answer);
      });

      it("should not add a Answer to an array that contains it", () => {
        const answer: IAnswer = { id: 123 };
        const answerCollection: IAnswer[] = [
          {
            ...answer,
          },
          { id: 456 },
        ];
        expectedResult = service.addAnswerToCollectionIfMissing(
          answerCollection,
          answer
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Answer to an array that doesn't contain it", () => {
        const answer: IAnswer = { id: 123 };
        const answerCollection: IAnswer[] = [{ id: 456 }];
        expectedResult = service.addAnswerToCollectionIfMissing(
          answerCollection,
          answer
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(answer);
      });

      it("should add only unique Answer to an array", () => {
        const answerArray: IAnswer[] = [
          { id: 123 },
          { id: 456 },
          { id: 40391 },
        ];
        const answerCollection: IAnswer[] = [{ id: 123 }];
        expectedResult = service.addAnswerToCollectionIfMissing(
          answerCollection,
          ...answerArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const answer: IAnswer = { id: 123 };
        const answer2: IAnswer = { id: 456 };
        expectedResult = service.addAnswerToCollectionIfMissing(
          [],
          answer,
          answer2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(answer);
        expect(expectedResult).toContain(answer2);
      });

      it("should accept null and undefined values", () => {
        const answer: IAnswer = { id: 123 };
        expectedResult = service.addAnswerToCollectionIfMissing(
          [],
          null,
          answer,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(answer);
      });

      it("should return initial array if no Answer is added", () => {
        const answerCollection: IAnswer[] = [{ id: 123 }];
        expectedResult = service.addAnswerToCollectionIfMissing(
          answerCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(answerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
