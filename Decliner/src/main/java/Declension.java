import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class all about noun declension and case generation
 */
public class Declension {

    public static void main(String[] args) {
        Declension d = new Declension();
/*        Noun test = d.createNoun("bulves", "bulvė", "Ncfpan");
        for (String s : d.generateDist(test)) {
            System.out.println(s);
        }*/
        //d.createDeclensionsFile();


    }


    /**
     * Create a noun object
     * @param noun the noun
     * @param lemma the lemma
     * @param info information about the noun in semantika.lt Morphological annotator format
     * @return a Noun object
     */
    public Noun createNoun(String noun, String lemma, String info) {

        //info e.g. : Ncfsn

        String gender = info.substring(2, 3);
        String number = info.substring(3, 4);
        String infl = info.substring(4, 5);

        return new Noun(noun, lemma, gender, number, infl);
    }

    /**
     * Generate a list of noun distractors (nouns in different inflections than the original)
     * @param noun the original noun
     * @return a list of two distractors
     */
    public ArrayList<String> generateDist(Noun noun) {

        ArrayList<String> inflections = new ArrayList<>(Arrays.asList("n", "g", "d", "a", "i", "l"));

        ArrayList<String> result = new ArrayList<>();
        while (result.size() < 2) {
            int rand = (int) (Math.random() * 6);
            String dist = declineN(noun, inflections.get(rand));
            if (Character.isUpperCase(noun.getNoun().charAt(0))) {
                dist = dist.substring(0, 1).toUpperCase() + dist.substring(1);
            }
            if (!result.contains(dist) && !dist.equals(noun.getNoun())) {
                result.add(dist);
            }
        }

        return result;

    }


