import { Injectable } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { Resolve, ActivatedRouteSnapshot, Router } from "@angular/router";
import { Observable, of, EMPTY } from "rxjs";
import { mergeMap } from "rxjs/operators";

import { IAnswerChoice, AnswerChoice } from "../answer-choice.model";
import { AnswerChoiceService } from "../service/answer-choice.service";

@Injectable({ providedIn: "root" })
export class AnswerChoiceRoutingResolveService
  implements Resolve<IAnswerChoice>
{
  constructor(
    protected service: AnswerChoiceService,
    protected router: Router
  ) {}

  resolve(
    route: ActivatedRouteSnapshot
  ): Observable<IAnswerChoice> | Observable<never> {
    const id = route.params["id"];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((answerChoice: HttpResponse<AnswerChoice>) => {
          if (answerChoice.body) {
            return of(answerChoice.body);
          } else {
            this.router.navigate(["404"]);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnswerChoice());
  }
}
