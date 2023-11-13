import { Component, OnInit } from '@angular/core';
import * as echarts from 'echarts';
import { ReportService } from '../reports.service';
@Component({
  selector: 'app-per-surveys',
  templateUrl: './per-surveys.component.html',
  styleUrls: ['./per-surveys.component.scss']
})
export class PerSurveysComponent implements OnInit {
  

  number_surveys = 0;
  number_answer = 0;
  number_question = 0;
  public option: any;

  constructor(private _reportService : ReportService) { }
  
  ngOnInit(): void {
    this.getCount();
  }

  getCount(){
    this._reportService.getQuestionsNumber().subscribe((response) => {
      this.number_question = response;
    })
   
   this._reportService.getSurveysNumber().subscribe((response) => {
     this.number_surveys = response;
   })


   this._reportService.getAnswersNumber().subscribe((response) => {
     this.number_answer = response;
   })

   this._reportService.getSurveysCountPerInvitation().subscribe((response) => {
    this.showChart(response)
  })
   
 }

 showChart(data:any){

  this.option = {
    title: {
      text: 'count of invitations per Survey ',
      //subtext: 'Fake Data',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data:data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }


}

 
}
