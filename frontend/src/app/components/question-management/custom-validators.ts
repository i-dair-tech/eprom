import { FormControl } from '@angular/forms';

export function validAnswerChoice(control: FormControl) {
  const pattern =  /^.*\(\s*\d+\s*point[s]?\s*\)$/i;

  if (!pattern.test(control.value)) {
    return { invalidAnswerChoice: true };
  }

  return null;
}
