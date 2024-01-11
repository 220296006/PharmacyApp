import { User } from './../../model/user';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { UserService } from 'src/app/services/user-service/userservice.service';
import { UpdateUserDialogComponent } from '../update-user-dialog/update-user-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit {
  tableDataSource: MatTableDataSource<User> = new MatTableDataSource<User>([]);
  displayedColumns: string[] = [
    'id',
    'imageUrl',
    'firstName',
    'middleName',
    'lastName',
    'email',
    'phone',
    'address',
    'options',
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private userService: UserService,
    private updateDialog: MatDialog
  ) {}

  ngOnInit() {
    this.getAllUserData();
  }

  getAllUserData() {
    this.userService.getAllUserData().subscribe({
      next: (response) => {
        console.log('Response from server:', response);

        if (
          response.status === 'OK' &&
          response.data &&
          Array.isArray(response.data.page)
        ) {
          console.log('Data received:', response.data);

          this.tableDataSource.data = response.data.page;
          this.tableDataSource.paginator = this.paginator;
          this.tableDataSource.sort = this.sort;
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error fetching data:', error);
      },
    });
  }

  onDeleteUser(id: number) {
    this.userService.deleteUserById(id).subscribe({
      next: (response) => {
        console.log('Response from server:', response);
        if (response.status === 'OK') {
          this.getAllUserData();
        } else {
          console.error('Error: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Error deleting user:', error);
      },
    });
  }

  onUpdateUser(userId: number) {
    this.userService.getUserById(userId).subscribe({
      next: (response) => {
        console.log('Response from server:', response);

        if (response.status === 'OK' && response.data && response.data.user) {
          console.log('Data passed to dialog:', response.data);
          const user = response.data.user;
          if (user) {
            this.openUpdateDialog(user);
          } else {
            console.error('Error: User not found in response.data.user');
          }
        } else {
          console.error('Error: User data not present in the response');
        }
      },
      error: (error) => {
        console.error('Error fetching user:', error);
      },
    });
  }

  openUpdateDialog(user: User): void {
    console.log('User data passed to dialog:', user); // Check if user is defined
    const dialogRef = this.updateDialog.open(UpdateUserDialogComponent, {
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data: { user: user },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The update dialog was closed');
      if (result) {
        this.getAllUserData();
      }
    });
  }
}
