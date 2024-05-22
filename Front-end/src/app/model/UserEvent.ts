import { Events } from "./event";
import { User } from "./user";

export interface UserEvent {
  id: number;
  device: string;
  ipAddress: string;
  createdAt: string;
  user: {
    id: number;
  };
  event: {
    id: number;
    type: string;
    description: string;
  };
}