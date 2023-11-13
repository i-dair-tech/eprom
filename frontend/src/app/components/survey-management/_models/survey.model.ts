import { Question } from "../../question-management/_models/question.model";

export class Survey {
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
}