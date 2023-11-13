import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit, OnDestroy {
  errorMessage?: string;
  errorKey?: string;
  langChangeSubscription?: Subscription;

  constructor(private translateService: TranslateService, private route: ActivatedRoute,   private _router: Router,) {}

  ngOnInit(): void {
    this.route.data.subscribe(routeData => {
      if (routeData['errorMessage']) {
        this.errorKey = routeData['errorMessage'];
        this.getErrorMessageTranslation();
        this.langChangeSubscription = this.translateService.onLangChange.subscribe(() => this.getErrorMessageTranslation());
      }
    });
  }

  ngOnDestroy(): void {
    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
    }
  }

  private getErrorMessageTranslation(): void {
    this.errorMessage = '';
    if (this.errorKey) {
      this.translateService.get(this.errorKey).subscribe(translatedErrorMessage => {
        this.errorMessage = translatedErrorMessage;
      });
    }
  }

  public goToPage(pageName: string) {
    this._router.navigate([`${pageName}`]);
  }
}
