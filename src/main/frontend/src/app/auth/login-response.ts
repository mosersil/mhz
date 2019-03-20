import {AuthUser} from "./auth-user";

export class LoginResponse {
  jwt: string;
  authUser: AuthUser;
}
