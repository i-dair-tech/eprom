

import { AnswerChoice } from "./answer-choice.model";
import { Language } from "./language.model";
import { TypeQuestion } from "./questions-type.model";


export class Question {
    id?: number;
    text: string | undefined;
    language :Language;
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    isArchived?: Boolean;
    archivedDate?: Date;
    typeQuestion : TypeQuestion;
    answerChoices : AnswerChoice[];
}

