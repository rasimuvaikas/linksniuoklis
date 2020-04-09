/**
 * Interface that tracks the user's level at each inflection for each number
 * as well as which declension patterns they are currently practicing
 */
export interface Level {
    
    number:string;
    level:string;
    infl:string;
    username:string;
    declensions:string[];
}
