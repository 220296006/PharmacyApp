import { User } from '../../model/user';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { UserService } from 'src/app/services/user-service/userservice.service';
import { UpdateUserDialogComponent } from '../update-user-dialog/update-user-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { CreateUserDialogComponent } from '../create-user-dialog/create-user-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth-service/auth-service.service';

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
  getUserInfo: any;

  constructor(
    private userService: UserService,
    private updateUserDialog: MatDialog,
    private createUserDialog: MatDialog,
    private snackBar: MatSnackBar,
    private authService: AuthService 
  ) {}

  ngOnInit() {
    this.getAllUserData();
    this.getUserInfo = this.authService.getLoggedInUser();
  }

  openCreateUserDialog(id: any) {
    if (this.getUserInfo === 'ROLE_ADMIN') {
    const dialogRef = this.createUserDialog.open(CreateUserDialogComponent,{
      width: '400px',
      exitAnimationDuration: '1000ms',
      enterAnimationDuration: '1000ms',
      data:
      {
        id: id
      }
    })
    dialogRef.afterClosed().subscribe(response=> {
      response = this.getAllUserData();
    });
    } else {
      this.snackBar.open('You do not have permission to perform this action.', 'Close', {
        duration: 3000,
      });
    }
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
    if (this.getUserInfo === 'ROLE_ADMIN') {
     this.snackBar.open('You do not have permission to perform this action.', 'Close', {
        duration: 3000,
      });
          this.userService.deleteUserById(id).subscribe({
            next: (response) => {
              console.log('Response from server:', response);
              if (response.status === 'OK') {
                this.snackBar.open('User deleted successfully!', 'Close', {
                  duration: 3000,
                });
                this.getAllUserData(); // Refresh the user data
              } else {
                this.snackBar.open('Error: ' + response.message, 'Close', {
                  duration: 3000,
                });
              }
            },
            error: (error) => {
              console.error('Error deleting user:', error);
              this.snackBar.open('An error occurred while deleting the user.', 'Close', {
                duration: 3000,
              });
            },
        });
    } else {
      this.snackBar.open('You do not have permission to perform this action.', 'Close', {
        duration: 3000,
      });
    }
  }

  onUpdateUser(userId: number) {
    if (this.getUserInfo === 'ROLE_ADMIN') {
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
  } else {
    this.snackBar.open('You do not have permission to perform this action.', 'Close', {
      duration: 3000,
    });
  }
}

  openUpdateDialog(user: User): void {
    console.log('User data passed to dialog:', user); 
    const dialogRef = this.updateUserDialog.open(UpdateUserDialogComponent, {
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
