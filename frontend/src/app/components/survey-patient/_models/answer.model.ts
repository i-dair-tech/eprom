import { Question } from "../../question-management/_models/question.model";
import { Survey } from "../../survey-management/_models/survey.model";
import { AnswerChoice } from "../../question-management/_models/answer-choice.model";
import { Invitation } from "../../survey-management/_models/invitation.model";


export class Answer {
    id?: number;
    email?: string;
    createdDate?: Date;
    isArchived:Boolean;
    archivedDate:Date;
    question?:Question;
    survey?:Survey;
    invitation:Invitation;
    answerChoices: AnswerChoice[];
    
}



export class FinalAnswer{
 email : string ;
 invitationId : number;
 surveyId : number;
 answers :Answer[];
 finalScore:number;
}