import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable, BehaviorSubject, catchError, map, of, startWith } from 'rxjs';
import { ApiResponse } from 'src/app/model/api-response';
import { Page } from 'src/app/model/page';
import { UserService } from 'src/app/services/userservice.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  usersState$: Observable<{ appState: string, appData?: ApiResponse<Page>, error?: HttpErrorResponse }>;
  responseSubject = new BehaviorSubject<ApiResponse<Page>>(null);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$: any;

  constructor(private userSevice: UserService) { }

  ngOnInit(): void {
  
    this.usersState$ = this.userSevice.users$().pipe(
      map((response: ApiResponse<Page>) => {
        this.responseSubject.next(response);
        this.currentPageSubject.next(response.data.page.number);
        console.log(response);
        return ({ appState: 'APP_LOADED', appData: response });
      }),
      startWith({ appState: 'APP_LOADING' }),
      catchError((error: HttpErrorResponse) =>{ 
        return of({ appState: 'APP_ERROR', error })}
        )
    )
  }

  gotToPage(name?: string, pageNumber: number = 0): void {
    this.usersState$ = this.userSevice.users$(name, pageNumber).pipe(
      map((response: ApiResponse<Page>) => {
        this.responseSubject.next(response);
        this.currentPageSubject.next(pageNumber);
        console.log(response);
        return ({ appState: 'APP_LOADED', appData: response });
      }),
      startWith({ appState: 'APP_LOADED', appData: this.responseSubject.value }),
      catchError((error: HttpErrorResponse) =>{ 
        return of({ appState: 'APP_ERROR', error })}
        )
    )
  }

  goToNextOrPreviousPage(direction?: string, name?: string): void {
    this.gotToPage(name, direction === 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1);
  }

}
