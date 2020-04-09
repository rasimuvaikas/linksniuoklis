/**
 * A class that stores a noun and all its relevant morphological information
 */

public class Noun
{
    String noun;
    String lemma;
    String gender;
    String number;
    String infl;

    public Noun()
    {
        noun = "";
        lemma = "";
        gender = "";
        number = "";
        infl = "";
    }

    public Noun(String n, String l, String g, String num, String d)
    {
        noun = n;
        lemma = l;
        gender = g;
        number = num;
        infl = d;
    }

    public String getNoun() {
        return noun;
    }

    public String getGender() {
        return gender;
    }

    public String getNumber() {
        return number;
    }

    public String getInfl() {
        return infl;
    }

    public String getLemma()
    {
        return lemma;
    }
}
