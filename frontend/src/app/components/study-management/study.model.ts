import { Survey } from "../survey-management/_models/survey.model";

export class Study {

    id?: number;
    title!: string;
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    isArchived?: Boolean;
    archivedDate?: Date;
    surveys?:Survey[];
}