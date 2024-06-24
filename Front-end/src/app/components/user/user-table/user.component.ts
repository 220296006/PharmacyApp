import { User } from '../../../interface/user';
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
import { jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';

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
  user: User;

  constructor(
    private userService: UserService,
    private updateUserDialog: MatDialog,
    private createUserDialog: MatDialog,
    private snackBar: MatSnackBar,
    private authService: AuthService 
  ) {}

  ngOnInit() {
    this.getAllUserData();
    const token = this.authService.getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token);
      const permissions = decodedToken.permissions?.map((role: any) => role.permissions).flat();
      console.log('User permissions:', permissions);
    }
  }

  openCreateUserDialog(id: any) {
    const userPermissions = this.authService.getPermissionsFromToken();
    if (userPermissions.includes('UPDATE:USER')) {
      const dialogRef = this.createUserDialog.open(CreateUserDialogComponent,{
        width: '400px',
        exitAnimationDuration: '1000ms',
        enterAnimationDuration: '1000ms',
        data: { id: id }
      });
      dialogRef.afterClosed().subscribe(response => {
        response = this.getAllUserData();
      });
    } else {
      this.snackBar.open('You do not have permission to perform this action.', 'Close', {
        duration: 3000,
      });
    }
  }


  loadProfileImage(userId: number, imageUrl: string): void {
    // Check if the image URL includes the query parameter 'includeData=true'
    const includeData = imageUrl.includes('includeData=true');
    if (includeData) {
      // Fetch image data if 'includeData' parameter is true
      this.userService.getImageData(userId).subscribe(
        (data: Blob) => {
          const reader = new FileReader();
          reader.onloadend = () => {
            // Update the 'imageUrl' field with the data URI
            this.tableDataSource.data.forEach((user: User) => {
              if (user.id === userId) {
                user.imageUrl = reader.result as string;
              }
            });
          };
          reader.readAsDataURL(data);
        },
        (error) => {
          console.error('Error loading profile image:', error);
          // Handle error (e.g., display default image)
        }
      );
    }
  }

  getAllUserData() {
    this.userService.getAllUserData().subscribe({
      next: (response) => {
        if (
          response.status === 'OK' &&
          response.data &&
          Array.isArray(response.data.page)
        ) {
          console.log('Data received:', response.data);
          // Iterate through each user data and load profile image if 'includeData' is true
          response.data.page.forEach((user: User) => {
            this.loadProfileImage(user.id, user.imageUrl);
          });
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
    const userPermissions = this.authService.getPermissionsFromToken();
    if (userPermissions.includes('DELETE:USER')) {
      const dialogRef = this.snackBar.open('Are you sure you want to delete this user?', 'Delete', {
        duration: 5000, // Adjust duration as needed
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        panelClass: ['snackbar-confirm'],
      });
      dialogRef.onAction().subscribe(() => {
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
      });
    } else {
      this.snackBar.open('You do not have permission to perform this action.', 'Close', {
        duration: 3000,
      });
    }
  }

  onUpdateUser(userId: number) { 
    const userPermissions = this.authService.getPermissionsFromToken();
    if (userPermissions.includes('UPDATE:USER')) {
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
  searchUsers(event: Event, type: string) {
    const value = (event.target as HTMLInputElement).value;
    console.log(`Searching users by ${type}:`, value);

    if (value) {
      let searchObservable: Observable<any>;
      switch (type) {
        case 'firstName':
          searchObservable = this.userService.getUsersByFirstName(value);
          break;
        case 'middleName':
          searchObservable = this.userService.getUsersByMiddleName(value);
          break;
        case 'lastName':
          searchObservable = this.userService.getUsersByLastName(value);
          break;
        case 'email':
          searchObservable = this.userService.getUsersByEmail(value);
          break;
        case 'address':
          searchObservable = this.userService.getUsersByAddress(value);
          break;
        case 'phone':
          searchObservable = this.userService.getUsersByPhone(value);
          break;
        default:
          return;
      }

      searchObservable.subscribe({
        next: (response: any) => {
          console.log('Response from server:', response);
          if (response.length > 0) {
            this.tableDataSource.data = response;
            this.tableDataSource.paginator = this.paginator;
            this.tableDataSource.sort = this.sort;
          } else {
            console.log('No users found.');
            this.tableDataSource.data = [];
          }
        },
        error: (error) => {
          console.error(`Error searching users by ${type}:`, error);
          this.snackBar.open(`Error searching users by ${type}`, 'Close', {
            duration: 3000,
          });
        },
      });
    } else {
      this.getAllUserData();
    }
  }
}
