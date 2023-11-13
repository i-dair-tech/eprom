import { Civility } from "./civility.model";


export class UserDTO {
    id!: number;
    login!: string;
    firstName!: string;
    lastName!: string;
    email!: string;

}


export class User{

    id: number | null;
    login?: string;
    firstName?: string | null;
    lastName?: string | null;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: string[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
}



export enum UserStatus {
    NEW = 'NEW',
    ACTIVE = 'ACTIVE',
    BLOCKED = 'BLOCKED',
    INACTIVE = 'INACTIVE',
}

export enum LangKey {
    FR = 'FR',
    EN = 'EN',
    HI = 'HI'
}

export const LANGUAGES: string[] = [
    'en',
    'fr',
    'hi',
   
  ];
  

export enum TypeUser {
    SUPER_ADMIN = 'SUPER_ADMIN',
    STUDY_COORDINATOR = 'STUDY_COORDINATOR',
    PATIENT = 'PATIENT'
}


