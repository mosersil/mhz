import {ErrorDetail} from "./error-detail";

export class ChangePasswordResponse {
  errors: ErrorDetail[];

  public isSuccessful() {
    return (this.errors==null || this.errors.length==0);
  }
}
