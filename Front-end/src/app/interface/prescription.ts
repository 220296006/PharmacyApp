export interface Prescription {
  id: number;
  doctorName: string;
  doctorAddress: string;
  issueDate: Date;
  customer: {
  id: number;
  };
}
