import { AfterViewInit, Component } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterViewInit {
  title = 'frontend';
  textDir: any;
  lang: any;

  constructor(private router: Router,
    private location: Location) {
    if (sessionStorage.getItem('lang') !== null) {
      this.lang = sessionStorage.getItem('lang');
    } else {
      this.lang = 'fr'
      sessionStorage.setItem('lang', 'fr')
    }

    this.replaceUrl();

  }
  ngAfterViewInit(): void {
    this.replaceUrl();
  }


  redirectIfNecessary() {
    const currentPath = this.router.url;
    if (currentPath === '/home') {
      this.router.navigate(['/home/LandingPage']);
    }
  }

  replaceUrl() {
    const path = this.location.path();
    if (path.startsWith('/#')) {
      const newPath = path.substring(2);
      this.location.replaceState(newPath);
    }
  }

}
