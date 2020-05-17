/**
 * Interface that tracks the user's score at each inflection, number, and declension combination during the current session
 */
export interface Score {
    username:string;
    time:string;
    inflection:string;
    number:string;
    declension:string;
    correct:number; //0 for incorrect, 1 for correct
    incorrect:number;//1 for incorrect, 0 for correct
}
