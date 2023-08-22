import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { ApiResponse } from 'src/app/model/api-response';
import { Page } from 'src/app/model/page';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/userservice.service';

const tableDataSource: ApiResponse<Page>[] = [];

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
 
  tableDataSource: MatTableDataSource<User> = new MatTableDataSource<User>();
  displayedColumns: string[] = ["id", "imageUrl", "firstName", "middleName", "lastName", "email", "phone", "address"];
  users: User[];

  @ViewChild(MatTable) table?: MatTable<ApiResponse<Page>>;
  @ViewChild(MatPaginator) _paginator!: MatPaginator;
  @ViewChild(MatSort) _sort!: MatSort;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
   this.getAllUserData();
  }

  getAllUserData() {
    this.userService.getAllUserData().subscribe((response: ApiResponse<Page>) => {
      this.tableDataSource.data = response.data.page.content;
      this.tableDataSource.paginator = this._paginator;
      this.tableDataSource.sort = this._sort;
      console.log(response);
    });
    
  }
}

