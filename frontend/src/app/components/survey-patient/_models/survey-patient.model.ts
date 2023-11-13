import { Question } from "../../question-management/_models/question.model";

export class SurveyPatient {
    id?: number;
    title!: string;
    topic!: string;
    description!: string;
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    isArchived?: Boolean;
    archivedDate?: Date;
    questions?:Question[];
    status:SurveyPatientStatus.SENT;
    invitationId : number;
}

export enum SurveyPatientStatus {
    UNSENT = "UNSENT",
    SENT = "SENT",
    REMINDED = "REMINDED",
    RESPONDED = "RESPONDED",
    NOT_ANSWERED = "NOT_ANSWERED"
}