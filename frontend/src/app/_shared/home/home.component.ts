import { Component, OnDestroy, OnInit } from '@angular/core';
import { MediaChange, MediaObserver } from '@angular/flex-layout';
import { Subscription } from 'rxjs';
import { AccountService } from 'src/app/components/authentification/authService/account.service';
import { AuthService } from 'src/app/components/authentification/authService/auth.service';
import { TokenService } from 'src/app/components/authentification/token/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnDestroy {

  opened: boolean = true;
  expand : boolean = false;
  private mediaWatcher: Subscription;

  constructor(private media: MediaObserver,
    private token: TokenService,
    
    private authService: AuthService) {
    this.mediaWatcher = this.media.media$.subscribe((mediaChange: MediaChange) => {

    })
  }



  ngOnDestroy() {
    this.mediaWatcher.unsubscribe();
  }

  



}