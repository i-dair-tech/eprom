import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import dayjs from "dayjs/esm";

import { isPresent } from "app/core/util/operators";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { createRequestOption } from "app/core/request/request-util";
import { ISurvey, getSurveyIdentifier } from "../survey.model";

export type EntityResponseType = HttpResponse<ISurvey>;
export type EntityArrayResponseType = HttpResponse<ISurvey[]>;

@Injectable({ providedIn: "root" })
export class SurveyService {
  protected resourceUrl =
    this.applicationConfigService.getEndpointFor("api/surveys");

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService
  ) {}

  create(survey: ISurvey): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(survey);
    return this.http
      .post<ISurvey>(this.resourceUrl, copy, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(survey: ISurvey): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(survey);
    return this.http
      .put<ISurvey>(
        `${this.resourceUrl}/${getSurveyIdentifier(survey) as number}`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(survey: ISurvey): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(survey);
    return this.http
      .patch<ISurvey>(
        `${this.resourceUrl}/${getSurveyIdentifier(survey) as number}`,
        copy,
        { observe: "response" }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISurvey>(`${this.resourceUrl}/${id}`, { observe: "response" })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISurvey[]>(this.resourceUrl, {
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

  addSurveyToCollectionIfMissing(
    surveyCollection: ISurvey[],
    ...surveysToCheck: (ISurvey | null | undefined)[]
  ): ISurvey[] {
    const surveys: ISurvey[] = surveysToCheck.filter(isPresent);
    if (surveys.length > 0) {
      const surveyCollectionIdentifiers = surveyCollection.map(
        (surveyItem) => getSurveyIdentifier(surveyItem)!
      );
      const surveysToAdd = surveys.filter((surveyItem) => {
        const surveyIdentifier = getSurveyIdentifier(surveyItem);
        if (
          surveyIdentifier == null ||
          surveyCollectionIdentifiers.includes(surveyIdentifier)
        ) {
          return false;
        }
        surveyCollectionIdentifiers.push(surveyIdentifier);
        return true;
      });
      return [...surveysToAdd, ...surveyCollection];
    }
    return surveyCollection;
  }

  protected convertDateFromClient(survey: ISurvey): ISurvey {
    return Object.assign({}, survey, {
      createdDate: survey.createdDate?.isValid()
        ? survey.createdDate.toJSON()
        : undefined,
      lastModifiedDate: survey.lastModifiedDate?.isValid()
        ? survey.lastModifiedDate.toJSON()
        : undefined,
      archivedDate: survey.archivedDate?.isValid()
        ? survey.archivedDate.toJSON()
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
      res.body.forEach((survey: ISurvey) => {
        survey.createdDate = survey.createdDate
          ? dayjs(survey.createdDate)
          : undefined;
        survey.lastModifiedDate = survey.lastModifiedDate
          ? dayjs(survey.lastModifiedDate)
          : undefined;
        survey.archivedDate = survey.archivedDate
          ? dayjs(survey.archivedDate)
          : undefined;
      });
    }
    return res;
  }
}
