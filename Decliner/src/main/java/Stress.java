import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

/**
 * A class that analyses stressed words, performs statistical analyses on stressed word data,
 * and generates distractors for accentuation exercises
 */
public class Stress {

    private ArrayList<Integer> count = new ArrayList<>();
    private HashMap<Pair, Integer> tokenSeq = new HashMap<>();
    private HashMap<String, Integer> allTokens = new HashMap<>();
    private HashMap<String, Integer> stressedTokens = new HashMap<>();
    private HashMap<String, Integer> cleanSeq = new HashMap<>();

    public static void main(String[] args) {
        Stress s = new Stress();

        //ArrayList<String> test = new ArrayList<>(Arrays.asList("į̃", "o"));
        //s.generateDistractor(test, "grįžo");
        //s.findPatterns(s.findSyllables("slenksčiaìs", false));
        try {
            //s.mapIt();
            for (ArrayList<String> a : s.findDistractors("ánglų")) {
                s.generateDistractor(a, "anglų");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Determine which relative position in a word the token takes
     *
     * @param pos      the token's position
     * @param arrayLen the length of the array of token sequences
     * @return the relative position as string
     */
    private String findPosition(int pos, int arrayLen) {
        if (pos == 0) {
            return "first";
        } else if (pos == arrayLen - 1) {
            return "last";
        } else {
            return "middle";
        }
    }

    //word is accentuated word

    /**
     * Determine two different most likely stress patterns for the same word to generate distractors later
     *
     * @param word the accentuated word to find distractors for
     * @return an arraylist of two arraylists that contain accentuated token sequences
     * @throws IOException
     */
    public ArrayList<ArrayList<String>> findDistractors(String word) throws IOException {

        //connect to mysql server
        Data data = new Data();

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        word = Normalizer.normalize(word, Normalizer.Form.NFC);

        Pair pair = findPatterns(findSyllables(word, false));

        ArrayList<String> clean = pair.getClean();
        ArrayList<String> org = pair.getOrg();

        //1: try to find the exact same token sequences that are accentuated in a different way:

        HashMap<ArrayList<String>, Double> sequences = new HashMap<>();
        ResultSet tokenSeq = data.getSeq(clean.toString());

        try {
            if (tokenSeq.next()) {
                do {
                    String str = tokenSeq.getString("stressed");
                    double pr = tokenSeq.getDouble("prob");

                    if (!org.toString().equals(str)) {
                        ArrayList<String> aL = new ArrayList<>();
                        String[] array = str.substring(1, str.length() - 1).split(", "); //put the sequence into an arraylist
                        for (String s : array) {
                            aL.add(s);
                        }
                        sequences.put(aL, pr);
                    }

                } while (tokenSeq.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //determine the most likely sequences:
        while (sequences.size() > 0 && result.size() < 2) {
            double high = 0.0;
            ArrayList<String> add = new ArrayList<>();
            for (ArrayList<String> a : sequences.keySet()) {
                if (sequences.get(a) > high) {
                    high = sequences.get(a);
                    add = a;
                }
            }
            if (!result.contains(add)) {
                result.add(add);
            }
            sequences.remove(add, high);
        }


        if (result.size() == 2) {
            return result;
        }


        //not enough sequences found? move to possible accentuations for separate tokens
        else {

            //hashmap of clean tokens as keys, and as values: hashmaps of their accentuated equivalents as keys and their probabilities given the token's position as values
            HashMap<String, HashMap<String, String>> tokens = new HashMap<>();

            String position;


            for (int k = 0; k < clean.size(); k++) {

                String token = clean.get(k);
                token = Normalizer.normalize(token, Normalizer.Form.NFC);

                position = findPosition(k, pair.getLen());
                HashMap<String, String> pos = new HashMap<>(); //temporary hashmap to add to the tokens hashmap as value


                ResultSet tokenProb = data.getToken(token);

                try {
                    if (tokenProb.next()) {
                        do {
                            String str = tokenProb.getString("stressed");
                            double first = tokenProb.getDouble("first");
                            double middle = tokenProb.getDouble("middle");
                            double last = tokenProb.getDouble("last");

                            System.out.println("other str: " + str);

                            String positions = "";
                            if (first != 0.0) {
                                positions += "first" + "\t" + first;
                            }
                            if (middle != 0.0) {
                                if (positions.length() > 0) {
                                    positions += "\t" + "middle" + "\t" + middle;
                                } else {
                                    positions += "middle" + "\t" + middle;
                                }
                            }

                            if (last != 0.0) {
                                if (positions.length() > 0) {
                                    positions += "\t" + "last" + "\t" + last;
                                } else {
                                    positions += "last" + "\t" + last;
                                }
                            }

                            pos.put(str, positions);

                            //put the hashmaps of accentuated tokens and their probabilities into a hashmap that has a clean token as key for easier access
                            tokens.put(token, pos);

                        } while (tokenProb.next());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            //find the stressed tokens that are in the exact same position in the sequence as the current clean token
            HashMap<String, HashMap<String, Double>> exacts = new HashMap<>();


            for (int k = 0; k < clean.size(); k++) {

                HashMap<String, Double> exact = new HashMap<>();

                String token = clean.get(k);
                token = Normalizer.normalize(token, Normalizer.Form.NFC);

                position = findPosition(k, pair.getLen());
                for (String c : tokens.keySet()) {
                    if (c.equals(token)) {
                        for (String s : tokens.get(c).keySet()) {
                            String[] spl = tokens.get(c).get(s).split("\t");
                            for (int i = 0; i < spl.length; i++) {
                                if (spl[i].equals(position)) {
                                    exact.put(s, Double.parseDouble(spl[i + 1]));
                                    exacts.put(token, exact);
                                }
                            }
                        }
                    }
                }

            }

            //determine the most likely accentuation
            while (exacts.size() > 0 && result.size() < 2) {

                double high = 0.0;
                ArrayList<String> add = new ArrayList<>();
                String acc = "";
                String nonAcc = "";
                //find the most probable stress patterns first
                for (String s : exacts.keySet()) {
                    for (String c : exacts.get(s).keySet()) {
                        if (exacts.get(s).get(c) > high) {
                            high = exacts.get(s).get(c);
                            acc = c;
                            nonAcc = s;
                        }
                    }
                }


                //create an arraylist that contains the accentuated token
                for (int j = 0; j < clean.size(); j++) {
                    if (j == clean.indexOf(nonAcc)) {
                        add.add(acc);
                    } else {
                        add.add(clean.get(j));
                    }
                }


                if (result.size() < 2 && !add.equals(org) && !result.contains(add) && !add.equals(clean)) {
                    result.add(add);
                }


                exacts.get(nonAcc).remove(acc);
                if (exacts.get(nonAcc).size() == 0) {
                    exacts.remove(nonAcc);
                }

                tokens.get(nonAcc).remove(acc);

            }

            if (result.size() == 2) {
                return result;
            }


            //if enough stress patterns still could not be found, introduce some flexibility with token positions
            else {


                HashMap<String, HashMap<String, Double>> flexes = new HashMap<>();
                for (int k = 0; k < clean.size(); k++) {
                    String token = clean.get(k);
                    token = Normalizer.normalize(token, Normalizer.Form.NFC);
                    position = findPosition(k, pair.getLen());


                    HashMap<String, Double> flex = new HashMap<>();


                    for (String c : tokens.keySet()) {
                        if (c.equals(token)) {
                            for (String s : tokens.get(c).keySet()) {
                                String[] spl = tokens.get(c).get(s).split("\t");
                                for (int i = 0; i < spl.length; i++) {
                                    if (position.equals("first") && spl[i].equals("middle") || position.equals("middle") && spl[i].equals("last")) {
                                        flex.put(s, Double.parseDouble(spl[i + 1]));
                                        flexes.put(token, flex);
                                    }
                                }
                            }
                        }
                    }
                }

                //determine the most likely accentuation
                while (flexes.size() > 0 && result.size() < 2) {
                    double high = 0.0;
                    ArrayList<String> add = new ArrayList<>();
                    String acc = "";
                    String nonAcc = "";
                    //find the most probable stress patterns first
                    for (String s : flexes.keySet()) {
                        for (String c : flexes.get(s).keySet()) {
                            if (flexes.get(s).get(c) > high) {
                                high = flexes.get(s).get(c);
                                acc = c;
                                nonAcc = s;
                            }
                        }
                    }

                    //create an arraylist that contains the accentuated token
                    for (int j = 0; j < clean.size(); j++) {
                        if (j == clean.indexOf(nonAcc)) {
                            add.add(acc);
                        } else {
                            add.add(clean.get(j));
                        }
                    }


                    if (result.size() < 2 && !add.equals(org) && !result.contains(add) && !add.equals(clean)) {
                        result.add(add);
                    }


                    flexes.get(nonAcc).remove(acc);
                    if (flexes.get(nonAcc).size() == 0) {
                        flexes.remove(nonAcc);
                    }

                    tokens.get(nonAcc).remove(acc);
                }
            }

            if (result.size() == 2) {
                return result;
            }

            //whichever position from what's still left in the tokens hashmap
            else {
                for (int k = 0; k < clean.size(); k++) {

                    String token = clean.get(k);
                    token = Normalizer.normalize(token, Normalizer.Form.NFC);

                    HashMap<String, String> pos = null;

                    for (String c : tokens.keySet()) {
                        if (c.equals(token)) {
                            pos = new HashMap<>(tokens.get(c));
                            ArrayList<String> add = new ArrayList<>();
                            String acc = "";
                            if (pos.size() > 0) {
                                for (String s : pos.keySet()) {
                                    acc = s;
                                    for (int j = 0; j < clean.size(); j++) {
                                        if (j == k) {
                                            add.add(acc);
                                        } else {
                                            add.add(clean.get(j));
                                        }
                                    }

                                    if (result.size() < 2 && !add.equals(org) && !result.contains(add) && !add.equals(clean)) {
                                        result.add(add);
                                    }

                                    if (result.size() == 2) {
                                        return result;
                                    }
                                }

                            }
                        }
                    }
                }
            }


            if (result.size() == 2) {
                return result;
            }


            //last resort
            else {

                while (result.size() < 2) {

                    ArrayList<String> a = new ArrayList<>(Arrays.asList("à", "á", "ã"));
                    ArrayList<String> e = new ArrayList<>(Arrays.asList("è", "é", "ẽ"));
                    ArrayList<String> i = new ArrayList<>(Arrays.asList("ì", "í", "ĩ"));
                    ArrayList<String> u = new ArrayList<>(Arrays.asList("ù", "ú", "ũ"));
                    ArrayList<String> o = new ArrayList<>(Arrays.asList("ò", "ó", "õ"));
                    ArrayList<String> y = new ArrayList<>(Arrays.asList("ý", "ỹ"));

                    for (int k = 0; k < clean.size(); k++) {
                        String token = clean.get(k);
                        String acc = "";
                        for (int j = 0; j < token.length(); j++) {
                            switch (token.charAt(j)) {
                                case 'a':
                                    if (a.size() > 0) {
                                        acc = token.replace(token.charAt(j), a.get(0).charAt(0));
                                        a.remove(a.get(0));
                                    }
                                case 'e':
                                    if (e.size() > 0) {
                                        acc = token.replace(token.charAt(j), e.get(0).charAt(0));
                                        e.remove(a.get(0));
                                    }
                                case 'i':
                                    if (i.size() > 0) {
                                        acc = token.replace(token.charAt(j), i.get(0).charAt(0));
                                        i.remove(a.get(0));
                                    }
                                case 'u':
                                    if (u.size() > 0) {
                                        acc = token.replace(token.charAt(j), u.get(0).charAt(0));
                                        u.remove(a.get(0));
                                    }
                                case 'o':
                                    if (o.size() > 0) {
                                        acc = token.replace(token.charAt(j), o.get(0).charAt(0));
                                        o.remove(a.get(0));
                                    }
                                case 'y':
                                    if (y.size() > 0) {
                                        acc = token.replace(token.charAt(j), y.get(0).charAt(0));
                                        y.remove(a.get(0));
                                    }
                            }

                            if (acc.length() > 0) {
                                ArrayList<String> add = new ArrayList<>();
                                for (int h = 0; h < clean.size(); h++) {
                                    if (h == k) {
                                        add.add(token);
                                    } else {
                                        add.add(clean.get(h));
                                    }
                                }

                                if (result.size() < 2 && !add.equals(org) && !result.contains(add) && !add.equals(clean)) {
                                    result.add(add);
                                }
                            }
                        }
                    }
                }
            }

            return result;
        }
    }


    /**
     * Generate a stressed word that serves as a distractor to the original one
     * @param dist an arraylist that contains a stressed token to be inserted into a clean version of the word
     * @param cleanWord the clean version of the stressed word
     * @return
     */
    public String generateDistractor(ArrayList<String> dist, String cleanWord) {

        findSyllables(cleanWord, true); //generate count arraylist that contains the indices of consonants that go inbetween tokens (vowels or diphthongs)
        String result = "";

        int j = 0; //dist index
        int i = 0; //count index
        while (i < this.count.size()) {


            if (i == 0) {
                //word starts with a consonant
                if (count.get(0) == 0) {

                    //check if next char is a consonant, if so, skip to the end of a consonant sequence
                    while (i < count.size() - 1 && count.get(i) + 1 == count.get(i + 1)) {
                        i++;
                    }

                    if(i == count.size() - 1){ //the word contains one consonant/consonant sequence
                        result = cleanWord.substring(0, i + 1) + dist.get(0);
                    } else {
                        result = cleanWord.substring(0, i + 1) + dist.get(0) + cleanWord.substring(count.get(i + 1));
                    }

                } else { //word starts with the first dist token

                    result = dist.get(0) + cleanWord.substring(count.get(0));
                    j++;
                    //check if next char is a consonant, if so, skip to the end of a consonant sequence
                    while (i < count.size() - 1 && count.get(i) + 1 == count.get(i + 1)) {
                        i++;
                    }
                    int diff = result.length() - cleanWord.length(); //if a previously inserted token is longer than the original version, move count indices forward
                    if(i < count.size() - 1) { //if we have more than one consonant sequence with at least an index inbetween then there must be a vowel inbetween, so no need to check if j is less than dist.size()
                        result = result.substring(0, count.get(i) + 1 + diff) + dist.get(j) + result.substring(count.get(i + 1) + diff);
                    } else if(j < dist.size()){ //word contains two vowels, one consonant sequence inbetween
                        result = result.substring(0, count.get(i) + 1 + diff) + dist.get(j);
                    } //else no more vowels, result stays the same

                }
                //check if next char is a consonant, if so, skip to the end of a consonant sequence
                int a = i + 1;
                while (a < count.size() - 1 && count.get(a) + 1 == count.get(a + 1)) {
                    a++;
                }
                i = a;
                j++;

            } else if (i == count.size() - 1) {
                int diff = result.length() - cleanWord.length(); //if a previously inserted token is longer than the original version, move count indices forward
                if(j < dist.size()){
                    result = result.substring(0, count.get(i) + 1 + diff) + dist.get(j);
                } else{ //no more vowels left
                    result = result.substring(0, count.get(i) + 1 + diff);
                }
                i++;
            } else {
                int diff = result.length() - cleanWord.length();
                result = result.substring(0, count.get(i) + diff + 1) + dist.get(j) + result.substring(count.get(i + 1) + diff);

                //check if next char is a consonant, if so, skip to the end of a consonant sequence
                int a = i + 1;
                while (a < count.size() - 1 && count.get(a) + 1 == count.get(a + 1)) {
                    a++;
                }
                i = a;
                j++;
            }
        }

        this.count.clear(); //clear count for the next distractor

        //make sure the new word looks the same as the original except stressed
        if (Character.isUpperCase(cleanWord.charAt(0))) {
            result = result.substring(0, 1).toUpperCase() + result.substring(1);
        }
        return result;
    }


    /**
     * Separate a word into tokens i.e. diphthongs and vowels sequences, leave out the rest
     * @param word the word for which the tokens need to be found
     * @param countIt true if count arraylist should be created, false if not
     * @return an array of tokens that loosely represent syllables
     */
    public String[] findSyllables(String word, boolean countIt) {

        ArrayList<String> diphthongs = new ArrayList(Arrays.asList("ái", "aĩ", "áu", "aũ", "éi", "eĩ", "ùi", "uĩ",
                "ál", "al̃", "ám", "am̃", "án", "añ", "ár", "ar̃", "él", "el̃", "ém", "em̃", "én", "eñ", "ìl", "il̃",
                "ìn", "iñ", "ìm", "im̃", "ìr", "ir̃", "ùl", "ul̃", "ùm", "um̃", "ùn", "uñ", "ùr", "ur̃", "ér", "er̃",
                "íe", "iẽ", "úo", "uõ", "èl", "el̃", "èm", "em̃", "èn", "eñ", "èr", "er̃"));

        String cons = "bcčdfghjklmnprsštvzž";

        word = Normalizer.normalize(word, Normalizer.Form.NFC);

        word = word.toLowerCase();


        //find the accentuated diphthongs and make sure their chars aren't recognised as consonants later
        String dvg = "";
        for (String s : diphthongs) {
            //make sure it's actually a diphthong, i.e. is succeeded by a consonant or end of the word
            if (Pattern.matches(".*" + s + "([bcčdfghjklmnrpsštvzž].*|$)", word)) {
                dvg = s;
                word = word.replace(s, "ww"); //replace the dipthong with letters that do not exist in LT alphabet, for now
            }
        }

        //replace all consonants with a "C" except for those that are part of diphthongs
        word = word.replaceAll("([lmnr](?<![auie])(?!([bcčdfghjklmnrpsštvzž].*|$))|[bcčdfghjkpsštvzž])", "C");

        //add the indices of consonants to the count arraylist to be used in generateDistractor method
        if (countIt) {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == 'C') {
                    count.add(i);
                }
            }
        }

        //put the tokens inbetween the C's into an array
        String[] split = word.split("C+");
        String[] result = null;
        if (word.charAt(0) == 'C') {
            result = new String[split.length - 1];
            for (int i = 1; i < split.length; i++) {
                result[i - 1] = split[i];
            }
        } else {
            result = new String[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = split[i];
            }
        }

        //put the dipthong back into the token
        for (int i = 0; i < result.length; i++) {
            if (result[i].contains("ww")) {
                result[i] = result[i].replaceAll("ww", dvg);
            }
        }

        return result;
    }

    /**
     * Find which token in an array of tokens is stressed
     * @param syll the array of vowel/dipthong tokens
     * @return a pair object that contains the clean version of the stressed array, the position of the stressed token and the length of the array
     */
    public Pair findPatterns(String[] syll) {

        ArrayList<String> clean = new ArrayList<>(); //a list of "clean" tokens (one of them no longer stressed)
        ArrayList<String> org = new ArrayList<>(); //a list of original tokens (one of them stressed)


        int position = 77; //set position to 77 to easily spot bugs later

        String gr = "[àèìùò]";
        String ac = "[óýáéíú]";
        String nonUniCode = "[ąęėįųūilmr]";
        String til = "[ãẽõỹñũĩ]";

        String tilde = "\\x{0303}";
        String acute = "\\x{0301}";
        String grave = "\\x{0300}";


        int c = 0; //keep track of which position of the array we are currently at
        for (String s : syll) {

            //i is sometimes accentuated as "ì" but other times as the proper Lithuanian version "i̇̀"
            //in the latter case it is a string comprised of three symbols. for the sake of simplicity and consistency it is best to replace it with its simpler
            //unicode version before any further processing
            if (s.contains("i̇̀")) {
                s = s.replaceAll("i̇̀", "ì");
            } else if (s.contains("i̇́")) {
                s = s.replaceAll("i̇́", "í");
            } else if (s.contains("į̇̃")) {
                s = s.replaceAll("į̇̃", "į\u0303");
            }

            org.add(s);
            s = Normalizer.normalize(s, Normalizer.Form.NFC);

            //a stressed token of length of 1 could only be stressed in one of the following ways
            if (s.length() == 1) {
                if (Pattern.matches(gr, s) || Pattern.matches(ac, s) || Pattern.matches(til, s)) {
                    position = c;
                    if (s.equals("à") || s.equals("ã") || s.equals("á")) {
                        clean.add("a");
                    } else if (s.equals("è") || s.equals("ẽ") || s.equals("é")) {
                        clean.add("e");
                    } else if (s.equals("ì") || s.equals("í") || s.equals("ĩ")) {
                        clean.add("i");
                    } else if (s.equals("ù") || s.equals("ú") || s.equals("ũ")) {
                        clean.add("u");
                    } else if (s.equals("ó") || s.equals("õ") || s.equals("ò")) {
                        clean.add("o");
                    } else if (s.equals("ý") || s.equals("ỹ")) {
                        clean.add("y");
                    }
                } else {
                    clean.add(s);
                }

                //for longer tokens, first look for a stressed unicode character anywhere in the sequence of chars
            } else {
                boolean found = false;
                for (int i = 0; i < s.length(); i++) {
                    if (Pattern.matches(gr, s.substring(i, i + 1)) || Pattern.matches(ac, s.substring(i, i + 1)) || Pattern.matches(til, s.substring(i, i + 1))) {
                        found = true;
                        position = c;
                        if (i == 0) {
                            if (s.substring(i, i + 1).equals("à") || s.substring(i, i + 1).equals("ã") || s.substring(i, i + 1).equals("á")) {
                                clean.add("a" + s.substring(1));
                            } else if (s.substring(i, i + 1).equals("è") || s.substring(i, i + 1).equals("ẽ") || s.substring(i, i + 1).equals("é")) {
                                clean.add("e" + s.substring(1));
                            } else if (s.substring(i, i + 1).equals("ì") || s.substring(i, i + 1).equals("í") || s.substring(i, i + 1).equals("ĩ")) {
                                clean.add("i" + s.substring(1));
                            } else if (s.substring(i, i + 1).equals("ù") || s.substring(i, i + 1).equals("ú") || s.substring(i, i + 1).equals("ũ")) {
                                clean.add("u" + s.substring(1));
                            } else if (s.substring(i, i + 1).equals("ó") || s.substring(i, i + 1).equals("õ") || s.substring(i, i + 1).equals("ò")) {
                                clean.add("o" + s.substring(1));
                            } else if (s.substring(i, i + 1).equals("ý") || s.substring(i, i + 1).equals("ỹ")) {
                                clean.add("y" + s.substring(1));
                            } else if (s.substring(i, i + 1).equals("ñ")) {
                                clean.add("n" + s.substring(1));
                            }
                        } else if (i == s.length() - 1) {
                            if (s.substring(i, i + 1).equals("à") || s.substring(i, i + 1).equals("ã") || s.substring(i, i + 1).equals("á")) {
                                clean.add(s.substring(0, i) + "a");
                            } else if (s.substring(i, i + 1).equals("è") || s.substring(i, i + 1).equals("ẽ") || s.substring(i, i + 1).equals("é")) {
                                clean.add(s.substring(0, i) + "e");
                            } else if (s.substring(i, i + 1).equals("ì") || s.substring(i, i + 1).equals("í") || s.substring(i, i + 1).equals("ĩ")) {
                                clean.add(s.substring(0, i) + "i");
                            } else if (s.substring(i, i + 1).equals("ù") || s.substring(i, i + 1).equals("ú") || s.substring(i, i + 1).equals("ũ")) {
                                clean.add(s.substring(0, i) + "u");
                            } else if (s.substring(i, i + 1).equals("ó") || s.substring(i, i + 1).equals("õ") || s.substring(i, i + 1).equals("ò")) {
                                clean.add(s.substring(0, i) + "o");
                            } else if (s.substring(i, i + 1).equals("ý") || s.substring(i, i + 1).equals("ỹ")) {
                                clean.add(s.substring(0, i) + "y");
                            } else if (s.substring(i, i + 1).equals("ñ")) {
                                clean.add(s.substring(0, i) + "n");
                            }

                        } else {
                            if (s.substring(i, i + 1).equals("à") || s.substring(i, i + 1).equals("ã") || s.substring(i, i + 1).equals("á")) {
                                clean.add(s.substring(0, i) + "a" + s.substring(i + 1));
                            } else if (s.substring(i, i + 1).equals("è") || s.substring(i, i + 1).equals("ẽ") || s.substring(i, i + 1).equals("é")) {
                                clean.add(s.substring(0, i) + "e" + s.substring(i + 1));
                            } else if (s.substring(i, i + 1).equals("ì") || s.substring(i, i + 1).equals("í") || s.substring(i, i + 1).equals("ĩ")) {
                                clean.add(s.substring(0, i) + "i" + s.substring(i + 1));
                            } else if (s.substring(i, i + 1).equals("ù") || s.substring(i, i + 1).equals("ú") || s.substring(i, i + 1).equals("ũ")) {
                                clean.add(s.substring(0, i) + "u" + s.substring(i + 1));
                            } else if (s.substring(i, i + 1).equals("ó") || s.substring(i, i + 1).equals("õ") || s.substring(i, i + 1).equals("ò")) {
                                clean.add(s.substring(0, i) + "o" + s.substring(i + 1));
                            } else if (s.substring(i, i + 1).equals("ý") || s.substring(i, i + 1).equals("ỹ")) {
                                clean.add(s.substring(0, i) + "y" + s.substring(i + 1));
                            } else if (s.substring(i, i + 1).equals("ñ")) {
                                clean.add(s.substring(0, i) + "n" + s.substring(i + 1));
                            }
                        }

                        //if the stressed char is not a unicode character it could be a unicode char followed by a grave, acute or tilde accent
                    } else if (s.length() > i + 1 && Pattern.matches(nonUniCode, s.substring(i, i + 1)) && (Pattern.matches(grave, s.substring(i + 1, i + 2)) || Pattern.matches(acute, s.substring(i + 1, i + 2)) || Pattern.matches(tilde, s.substring(i + 1, i + 2)))) {
                        found = true;
                        position = c;
                        if (i == 0) {
                            if (s.length() > 2) {
                                clean.add(s.substring(i, i + 1) + s.substring(i + 2));
                            } else {
                                clean.add(s.substring(i, i + 1));
                            }
                        } else if (i == s.length() - 2) {
                            clean.add(s.substring(0, i + 1));
                        } else {
                            clean.add(s.substring(0, i + 1) + s.substring(i + 2));
                        }
                    }

                }

                if (!found) {
                    clean.add(s);
                } else {
                    found = false;
                }
            }
            c++;
        }


        Pair p = new Pair(clean, org, position, org.size());

        //put the pair object into a hashmap
        if (!tokenSeq.containsKey(p)) {
            tokenSeq.put(p, 1);
        } else {
            tokenSeq.put(p, tokenSeq.get(p) + 1);
        }


        return p;
    }

    /**
     * create Pair objects for every word in a stressed word file
     * add different parts of these objects to different hashmaps for further statistical analysis
     * @throws IOException
     */
    public void mapIt() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + "stressed.tsv")));

        String line;

        while ((line = br.readLine()) != null) {

            String[] split = line.split("\t");
            String stressed = split[0];
            String[] words = stressed.split("[\\p{Punct}\\s]+");
            for (String word : words) {
                Pair pair = findPatterns(findSyllables(word, false));

                //find how many times a particular token occurs overall, accentuated or not
                for (String s : pair.getClean()) {
                    if (!allTokens.containsKey(s)) {
                        allTokens.put(s, 1);
                    } else {
                        allTokens.put(s, allTokens.get(s) + 1);
                    }
                }

                //find how many times a particular non-accentuated token sequence occurs
                if (!cleanSeq.containsKey(pair.getClean().toString())) {
                    cleanSeq.put(pair.getClean().toString(), 1);
                } else {
                    cleanSeq.put(pair.getClean().toString(), cleanSeq.get(pair.getClean().toString()) + 1);
                }


                //find how many times a token occurs as accentuated in a particular way
                if (!stressedTokens.containsKey(pair.getClean().get(pair.getPosition()) + "\t" + pair.getOrg().get(pair.getPosition()))) {
                    stressedTokens.put(pair.getClean().get(pair.getPosition()) + "\t" + pair.getOrg().get(pair.getPosition()), 1);
                } else {
                    stressedTokens.put(pair.getClean().get(pair.getPosition()) + "\t" + pair.getOrg().get(pair.getPosition()), stressedTokens.get(pair.getClean().get(pair.getPosition()) + "\t" + pair.getOrg().get(pair.getPosition())) + 1);
                }
            }
        }

        br.close();

        //print everything out into tsv files

        PrintWriter pw = new PrintWriter(new FileWriter("src/main/resources/probs.tsv", true));
        for(Pair p : tokenSeq.keySet()){
            pw.println(p.getClean() + "\t" + p.getOrg() + "\t" + tokenSeq.get(p));
        }

        pw.close();

       PrintWriter str = new PrintWriter(new FileWriter("src/main/resources/stressedTokens.tsv", true));

        for(String s : stressedTokens.keySet()){
            str.println(s + "\t" + stressedTokens.get(s));
        }

        str.close();


        PrintWriter all = new PrintWriter(new FileWriter("src/main/resources/tokensOverall.tsv", true));

        for(String s : allTokens.keySet()){
            all.println(s + "\t" + allTokens.get(s));
        }

        all.close();

        PrintWriter wr = new PrintWriter(new FileWriter("src/main/resources/accentProbabilities.tsv", true));

        for (String s : stressedTokens.keySet()) {
            String[] sp = s.split("\t");
            String acc = sp[1];
            String simple = sp[0];
            for (String t : allTokens.keySet()) {
                if (t.equals(simple)) {
                    //calculate the probability that a specific tokens will appear as stressed in this way
                    double prob = stressedTokens.get(s) / (double) allTokens.get(t);
                    wr.println(s + "\t" + prob);
                }
            }
        }

        wr.close();

        PrintWriter prin = new PrintWriter(new FileWriter("src/main/resources/sequenceProbabilities.tsv", true));

        for (Pair p : tokenSeq.keySet()) {
            for (String t : cleanSeq.keySet()) {
                if (p.getClean().toString().equals(t)) {
                    //calculate the probability that a specific token sequence will appear as stressed in this way
                    double prob = tokenSeq.get(p) / (double) cleanSeq.get(t);
                    prin.println(p.getClean() + "\t" + p.getOrg() + "\t" + prob);

                }
            }
        }

        prin.close();


        PrintWriter pos = new PrintWriter(new FileWriter("src/main/resources/positionProbabilities.tsv", true));


        for(String s : stressedTokens.keySet()){
            String[] sp = s.split("\t");
            String acc = sp[1];
            String simple = sp[0];
            int first = 0;
            int middle = 0;
            int last = 0;
            String positions = "";
            for(Pair pair : tokenSeq.keySet()){

                if (pair.getOrg().get(pair.getPosition()).equals(acc)) {
                    if(pair.getPosition() == 0){

                        first += tokenSeq.get(pair);

                    } else if(pair.getPosition() == pair.getLen() - 1){

                        last += tokenSeq.get(pair);

                    } else{

                        middle += tokenSeq.get(pair);

                    }
                }
            }

            //find position probabilities for every stressed token
            double f = first / (double) stressedTokens.get(s);
            double m = middle / (double) stressedTokens.get(s);
            double l = last / (double) stressedTokens.get(s);

            positions = "first" + "\t" + f + "\t" + "middle" + "\t" + m + "\t" + "last" + "\t" + l;

            pos.println(s + "\t" + positions);
        }

        pos.close();




        //find the conditional probability that a word stressed in a particular way will appear in a particular position
        PrintWriter con = new PrintWriter(new FileWriter("src/main/resources/conditionalProbabilities.tsv", true));

        BufferedReader bre = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + "stats/accentProbabilities.tsv")));

        String l;

        while ((l = bre.readLine()) != null) {

            String[] split = l.split("\t");
            String acc = split[1];
            double prob = Double.parseDouble(split[2]);

            BufferedReader bred = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("/" + "stats/positionProbabilities.tsv")));

