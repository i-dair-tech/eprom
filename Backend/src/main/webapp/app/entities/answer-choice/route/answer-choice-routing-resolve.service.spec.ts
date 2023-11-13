import { TestBed } from "@angular/core/testing";
import { HttpResponse } from "@angular/common/http";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import {
  ActivatedRouteSnapshot,
  ActivatedRoute,
  Router,
  convertToParamMap,
} from "@angular/router";
import { RouterTestingModule } from "@angular/router/testing";
import { of } from "rxjs";

import { IAnswerChoice, AnswerChoice } from "../answer-choice.model";
import { AnswerChoiceService } from "../service/answer-choice.service";

import { AnswerChoiceRoutingResolveService } from "./answer-choice-routing-resolve.service";

describe("AnswerChoice routing resolve service", () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AnswerChoiceRoutingResolveService;
  let service: AnswerChoiceService;
  let resultAnswerChoice: IAnswerChoice | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest
      .spyOn(mockRouter, "navigate")
      .mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(AnswerChoiceRoutingResolveService);
    service = TestBed.inject(AnswerChoiceService);
    resultAnswerChoice = undefined;
  });

  describe("resolve", () => {
    it("should return IAnswerChoice returned by find", () => {
      // GIVEN
      service.find = jest.fn((id) => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService
        .resolve(mockActivatedRouteSnapshot)
        .subscribe((result) => {
          resultAnswerChoice = result;
        });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAnswerChoice).toEqual({ id: 123 });
    });

    it("should return new IAnswerChoice if id is not provided", () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService
        .resolve(mockActivatedRouteSnapshot)
        .subscribe((result) => {
          resultAnswerChoice = result;
        });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAnswerChoice).toEqual(new AnswerChoice());
    });

    it("should route to 404 page if data not found in server", () => {
      // GIVEN
      jest
        .spyOn(service, "find")
        .mockReturnValue(
          of(new HttpResponse({ body: null as unknown as AnswerChoice }))
        );
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService
        .resolve(mockActivatedRouteSnapshot)
        .subscribe((result) => {
          resultAnswerChoice = result;
        });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAnswerChoice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(["404"]);
    });
  });
});
