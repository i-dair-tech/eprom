import { Injectable } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { Resolve, ActivatedRouteSnapshot, Router } from "@angular/router";
import { Observable, of, EMPTY } from "rxjs";
import { mergeMap } from "rxjs/operators";

import { ITypeQuestion, TypeQuestion } from "../type-question.model";
import { TypeQuestionService } from "../service/type-question.service";

@Injectable({ providedIn: "root" })
export class TypeQuestionRoutingResolveService
  implements Resolve<ITypeQuestion>
{
  constructor(
    protected service: TypeQuestionService,
    protected router: Router
  ) {}

  resolve(
    route: ActivatedRouteSnapshot
  ): Observable<ITypeQuestion> | Observable<never> {
    const id = route.params["id"];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeQuestion: HttpResponse<TypeQuestion>) => {
          if (typeQuestion.body) {
            return of(typeQuestion.body);
          } else {
            this.router.navigate(["404"]);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeQuestion());
  }
}