            String li;
            while ((li = bred.readLine()) != null) {
                String[] spl = li.split("\t");
                String accented = spl[1];
                if (acc.equals(accented)) {
                    double first = 0.0;
                    double middle = 0.0;
                    double last = 0.0;
                    for (int i = 0; i < spl.length; i++) {
                        if (spl[i].equals("first")) {
                            first = Double.parseDouble(spl[i + 1]) * prob;
                        } else if (spl[i].equals("middle")) {
                            middle = Double.parseDouble(spl[i + 1]) * prob;
                        } else if (spl[i].equals("last")) {
                            last = Double.parseDouble(spl[i + 1]) * prob;
                        }
                    }

                    con.println(split[0] + "\t" + split[1] + "\t" + "first" + "\t" + first + "\t" + "middle" + "\t" + middle + "\t" + "last" + "\t" + last);
                }
            }
        }

        con.close();


    }


    /**
     * A class that holds information about vowel/diphthong token sequences that make up stressed words
     */
    private class Pair {

        ArrayList<String> clean;
        ArrayList<String> org;
        int position;
        int len; //array length


        public Pair(ArrayList<String> clean, ArrayList<String> org, int position, int len) {
            this.clean = clean;
            this.org = org;
            this.position = position;
            this.len = len;
        }

        public ArrayList<String> getClean() {
            return clean;
        }

        public void setClean(ArrayList<String> clean) {
            this.clean = clean;
        }

        public ArrayList<String> getOrg() {
            return org;
        }

        public void setOrg(ArrayList<String> org) {
            this.org = org;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return position == pair.position &&
                    len == pair.len &&
                    Objects.equals(clean, pair.clean) &&
                    Objects.equals(org, pair.org);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clean, org, position, len);
        }

        @Override
        public String toString() {
            return clean.toString() + "\t" + org.toString() +
                    "\t" + position +
                    "\t" + len;
        }
    }


}
