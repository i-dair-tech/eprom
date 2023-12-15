import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-mail-config',
  templateUrl: './mail-config.component.html',
  styleUrls: ['./mail-config.component.scss']
})
export class MailConfigComponent implements OnInit {

  credentialsForm: FormGroup;

  constructor(private userService: UserService,private fb: FormBuilder) { }



  ngOnInit(): void {
    this.credentialsForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

    // addCredentials(username: string, password: string): void {
    //   this.userService.addCredentials(username, password)
    //     .subscribe(
    //       () => {
    //         console.log('Credentials added successfully.');
    //       },
    //       (error) => {
    //         console.error('Error adding credentials:', error);
    //       }
    //     );
    // }


    submitForm(): void {
      if (this.credentialsForm.valid) {
        const { username, password } = this.credentialsForm.value;
  
        this.userService.addCredentials(username, password)
          .subscribe(
            () => {
              Swal.fire({
                icon: 'success',
                title: 'Added!',
                text: 'the mail config has been added successfully',
                customClass: {
                  confirmButton: 'btn btn-success'
                }
              })
              this.credentialsForm.reset()
            },
            (error) => {
              Swal.fire({
                icon: 'error',
                title: 'ERROR!',
                text: 'try again !',
                customClass: {
                  confirmButton: 'btn btn-primary'
                }
              })
            }
          );
      }
    }

}
