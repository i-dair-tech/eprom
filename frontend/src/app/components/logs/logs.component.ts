import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Log } from './logs.model';
import { LogsService } from './logs.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.scss']
})
export class LogsComponent implements OnInit , OnDestroy {

  private _subscription: Subscription[] = [];

  public logs!: Log[];
  public q:any;


  constructor(
    private _logsService: LogsService,
    private _router: Router,

  ) { }

  ngOnInit(): void {
    this._getLogs()
  }


 /**
 *  On destroy
 */
  ngOnDestroy(): void {
    this._subscription.forEach(subscription => subscription.unsubscribe());
  }


  private _getLogs() {
      
    this._subscription.push(
      this._logsService.getAllLogs().subscribe((response) => {

      this.logs = response;
     
    })
    )
  }

}
