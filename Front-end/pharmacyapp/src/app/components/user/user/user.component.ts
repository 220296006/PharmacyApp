import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/userservice.service';

const tableDataSource: User[] = [];

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
 
  tableDataSource: MatTableDataSource<User> = new MatTableDataSource<User>();
  displayedColumns: string[] = ["id", "imageUrl","firstName", "middleName", "lastName", "email", "password", "phone", "address"];
  dataSource = [...tableDataSource];
  
  @ViewChild(MatTable) table?: MatTable<User>;
  @ViewChild(MatPaginator) _paginator!: MatPaginator;
  @ViewChild(MatSort) _sort!: MatSort;

  constructor(private userSevice: UserService) { }

  ngOnInit(): void {
   this.getAllUserData();
  }

  getAllUserData(){
    this.userSevice.getAllUserData().subscribe((response: any) => {
    this.tableDataSource = response;
    this.tableDataSource = new MatTableDataSource<User>(response.content);
    this.tableDataSource.paginator = this._paginator;
     this.tableDataSource.sort = this._sort;
 
  });
}

}