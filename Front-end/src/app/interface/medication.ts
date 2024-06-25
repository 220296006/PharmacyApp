export interface Medication {
    id: number;
    name: string;
    dosage: string;
    frequency: string;
    prescription: {
        id: number;
    };
}