    /**
     * Decline a noun to a desired case
     *
     * @param n    the noun object that contains the noun
     * @param rqrd the desired case
     * @return a declined noun
     */
    public String declineN(Noun n, String rqrd) {
        String noun = n.getNoun();
        String lemma = n.getLemma();

        if (n.getNumber().equals("s")) {
            if (n.getGender().equals("m")) {
                if (rqrd.equals("n")) {
                    noun = lemma;
                } else if (rqrd.equals("g")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "o";
                    } else if (lemma.equals("šuo")) {
                        noun = "šuns";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesio";
                    } else if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ies";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ens";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žio";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žio";
                    } else if (lemma.equals("petys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ies";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čio";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čio";
                    } else if (lemma.endsWith("jis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "o";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "io";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "io";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "aus";
                    }
                } else if (rqrd.equals("d")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ui";
                    } else if (lemma.equals("šuo")) {
                        noun = "šuniui";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesiui";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "eniui";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiui";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiui";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiui";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiui";
                    } else if (lemma.endsWith("jis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ui";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iui";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iui";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ui";
                    }
                } else if (rqrd.equals("a")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ą";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunį";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesį";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enį";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "į";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "į";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    }
                } else if (rqrd.equals("i")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "u";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunimi";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesiu";
                    } else if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "imi";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "eniu";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiu";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiu";
                    } else if (lemma.equals("petys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "imi";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiu";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiu";
                    } else if (lemma.endsWith("jis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "u";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iu";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iu";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "umi";
                    }
                } else if (rqrd.equals("l")) {
                    if (lemma.endsWith("ias")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "yje";
                    } else if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "e";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunyje";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesyje";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enyje";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "yje";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "yje";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "uje";
                    }
                }
            } else {
                if (rqrd.equals("n")) {
                    noun = lemma;
                } else if (rqrd.equals("g")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "os";
                    } else if (lemma.equals("moteris")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "s";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ers";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ers";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čios";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ės";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ies";
                    }
                } else if (rqrd.equals("d")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ai";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "eriai";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "eriai";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čiai";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ei";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiai";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiai";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iai";
                    }
                } else if (rqrd.equals("a")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ą";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "erį";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "erį";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čią";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ę";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "į";
                    }
                } else if (rqrd.equals("i")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "a";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "erimi";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "erimi";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čia";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "e";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "imi";
                    }
                } else if (rqrd.equals("l")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "oje";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "eryje";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "eryje";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čioje";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ėje";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "yje";
                    }
                }
            }
        } else {
            if (n.getGender().equals("m")) {
                if (rqrd.equals("n")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ai";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunys";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesiai";
                    } else if (lemma.equals("žmogus")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "nės";
                    } else if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ys";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enys";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiai";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiai";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiai";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiai";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iai";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iai";
                    } else if (lemma.endsWith("ius")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ai";
                    } else if (lemma.endsWith("jus")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ai";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ūs";
                    }
                } else if (rqrd.equals("g")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunų";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesių";
                    } else if (lemma.equals("žmogus")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "nių";
                    } else if (lemma.equals("dantis") || lemma.equals("debesis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enų";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žių";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žių";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čių";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čių";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ių";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ių";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    }
                } else if (rqrd.equals("d")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ams";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunims";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesiams";
                    } else if (lemma.equals("žmogus")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "nėms";
                    } else if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ims";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enims";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiams";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiams";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiams";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiams";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iams";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iams";
                    } else if (lemma.endsWith("ius")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "iams";
                    } else if (lemma.endsWith("jus")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ams";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ums";
                    }
                } else if (rqrd.equals("a")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "us";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunis";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesius";
                    } else if (lemma.equals("žmogus")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "nes";
                    } else if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                        noun = lemma;
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enis";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žius";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žius";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čius";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čius";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ius";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ius";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "us";
                    }
                } else if (rqrd.equals("i")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ais";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunimis";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesiais";
                    } else if (lemma.equals("žmogus")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "nėmis";
                    } else if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "imis";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enimis";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiais";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiais";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiais";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiais";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iais";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iais";
                    } else if (lemma.endsWith("ius")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "iais";
                    } else if (lemma.endsWith("jus")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ais";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "umis";
                    }
                } else if (rqrd.equals("l")) {
                    if (lemma.endsWith("as")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "uose";
                    } else if (lemma.equals("šuo")) {
                        noun = "šunyse";
                    } else if (lemma.equals("mėnuo")) {
                        noun = "mėnesiuose";
                    } else if (lemma.equals("žmogus")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "nėse";
                    } else if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "yse";
                    } else if (lemma.endsWith("uo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "enyse";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiuose";
                    } else if (lemma.endsWith("dys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žiuose";
                    } else if (lemma.endsWith("tys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiuose";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čiuose";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iuose";
                    } else if (lemma.endsWith("ys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "iuose";
                    } else if (lemma.endsWith("ius")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "uose";
                    } else if (lemma.endsWith("us")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ūse";
                    }
                }
            } else {
                if (rqrd.equals("n")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "os";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "erys";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "erys";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čios";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ės";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ys";
                    }
                } else if (rqrd.equals("g")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ų";
                    } else if (lemma.equals("grindys") || lemma.equals("durys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    } else if (lemma.equals("sultys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čių";
                    } else if (lemma.equals("lubos")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    } else if (lemma.equals("naktis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "erų";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "erų";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čių";
                    } else if (lemma.endsWith("dis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "žių";
                    } else if (lemma.endsWith("tis")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "čių";
                    } else if (lemma.endsWith("dė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "žių";
                    } else if (lemma.endsWith("tė")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čių";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ių";
                    } else if (lemma.endsWith("lis") || lemma.equals("ugnis") || lemma.equals("krosnis") || lemma.equals("vilnis") || lemma.equals("nosis") || lemma.equals("akis")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ių";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ų";
                    }
                } else if (rqrd.equals("d")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "oms";
                    } else if (lemma.equals("grindys") || lemma.equals("durys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ims";
                    } else if (lemma.equals("sultys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "tims";
                    } else if (lemma.equals("lubos")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "oms";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "erims";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "erims";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čioms";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ėms";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ims";
                    }
                } else if (rqrd.equals("a")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "as";
                    } else if (lemma.equals("grindys") || lemma.equals("durys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "is";
                    } else if (lemma.equals("sultys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "tis";
                    } else if (lemma.equals("lubos")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "as";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "eris";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "eris";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čias";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "es";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "is";
                    }
                } else if (rqrd.equals("i")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "omis";
                    } else if (lemma.equals("grindys") || lemma.equals("durys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "imis";
                    } else if (lemma.equals("sultys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "imis";
                    } else if (lemma.equals("lubos")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "omis";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "erimis";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "erimis";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čiomis";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ėmis";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "imis";
                    }
                } else if (rqrd.equals("l")) {
                    if (lemma.endsWith("a")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ose";
                    } else if (lemma.equals("grindys") || lemma.equals("durys")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "yse";
                    } else if (lemma.equals("sultys")) {
                        noun = lemma.substring(0, lemma.length() - 3) + "tyse";
                    } else if (lemma.equals("lubos")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "ose";
                    } else if (lemma.equals("duktė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "eryse";
                    } else if (lemma.equals("sesuo")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "eryse";
                    } else if (lemma.equals("marti")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "čiose";
                    } else if (lemma.endsWith("ė")) {
                        noun = lemma.substring(0, lemma.length() - 1) + "ėse";
                    } else if (lemma.endsWith("is")) {
                        noun = lemma.substring(0, lemma.length() - 2) + "yse";
                    }
                }
            }
        }
        return noun;
    }


    /**
     * Find the declension of a noun
     * @param n a noun object that contains the noun
     * @return the declensions class the noun belongs to
     * @throws IOException
     */
    public String findDeclension(Noun n) throws IOException {
        String noun = n.getNoun();
        String lemma = n.getLemma();

        String result = "";

        ArrayList<String> palat = new ArrayList<>(Arrays.asList("avis", "ugnis", "akis", "krosnis", "vilnis", "nosis"));


        if (n.getGender().equals("m")) {
            if (lemma.endsWith("dantis") || lemma.endsWith("vagis") || lemma.endsWith("debesis") || lemma.endsWith("žvėris")) {
                result = "3masc";
            } else if (lemma.equals("šuo") || lemma.equals("mėnuo") || lemma.equals("petys") || lemma.equals("žmogus")) {
                result = "exception";
            } else if (lemma.endsWith("as") || lemma.endsWith("ai")) {
                result = "1mascIAS";
            } else if(lemma.endsWith("is") || lemma.endsWith("ys")){
                result = "1mascIS";
            } else if (lemma.endsWith("us")) {
                result = "4masc";
            } else if (lemma.endsWith("uo")) {
                result = "5masc";
            }
        } else if (n.getGender().equals("f")) {
            if (lemma.equals("duktė") || lemma.equals("sesuo")) {
                result = "5fem";
            } else if (lemma.endsWith("a") || lemma.endsWith("os") || lemma.equals("marti")) {
                result = "2femIA";
            } else if(lemma.endsWith("ė") || lemma.endsWith("ės")){
                result = "2femĖ";
            } else if (lemma.endsWith("is") || lemma.endsWith("ys")) {
                result = "3fem";
            }
        }

        return result;

    }

}
