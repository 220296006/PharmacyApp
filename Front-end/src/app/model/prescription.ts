export interface Prescription {
  id: number;
  doctorName: string;
  doctorAddress: string;
  issue_date: Date;
  customer: {
  id: number;
  };
}
