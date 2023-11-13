import dayjs from "dayjs/esm";
import { IQuestion } from "app/entities/question/question.model";

export interface IAnswerChoice {
  id?: number;
  text?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  isArchived?: boolean | null;
  archivedDate?: dayjs.Dayjs | null;
  questions?: IQuestion[] | null;
}

export class AnswerChoice implements IAnswerChoice {
  constructor(
    public id?: number,
    public text?: string | null,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public isArchived?: boolean | null,
    public archivedDate?: dayjs.Dayjs | null,
    public questions?: IQuestion[] | null
  ) {
    this.isArchived = this.isArchived ?? false;
  }
}

export function getAnswerChoiceIdentifier(
  answerChoice: IAnswerChoice
): number | undefined {
  return answerChoice.id;
}
