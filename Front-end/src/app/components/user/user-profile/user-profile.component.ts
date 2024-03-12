import { Component } from '@angular/core';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent {
  selectedSection: string = 'profile';
  profileImageUrl: string | ArrayBuffer | null = 'assets/images/default.jpeg';
activityLogs: any;

  constructor() { }

  selectSection(section: string) {
    this.selectedSection = section;
  }

  handleFileInput(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = () => {
      this.profileImageUrl = reader.result;
    };

    reader.readAsDataURL(file);
  }

}
