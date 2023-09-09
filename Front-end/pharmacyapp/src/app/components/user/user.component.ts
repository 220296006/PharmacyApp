import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ApiResponse } from 'src/app/model/api-response';
import { Page } from 'src/app/model/page';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/userservice.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  tableDataSource: MatTableDataSource<User> = new MatTableDataSource<User>();
  displayedColumns: string[] = ["id", "imageUrl", "firstName", "middleName", "lastName", "email", "phone", "address"];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getAllUserData();
  }

  getAllUserData() {
    this.userService.getAllUserData().subscribe({
      next: (response: ApiResponse<Page>) => {
        this.tableDataSource.data = response.data.page.content;
        this.tableDataSource.paginator = this.paginator;
        this.tableDataSource.sort = this.sort;
      },
      error: (error) => {
        console.error("Error fetching data:", error);
      }
    });
  }
}
