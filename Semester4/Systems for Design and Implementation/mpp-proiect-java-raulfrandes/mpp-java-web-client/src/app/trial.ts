import { Referee } from "./referee";

export interface Trial {
    id: number;
    name: string;
    description: string;
    referee: Referee;
}