
import { Survey } from "./survey.model";

export class Invitation {
    id?: number;
    emails?:string[];
    survey_id?:number;
    status : InvitationStatus.UNSENT;
    validity?: string;
    createdBy?: string;
    createdDate?: Date;
    invitationCrons:Date[];
    senderEmail?:string;
    getNotified:boolean
    scoreNotif:number;
    timeZone?:string;
    
}


export enum InvitationStatus {
    UNSENT = "UNSENT",
    SENT = "SENT",
    REMINDED = "REMINDED",
    RESPONDED = "RESPONDED"
}



export class InvitationWrapper{

    invitation : Invitation;
    file : File;
     

}