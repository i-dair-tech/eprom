import { TestBed } from "@angular/core/testing";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";
import dayjs from "dayjs/esm";

import { DATE_TIME_FORMAT } from "app/config/input.constants";
import { ITypeQuestion, TypeQuestion } from "../type-question.model";

import { TypeQuestionService } from "./type-question.service";

describe("TypeQuestion Service", () => {
  let service: TypeQuestionService;
  let httpMock: HttpTestingController;
  let elemDefault: ITypeQuestion;
  let expectedResult: ITypeQuestion | ITypeQuestion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeQuestionService);
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

    it("should create a TypeQuestion", () => {
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
        .create(new TypeQuestion())
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a TypeQuestion", () => {
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

    it("should partial update a TypeQuestion", () => {
      const patchObject = Object.assign(
        {
          createdBy: "BBBBBB",
        },
        new TypeQuestion()
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

    it("should return a list of TypeQuestion", () => {
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

    it("should delete a TypeQuestion", () => {
      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe("addTypeQuestionToCollectionIfMissing", () => {
      it("should add a TypeQuestion to an empty array", () => {
        const typeQuestion: ITypeQuestion = { id: 123 };
        expectedResult = service.addTypeQuestionToCollectionIfMissing(
          [],
          typeQuestion
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeQuestion);
      });

      it("should not add a TypeQuestion to an array that contains it", () => {
        const typeQuestion: ITypeQuestion = { id: 123 };
        const typeQuestionCollection: ITypeQuestion[] = [
          {
            ...typeQuestion,
          },
          { id: 456 },
        ];
        expectedResult = service.addTypeQuestionToCollectionIfMissing(
          typeQuestionCollection,
          typeQuestion
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeQuestion to an array that doesn't contain it", () => {
        const typeQuestion: ITypeQuestion = { id: 123 };
        const typeQuestionCollection: ITypeQuestion[] = [{ id: 456 }];
        expectedResult = service.addTypeQuestionToCollectionIfMissing(
          typeQuestionCollection,
          typeQuestion
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeQuestion);
      });

      it("should add only unique TypeQuestion to an array", () => {
        const typeQuestionArray: ITypeQuestion[] = [
          { id: 123 },
          { id: 456 },
          { id: 27940 },
        ];
        const typeQuestionCollection: ITypeQuestion[] = [{ id: 123 }];
        expectedResult = service.addTypeQuestionToCollectionIfMissing(
          typeQuestionCollection,
          ...typeQuestionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const typeQuestion: ITypeQuestion = { id: 123 };
        const typeQuestion2: ITypeQuestion = { id: 456 };
        expectedResult = service.addTypeQuestionToCollectionIfMissing(
          [],
          typeQuestion,
          typeQuestion2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeQuestion);
        expect(expectedResult).toContain(typeQuestion2);
      });

      it("should accept null and undefined values", () => {
        const typeQuestion: ITypeQuestion = { id: 123 };
        expectedResult = service.addTypeQuestionToCollectionIfMissing(
          [],
          null,
          typeQuestion,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeQuestion);
      });

      it("should return initial array if no TypeQuestion is added", () => {
        const typeQuestionCollection: ITypeQuestion[] = [{ id: 123 }];
        expectedResult = service.addTypeQuestionToCollectionIfMissing(
          typeQuestionCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(typeQuestionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
