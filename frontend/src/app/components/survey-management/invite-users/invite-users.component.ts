import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Survey } from '../_models/survey.model';
import { SurveyService } from '../survey.service';
import { Invitation, InvitationWrapper } from '../_models/invitation.model';
import { TagInputComponent as SourceTagInput } from 'ngx-chips';
import { of, throwError } from 'rxjs';
import { FormControl, ValidatorFn } from '@angular/forms';
import Swal from 'sweetalert2';
import * as moment from 'moment';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import * as momentTz from 'moment-timezone'
import { DateTime } from 'luxon';

export interface AutoCompleteModel {
   value: any;
   display: string;
}

@Component({
  selector: 'app-invite-users',
  templateUrl: './invite-users.component.html',
  styleUrls: ['./invite-users.component.scss'],
  encapsulation:ViewEncapsulation.None,
})
export class InviteUsersComponent implements OnInit {



  @ViewChild('tagInput') tagInput:SourceTagInput
  @ViewChild('picker') timepicker: any;

  public data : Survey;
  public emails  = [] as any;
  public emailsFile : File ; 

  public survey_id:number;
  public notify="no"
  public scoreOptions = [ 1, 2, 3 ,4, 5, 6, 7, 8 ,9, 10, 15, 20, 25, 30]
  public selectedOption=0
   //frequency
  public dates: moment.Moment[] = []
  public finalDates : Date[]= []
  public times:string[] = ['09:00'];
  public validityOptions = ['Two Hours','Half Day' ,'One Day' , 'Two Day' , 'One Week' , 'One Month' , 'Always'] 
  public validity:string = 'Always' ; 

  public validators = [ this.must_be_email.bind(this) as ValidatorFn ];
  public errorMessages = {
      'must_be_email': 'Please be sure to use a valid email format'
  };
  public splitPattern = new RegExp('[\,\;]');
  public onAddedFunc = this.beforeAdd.bind(this);

  public userTz: string;
  public selectedTz: string = 'Etc/GMT+1';
  public utcTz: string;
  public tzNames: string[];

  private addFirstAttemptFailed = false;
  username="bilel.jarrahi@dqlick.com";
  password="Dqlick2022+";

  private must_be_email(control: FormControl) {        

      if (this.addFirstAttemptFailed && !this.validateEmail(control.value)) {
          return { "must_be_email": true };
      }
      return null;
  }

  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private _surveysService: SurveyService,
    private localStorageService: LocalStorageService,
    private sessionStorageService: SessionStorageService,
    ) {
      this.userTz = DateTime.local().zoneName;
      this.utcTz = 'GMT'
      this.tzNames =  momentTz.tz.names();
    }





    ngOnInit() {
    
      this._route.queryParamMap.subscribe(
          params => {
            
            this.survey_id  = parseInt(params.get("id")!);

        })

    }


  private beforeAdd(tag: any) {

    if (!this.validateEmail(tag)) {
      if (!this.addFirstAttemptFailed) {
        this.addFirstAttemptFailed = true;
        this.tagInput.setInputValue(tag);
       
      }
 
      return throwError(this.errorMessages['must_be_email']);
      //return of('').pipe(tap(() => setTimeout(() => this.tagInput.setInputValue(tag))));
    }

    this.addFirstAttemptFailed = false;
    return of(tag);
  }
  

  private validateEmail(text: string) {
    var EMAIL_REGEXP = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}$/i;
    return (text && EMAIL_REGEXP.test(text));
  }


 
  invitation : Invitation = new Invitation;
  public send(){


  
    if(this.emails.length != 0 && this.dates.length != 0 && this.times.length != 0 || this.emailsFile != null && this.dates.length !=0 && this.times.length != 0 ) {

      if(this.emails.length != 0){

        this.emails = this.emails.map((i:any) => i?.value)
      }
     
      for (let i = 0; i < this.times.length; i++) {
        this.dates.forEach(momentDate => {
          let el = momentDate.toDate();
      
          if (!(el instanceof Date) || isNaN(el.getTime())) {
            console.log("Invalid date object:", el);
            return;
          }
      
          let timeArr = this.times[i].split(":");
          let hours = Number(timeArr[0]);
          let minutes = Number(timeArr[1]);
      
          let manipulatedDate = new Date(el);
          manipulatedDate.setHours(hours);
          manipulatedDate.setMinutes(minutes);
                if (!isNaN(manipulatedDate.getTime())) {
            this.finalDates.push(manipulatedDate);
          }
        });
      }
            
         
      this.invitation.emails = this.emails;
      this.invitation.survey_id = this.survey_id;
      this.invitation.validity = this.validity ;
      this.invitation.invitationCrons = this.finalDates;
      this.invitation.senderEmail = this.localStorageService.retrieve('accountInfo') ? this.localStorageService.retrieve('accountInfo').email : this.sessionStorageService.retrieve('accountInfo').email;
      this.invitation.getNotified = this.notify == 'yes'? true : false ; 
      this.invitation.scoreNotif = this.selectedOption;
      this.invitation.timeZone = this.selectedTz;
              
      const formData = new FormData();
      formData.append('data', JSON.stringify(this.invitation))
      formData.append('file', this.emailsFile);
      // formData.append('username', this.username);
      // formData.append('password', this.password);



      console.log("formData formData :",formData);
      
      this._surveysService.sendInvitation(formData).subscribe(
        (response) => { },
        (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error!',
            text: 'An error occur !' ,
            customClass: {
              confirmButton: 'btn btn-primary'
            }
          })
        },
        () => {
          Swal.fire({
            icon: 'success',
            title: 'Added!',
            text: 'invitation(s) sent successfully',
            customClass: {
              confirmButton: 'btn btn-success'
            }
          })
          this._router.navigate(['/home/survey/list']);
        }
      )
    
    }else{

      Swal.fire({
        icon: 'error',
        title: 'Error!',
        text: 'please enter at least one email and pick one time / date to proceed with the invitation !' ,
        customClass: {
          confirmButton: 'btn btn-primary'
        }
      })

     } 
    
  }



  public isSelected = (event: any) => {

    const date = event as moment.Moment

    return( (this.dates.find(x => x.isSame(date))) ? "selected" : null) as any;
  };

  
  
  public select(event: any, calendar: any) {
    const date: moment.Moment = event

    const index = this.dates.findIndex(x => x.isSame(date));
    if (index < 0) this.dates.push(date);
    else this.dates.splice(index, 1); this.finalDates.splice(index,1);
    
    calendar.updateTodaysDate();
  }


  public onChangeHour(event :any){
   let time = event;
  }


  onDatepickerClose( picker :any){
   let b : string=  picker['defaultTime'] as string
    this.times.push(b)
    console.log(this.times)
  }


  public handleFileSelect(evt :any) {
    var files = evt.target.files; // FileList object
    this.emailsFile = files[0];
    
  }


  public deleteTime(index : number){
 
   return  this.times.splice(index, 1)

  }


  public timeZoneChanged(timeZone: string): void {
    console.log(timeZone);
    this.selectedTz = timeZone;

  }

}
