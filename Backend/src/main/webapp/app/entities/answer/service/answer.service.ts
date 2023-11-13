import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import dayjs from "dayjs/esm";

import { isPresent } from "app/core/util/operators";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { createRequestOption } from "app/core/request/request-util";
import { IAnswer, getAnswerIdentifier } from "../answer.model";

export type EntityResponseType = HttpResponse<IAnswer>;
export type EntityArrayResponseType = HttpResponse<IAnswer[]>;

@Injectable({ providedIn: "root" })
export class AnswerService {
  protected resourceUrl =
    this.applicationConfigService.getEndpointFor("api/answers");

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService
  ) {}

  create(answer: IAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .post<IAnswer>(this.resourceUrl, copy, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(answer: IAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .put<IAnswer>(
        `${this.resourceUrl}/${getAnswerIdentifier(answer) as number}`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(answer: IAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .patch<IAnswer>(
        `${this.resourceUrl}/${getAnswerIdentifier(answer) as number}`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnswer>(`${this.resourceUrl}/${id}`, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnswer[]>(this.resourceUrl, {
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

  addAnswerToCollectionIfMissing(
    answerCollection: IAnswer[],
    ...answersToCheck: (IAnswer | null | undefined)[]
  ): IAnswer[] {
    const answers: IAnswer[] = answersToCheck.filter(isPresent);
    if (answers.length > 0) {
      const answerCollectionIdentifiers = answerCollection.map(
        (answerItem) => getAnswerIdentifier(answerItem)!
      );
      const answersToAdd = answers.filter((answerItem) => {
        const answerIdentifier = getAnswerIdentifier(answerItem);
        if (
          answerIdentifier == null ||
          answerCollectionIdentifiers.includes(answerIdentifier)
        ) {
          return false;
        }
        answerCollectionIdentifiers.push(answerIdentifier);
        return true;
      });
      return [...answersToAdd, ...answerCollection];
    }
    return answerCollection;
  }

  protected convertDateFromClient(answer: IAnswer): IAnswer {
    return Object.assign({}, answer, {
      createdDate: answer.createdDate?.isValid()
        ? answer.createdDate.toJSON()
        : undefined,
      lastModifiedDate: answer.lastModifiedDate?.isValid()
        ? answer.lastModifiedDate.toJSON()
        : undefined,
      archivedDate: answer.archivedDate?.isValid()
        ? answer.archivedDate.toJSON()
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
      res.body.forEach((answer: IAnswer) => {
        answer.createdDate = answer.createdDate
          ? dayjs(answer.createdDate)
          : undefined;
        answer.lastModifiedDate = answer.lastModifiedDate
          ? dayjs(answer.lastModifiedDate)
          : undefined;
        answer.archivedDate = answer.archivedDate
          ? dayjs(answer.archivedDate)
          : undefined;
      });
    }
    return res;
  }
}
