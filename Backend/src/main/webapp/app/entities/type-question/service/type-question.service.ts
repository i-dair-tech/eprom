import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import dayjs from "dayjs/esm";

import { isPresent } from "app/core/util/operators";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { createRequestOption } from "app/core/request/request-util";
import {
  ITypeQuestion,
  getTypeQuestionIdentifier,
} from "../type-question.model";

export type EntityResponseType = HttpResponse<ITypeQuestion>;
export type EntityArrayResponseType = HttpResponse<ITypeQuestion[]>;

@Injectable({ providedIn: "root" })
export class TypeQuestionService {
  protected resourceUrl =
    this.applicationConfigService.getEndpointFor("api/type-questions");

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService
  ) {}

  create(typeQuestion: ITypeQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(typeQuestion);
    return this.http
      .post<ITypeQuestion>(this.resourceUrl, copy, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(typeQuestion: ITypeQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(typeQuestion);
    return this.http
      .put<ITypeQuestion>(
        `${this.resourceUrl}/${
          getTypeQuestionIdentifier(typeQuestion) as number
        }`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(typeQuestion: ITypeQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(typeQuestion);
    return this.http
      .patch<ITypeQuestion>(
        `${this.resourceUrl}/${
          getTypeQuestionIdentifier(typeQuestion) as number
        }`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITypeQuestion>(`${this.resourceUrl}/${id}`, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITypeQuestion[]>(this.resourceUrl, {
        params: options,
        observe: "response",
      })
      .pipe(
        map((res: EntityArrayResponseType) =>
          this.convertDateArrayFromServer(res)
        )
      );
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, {
      observe: "response",
    });
  }

  addTypeQuestionToCollectionIfMissing(
    typeQuestionCollection: ITypeQuestion[],
    ...typeQuestionsToCheck: (ITypeQuestion | null | undefined)[]
  ): ITypeQuestion[] {
    const typeQuestions: ITypeQuestion[] =
      typeQuestionsToCheck.filter(isPresent);
    if (typeQuestions.length > 0) {
      const typeQuestionCollectionIdentifiers = typeQuestionCollection.map(
        (typeQuestionItem) => getTypeQuestionIdentifier(typeQuestionItem)!
      );
      const typeQuestionsToAdd = typeQuestions.filter((typeQuestionItem) => {
        const typeQuestionIdentifier =
          getTypeQuestionIdentifier(typeQuestionItem);
        if (
          typeQuestionIdentifier == null ||
          typeQuestionCollectionIdentifiers.includes(typeQuestionIdentifier)
        ) {
          return false;
        }
        typeQuestionCollectionIdentifiers.push(typeQuestionIdentifier);
        return true;
      });
      return [...typeQuestionsToAdd, ...typeQuestionCollection];
    }
    return typeQuestionCollection;
  }

  protected convertDateFromClient(typeQuestion: ITypeQuestion): ITypeQuestion {
    return Object.assign({}, typeQuestion, {
      createdDate: typeQuestion.createdDate?.isValid()
        ? typeQuestion.createdDate.toJSON()
        : undefined,
      lastModifiedDate: typeQuestion.lastModifiedDate?.isValid()
        ? typeQuestion.lastModifiedDate.toJSON()
        : undefined,
      archivedDate: typeQuestion.archivedDate?.isValid()
        ? typeQuestion.archivedDate.toJSON()
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate
        ? dayjs(res.body.createdDate)
        : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate
        ? dayjs(res.body.lastModifiedDate)
        : undefined;
      res.body.archivedDate = res.body.archivedDate
        ? dayjs(res.body.archivedDate)
        : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(
    res: EntityArrayResponseType
  ): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((typeQuestion: ITypeQuestion) => {
        typeQuestion.createdDate = typeQuestion.createdDate
          ? dayjs(typeQuestion.createdDate)
          : undefined;
        typeQuestion.lastModifiedDate = typeQuestion.lastModifiedDate
          ? dayjs(typeQuestion.lastModifiedDate)
          : undefined;
        typeQuestion.archivedDate = typeQuestion.archivedDate
          ? dayjs(typeQuestion.archivedDate)
          : undefined;
      });
    }
    return res;
  }
}
