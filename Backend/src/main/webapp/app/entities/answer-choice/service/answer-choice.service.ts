import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import dayjs from "dayjs/esm";

import { isPresent } from "app/core/util/operators";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { createRequestOption } from "app/core/request/request-util";
import {
  IAnswerChoice,
  getAnswerChoiceIdentifier,
} from "../answer-choice.model";

export type EntityResponseType = HttpResponse<IAnswerChoice>;
export type EntityArrayResponseType = HttpResponse<IAnswerChoice[]>;

@Injectable({ providedIn: "root" })
export class AnswerChoiceService {
  protected resourceUrl =
    this.applicationConfigService.getEndpointFor("api/answer-choices");

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService
  ) {}

  create(answerChoice: IAnswerChoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answerChoice);
    return this.http
      .post<IAnswerChoice>(this.resourceUrl, copy, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(answerChoice: IAnswerChoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answerChoice);
    return this.http
      .put<IAnswerChoice>(
        `${this.resourceUrl}/${
          getAnswerChoiceIdentifier(answerChoice) as number
        }`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(answerChoice: IAnswerChoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answerChoice);
    return this.http
      .patch<IAnswerChoice>(
        `${this.resourceUrl}/${
          getAnswerChoiceIdentifier(answerChoice) as number
        }`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnswerChoice>(`${this.resourceUrl}/${id}`, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnswerChoice[]>(this.resourceUrl, {
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

  addAnswerChoiceToCollectionIfMissing(
    answerChoiceCollection: IAnswerChoice[],
    ...answerChoicesToCheck: (IAnswerChoice | null | undefined)[]
  ): IAnswerChoice[] {
    const answerChoices: IAnswerChoice[] =
      answerChoicesToCheck.filter(isPresent);
    if (answerChoices.length > 0) {
      const answerChoiceCollectionIdentifiers = answerChoiceCollection.map(
        (answerChoiceItem) => getAnswerChoiceIdentifier(answerChoiceItem)!
      );
      const answerChoicesToAdd = answerChoices.filter((answerChoiceItem) => {
        const answerChoiceIdentifier =
          getAnswerChoiceIdentifier(answerChoiceItem);
        if (
          answerChoiceIdentifier == null ||
          answerChoiceCollectionIdentifiers.includes(answerChoiceIdentifier)
        ) {
          return false;
        }
        answerChoiceCollectionIdentifiers.push(answerChoiceIdentifier);
        return true;
      });
      return [...answerChoicesToAdd, ...answerChoiceCollection];
    }
    return answerChoiceCollection;
  }

  protected convertDateFromClient(answerChoice: IAnswerChoice): IAnswerChoice {
    return Object.assign({}, answerChoice, {
      createdDate: answerChoice.createdDate?.isValid()
        ? answerChoice.createdDate.toJSON()
        : undefined,
      lastModifiedDate: answerChoice.lastModifiedDate?.isValid()
        ? answerChoice.lastModifiedDate.toJSON()
        : undefined,
      archivedDate: answerChoice.archivedDate?.isValid()
        ? answerChoice.archivedDate.toJSON()
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
      res.body.forEach((answerChoice: IAnswerChoice) => {
        answerChoice.createdDate = answerChoice.createdDate
          ? dayjs(answerChoice.createdDate)
          : undefined;
        answerChoice.lastModifiedDate = answerChoice.lastModifiedDate
          ? dayjs(answerChoice.lastModifiedDate)
          : undefined;
        answerChoice.archivedDate = answerChoice.archivedDate
          ? dayjs(answerChoice.archivedDate)
          : undefined;
      });
    }
    return res;
  }
}
