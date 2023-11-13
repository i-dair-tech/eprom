import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Study } from '../../study-management/study.model';
import { StudyService } from '../../study-management/study.service';
import { ReportService } from '../reports.service';

@Component({
  selector: 'app-per-studies',
  templateUrl: './per-studies.component.html',
  styleUrls: ['./per-studies.component.scss']
})
export class PerStudiesComponent implements OnInit {
  
  private _subscription: Subscription[] = [];

  number_studies = 0;
  public studies!: Study[];
  public searchTerm: string;
  public selectedStudy: number;
  public study : Study;
  public option: any;
  public show:boolean = false ;
  owners: string[];
  surveys: { [owner: string]: string[] };
  scores: { [owner: string]: { [survey: string]: { timestamp: string, value: number }[] } };
  invitations:any;
  surveyNumber : number;

  constructor(private _reportService : ReportService,
    private _studyService: StudyService,) { }

  ngOnInit(): void {

    this._subscription.push(
      this._reportService.getStudiesNumber().subscribe((response) => {
        this.number_studies = response;
      })
      )

      this._subscription.push(
        this._studyService.getAllStudies().subscribe((response) => {
        this.studies = response;
       
      })
      )


  }


  /**
  *  On destroy
  */
   ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }





  get filteredStudies(): any {
    
    if (this.searchTerm) {
      return this.studies.filter(
        (study: any) => study.title!.toLowerCase().includes(this.searchTerm)
      );
    } else {
      return this.studies;
    }
  }


  onSelectStudy(){


    this._subscription.push(
      this._studyService.getStudyById(this.selectedStudy).subscribe((response) => {
      this.study = response;
      this.surveyNumber=this.study.surveys?.length!;
     
    })
    )
    this.show = true ; 


    this._subscription.push(
      this._reportService.getScoresByOwnersAndSurveysInStudy(Number(this.selectedStudy)).subscribe((response) => {
        this.owners = Object.keys(response);
        this.surveys = {};
        this.scores = {};
      
        for (const owner of this.owners) {
          this.surveys[owner] = Object.keys(response[owner]);
          this.scores[owner] = {};
      
          for (const survey of this.surveys[owner]) {
            this.scores[owner][survey] = [];
      
            for (const timestamp of Object.keys(response[owner][survey])) {
              const value = response[owner][survey][timestamp];
              this.scores[owner][survey].push({ timestamp, value });
            }
          }
        }
      })
      )


      this._subscription.push(
        this._reportService.countInvitationsBySurvey(Number(this.selectedStudy)).subscribe((response) => {
          this.invitations = response;
          console.log(this.invitations)
        })
        )
  }

}
