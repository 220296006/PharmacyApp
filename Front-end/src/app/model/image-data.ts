import { User } from "./user";

export interface ImageData {
  id: number;
  name: string;
  type: string;
  user: User;
  imageData?: Blob; // Optional field to store image data as a Blob (client-side representation)
}
