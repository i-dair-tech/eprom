import { Component, OnInit, ViewChild } from '@angular/core';
import { ColumnMode } from '@swimlane/ngx-datatable';
import { EChartsOption } from 'echarts';
import { Subscription } from 'rxjs';
import { User } from '../../user-management/user.model';
import { ReportService } from '../reports.service';

export interface Detail {
  [key: string]: { score: number; createdDate: string }[];
}
@Component({
  selector: 'app-per-users',
  templateUrl: './per-users.component.html',
  styleUrls: ['./per-users.component.scss']
})


export class PerUsersComponent implements OnInit {
  private _subscription: Subscription[] = [];


  public patients_number :number;
  public admins_number:number;
  public studyCoordinators_number:number;
  public number_users:number;
  public patients:any;
  public searchTerm: string;
  public selectedPatient: string;
  public option: any;
  public show:boolean = false ;
  public details:Detail;



  constructor(private _reportService : ReportService){


  }

  /**
   * On init
   */
  ngOnInit() {
    this._subscription.push(
    this._reportService.getPatientsList().subscribe(response => {
     
      this.patients = response;
      this.patients_number = response.length;
 
    })
    )

    this._subscription.push(
    this._reportService.getUsersNumber().subscribe(response => {
      this.number_users = response;
    })
    )

    this._subscription.push(
    this._reportService.getAdminsList().subscribe(response => {
      this.admins_number = response.length;
    })
    )

    this._subscription.push(
    this._reportService.getStudyCoordinatorsList().subscribe(response => {
      this.studyCoordinators_number = response.length;
    })
    )
   
  }

 

  /**
  *  On destroy
  */
   ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }



  get filteredPatients(): any {
    
    if (this.searchTerm) {
      return this.patients.filter(
        (patient: any) => patient.email!.includes(this.searchTerm)
      );
    } else {
      return this.patients;
    }
  }

  onSelectPatient() {

       let surveys: string[] = [];
       let dates: string[] = [];
       let scores = [] ; 
       let data: any[] = []

       const dateFormat = new Intl.DateTimeFormat("en-US", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
      });

      this._reportService.getScoreEvolutionPerOwner(this.selectedPatient).subscribe(response =>{
         
        this.details = response
        console.log(this.details)

        //survey list
        surveys = Object.keys(response)


        //dates
        dates =  Object.values(response)
          .flat()
          .map((item : any) => new Date(item.createdDate))
          //.sort((a : any, b : any) => a - b)
          .map((date) => dateFormat.format(date)); 
          
          //scores
        scores = Object.values(response)
        .flat()
        .map((item : any) => item.score)

 
        //aray of objects
        const arrays = Object.values(response).map((arr:any) => arr.map((obj:any) => obj.score));
        const maxLength = Math.max(...arrays.map((arr) => arr.length));
        const result = arrays.map((arr, index) => {
          const emptyArr = Array(maxLength).fill(null);
          arr.forEach((score:any, i: any) => {
            const pos = arrays.slice(0, index).reduce((acc, curr) => acc + curr.length, 0) + i;
            emptyArr[pos] = score;
          });
          return emptyArr;
        });
        result[0].pop();



        //chart objects
        for (let index = 0; index < surveys.length; index++) {
        
            let obj = {
              name: surveys[index],
              type: 'line',
              stack: 'Total',
              data : result[index]               
            }
            data.push(obj) ;   
        
        }
            

      })

    setTimeout(() => {

     this.showChart(dates,surveys ,data);

    }, 1500);
    this.show = true;
  }


  showChart(dates :any , surveys: any , data:any){

    this.option = {
      // title: {
      //   text: 'Score Evolution',
      //   top:'-2%'
      // },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: surveys
      },
      grid: {
        left: '8%',
        right: '8%',
        bottom: '3%',
        containLabel: true
      },
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: dates
      },
      yAxis: {
        type: 'value'
      },
      series: data,
       
    };


  }

}
