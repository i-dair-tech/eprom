import { Component, OnInit } from "@angular/core";
import { HttpHeaders, HttpResponse } from "@angular/common/http";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

import { IQuestion } from "../question.model";

import { ASC, DESC, ITEMS_PER_PAGE } from "app/config/pagination.constants";
import { QuestionService } from "../service/question.service";
import { QuestionDeleteDialogComponent } from "../delete/question-delete-dialog.component";
import { ParseLinks } from "app/core/util/parse-links.service";

@Component({
  selector: "jhi-question",
  templateUrl: "./question.component.html",
})
export class QuestionComponent implements OnInit {
  questions: IQuestion[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected questionService: QuestionService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.questions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = "id";
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.questionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IQuestion[]>) => {
          this.isLoading = false;
          this.paginateQuestions(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.questions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IQuestion): number {
    return item.id!;
  }

  delete(question: IQuestion): void {
    const modalRef = this.modalService.open(QuestionDeleteDialogComponent, {
      size: "lg",
      backdrop: "static",
    });
    modalRef.componentInstance.question = question;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe((reason) => {
      if (reason === "deleted") {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + "," + (this.ascending ? ASC : DESC)];
    if (this.predicate !== "id") {
      result.push("id");
    }
    return result;
  }

  protected paginateQuestions(
    data: IQuestion[] | null,
    headers: HttpHeaders
  ): void {
    const linkHeader = headers.get("link");
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.questions.push(d);
      }
    }
  }
}
