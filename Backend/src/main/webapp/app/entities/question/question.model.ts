import dayjs from "dayjs/esm";
import { IAnswerChoice } from "app/entities/answer-choice/answer-choice.model";
import { ITypeQuestion } from "app/entities/type-question/type-question.model";
import { ISurvey } from "app/entities/survey/survey.model";
import { Language } from "app/entities/enumerations/language.model";

export interface IQuestion {
  id?: number;
  text?: string;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  isArchived?: boolean | null;
  archivedDate?: dayjs.Dayjs | null;
  language?: Language;
  answerChoices?: IAnswerChoice[] | null;
  typeQuestion?: ITypeQuestion | null;
  surveys?: ISurvey[] | null;
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public text?: string,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public isArchived?: boolean | null,
    public archivedDate?: dayjs.Dayjs | null,
    public language?: Language,
    public answerChoices?: IAnswerChoice[] | null,
    public typeQuestion?: ITypeQuestion | null,
    public surveys?: ISurvey[] | null
  ) {
    this.isArchived = this.isArchived ?? false;
  }
}

export function getQuestionIdentifier(question: IQuestion): number | undefined {
  return question.id;
}
