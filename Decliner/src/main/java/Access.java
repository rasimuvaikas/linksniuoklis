

import org.json.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * A class that implements web crawlers for morphological text annotation and automatic accentuation
 */
public class Access {

    public static void main(String[] args) {
        Access test = new Access();

        try {
            //test.annotate();
            //test.fix();
            //test.stress();
            test.divide("stressed.tsv");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the first column from the tsv file, leave only Lithuanian + English sentences
     *
     * @throws IOException
     */
    public void fix() throws IOException {

        PrintWriter pw = new PrintWriter(new FileWriter("src/main/resources/sentences.tsv"));
        BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + "lit-eng-tatoeba-by-freq.tsv")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split("\\t");
            pw.println(split[1] + "\t" + split[2]);
        }

        pw.close();
        br.close();
    }

    /**
     * Web crawler for automatic morphological text annotation
     * Determines which sentences contain nouns and verbs and can therefore be used for further processing
     *
     * @param inputFile  input files that contains Lithuanian sentences
     * @param outputFile a file that sentences that contain nous and verbs are printed to
     * @throws IOException
     * @throws InterruptedException
     */
    public void annotate(String inputFile, String outputFile) throws IOException, InterruptedException {

        BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + inputFile)));

        String line;
        int count = 1;
        int l = 1;
        while ((line = br.readLine()) != null) {

            if (l == 1) { //this variable is to be manually incremented to the current tsv file line that has to be read IN CASE there is an unexpected problem with reading
                //any of the lines and the programme stops

                String[] split = line.split("\\t");
                String sent = split[0];

                PrintWriter pw = new PrintWriter(new FileWriter("src/main/resources/" + outputFile, true));

                //connect to the web service
                URL url = new URL("http://itpu.semantika.lt/Proxy/api/chains/morph");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("accept", "application/json;charset=utf-8");
                connection.setRequestProperty("Accept-Charset", "utf-8");
                connection.setRequestProperty("Content-Type", "text/plain;charset=utf-8");

                OutputStream os = connection.getOutputStream();

                os.write(sent.getBytes());
                os.flush();
                os.close();


                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();


                    JSONObject jO = new JSONObject(response.toString());

                    JSONArray jsonArray = jO.getJSONArray("annotations");
                    JSONObject needed = (JSONObject) jsonArray.get(1);
                    JSONObject jobj = needed.getJSONObject("annotation");
                    JSONArray msd = jobj.getJSONArray("msd");
                    boolean noun = false;
                    boolean verb = false;
                    for (Object o : msd) {

                        JSONArray pos = ((JSONArray) o).getJSONArray(0); //get the first one as that is most likely the correct annotation
                        if (pos.get(1).toString().substring(0, 1).equals("N")) {
                            noun = true;
                        }

                        if (pos.get(1).toString().substring(0, 1).equals("V")) {
                            verb = true;
                        }
                    }

                    if (verb && noun) {
                        pw.println(line);
                    }

                    pw.close();


                } else {
                    System.out.println("POST request did not work.");
                }

                if (count == 19) { //can only send post requests for 20 lines at a time
                    System.out.println("waiting");
                    TimeUnit.MINUTES.sleep(1);
                    count = 1;
                } else {
                    count++;
                }

            } else {
                l++;
            }
        }

        br.close();


    }

    /**
     * Web crawler for automatic text accentuation
     *
     * @param inputFile  a file with Lithuanian sentences that need to be accentuated
     * @param outputFile a file that accentuated sentences will be printed to
     * @throws IOException
     * @throws InterruptedException
     */
    public void stress(String inputFile, String outputFile) throws IOException, InterruptedException {

        BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + inputFile)));

        String line;
        int count = 1;
        int l = 1;
        while ((line = br.readLine()) != null) {
            if (l == 1) {//this variable is to be manually incremented to the current tsv file line that has to be read IN CASE there is an unexpected problem with reading
                //any of the lines and the programme stops

                String[] split = line.split("\\t");
                String sent = split[0];


                PrintWriter pw = new PrintWriter(new FileWriter("src/main/resources/" + outputFile, true));


                //connect to the web service
                URL url = new URL("http://klcdocker.vdu.lt/textaccenter/text-accents");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("accept", "application/json;charset=utf-8");
                connection.setRequestProperty("Accept-Charset", "utf-8");
                connection.setRequestProperty("Content-Type", "text/plain;charset=utf-8");

                OutputStream os = connection.getOutputStream();

                os.write(sent.getBytes());
                os.flush();
                os.close();


                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jO = new JSONObject(response.toString());

                    JSONArray jsonArray = jO.getJSONArray("textParts");
                    StringBuilder sB = new StringBuilder();
                    for (Object o : jsonArray) {
                        if (((JSONObject) o).get("type").equals("WORD") && !((JSONObject) o).get("accentType").equals("NONE")) {
                            sB.append(((JSONObject) o).get("accented"));
                        } else if (((JSONObject) o).get("type").equals("SEPARATOR")) {
                            sB.append(((JSONObject) o).get("string"));
                        } else {
                            sB = null;
                            break;
                        }
                    }
                    if (sB == null) {
                        continue;
                    } else {
                        pw.println(sB.toString() + "\t" + line);
                    }

                    pw.close();

                } else {
                    System.out.println("POST request did not work.");
                }

                if (count == 19) {  //can only send post requests for 20 lines at a time
                    System.out.println("waiting");
                    TimeUnit.MINUTES.sleep(1);
                    count = 1;
                } else {
                    count++;
                }
            } else {
                l++;
            }

        }


    }


    /**
     * Divide the stressed and annotated sentences into groups based on number and inflection of the nouns they contain
     *
     * @param inputFile a file that contains stressed and annotated sentences (in resources folder)
     * @throws InterruptedException
     * @throws IOException
     */
    public void divide(String inputFile) throws InterruptedException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + inputFile)));

        String line;
        int count = 1;
        int l = 1;
        while ((line = br.readLine()) != null) {

            if (l == 1) { //this variable is to be manually incremented to the current tsv file line that has to be read IN CASE there is an unexpected problem with reading
                //any of the lines and the programme stops

                String[] split = line.split("\\t");
                String sent = split[1];

                PrintWriter pw = null;

                URL url = new URL("http://itpu.semantika.lt/Proxy/api/chains/morph");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("accept", "application/json;charset=utf-8");
                connection.setRequestProperty("Accept-Charset", "utf-8");
                connection.setRequestProperty("Content-Type", "text/plain;charset=utf-8");

                OutputStream os = connection.getOutputStream();

                os.write(sent.getBytes());
                os.flush();
                os.close();


                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());

                    JSONObject jO = new JSONObject(response.toString());

                    JSONArray jsonArray = jO.getJSONArray("annotations");
                    JSONObject needed = (JSONObject) jsonArray.get(1);
                    JSONObject jobj = needed.getJSONObject("annotation");
                    JSONArray msd = jobj.getJSONArray("msd");
                    int position = 0;
                    for (Object o : msd) {

                        JSONArray pos = ((JSONArray) o).getJSONArray(0); //get the first one as that is most likely the correct annotation
                        if (pos.get(1).toString().substring(0, 1).equals("N")) {
                            String number = pos.get(1).toString().substring(3, 4);
                            String infl = pos.get(1).toString().substring(4, 5);
                            String[] spl = sent.split(" ?(?<!\\G)((?<=[^\\p{Punct}])(?=\\p{Punct})|\\b) ?");
                            String word = spl[position];
                            Declension d = new Declension();
                            Noun n = d.createNoun(word, pos.get(0).toString(), pos.get(1).toString());
                            String add = d.findDeclension(n);
                            System.out.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                            if (number.equals("s")) {
                                if (infl.equals("n")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/nominativeSg.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("g")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/genitiveSg.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("d")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/dativeSg.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("a")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/accusativeSg.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("i")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/instrumentalSg.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("l")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/locativeSg.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                }

                            } else if (number.equals("p")) {
                                if (infl.equals("n")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/nominativePl.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("g")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/genitivePl.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("d")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/dativePl.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("a")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/accusativePl.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("i")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/instrumentalPl.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                } else if (infl.equals("l")) {
                                    pw = new PrintWriter(new FileWriter("src/main/resources/locativePl.tsv", true));

                                    //stressed, lithuanian, english, word's position, word, lemma, info about word
                                    pw.println(line + "\t" + word + "\t" + position + "\t" + pos.get(0).toString() + "\t" + pos.get(1).toString() + "\t" + add);
                                    pw.close();
                                }
                            }
                        }
                        position++;
                    }

                } else {
                    System.out.println("POST request did not work.");
                }

                if (count == 19) {
                    System.out.println("waiting");
                    TimeUnit.MINUTES.sleep(1);
                    count = 1;
                } else {
                    count++;
                }
            } else {
                l++;
            }

        }

        br.close();
    }


    //***openSubtitles processing:***

    /**
     * Align english and Lithuanian OpenSub text files
     *
     * @throws IOException
     */
    public static void align() throws IOException {
        BufferedReader en = new BufferedReader(new FileReader("C:/Users/zivil/Downloads/opensub/OpenSubtitles.en-lt.en"));
        BufferedReader lit = new BufferedReader(new FileReader("C:/Users/zivil/Downloads/opensub/OpenSubtitles.en-lt.lt"));

        PrintWriter pw = new PrintWriter(new FileWriter("src/main/resources/openSub.tsv", true));


        while (true) {
            String partOne = lit.readLine();
            String partTwo = en.readLine();

            if (partOne == null || partTwo == null)
                break;

            pw.println(partOne + "\t" + partTwo);
        }

        pw.close();
    }

    /**
     * Find sentences longer than 5 words and shorter than 11
     *
     * @throws IOException
     */
    public static void find() throws IOException {
        BufferedReader lit = new BufferedReader(new FileReader("src/main/resources/openSub.tsv"));

        PrintWriter pw = new PrintWriter(new FileWriter("src/main/resources/openSubUsable.tsv", true));


        String line;
        int count = 1;
        int l = 1;
        while ((line = lit.readLine()) != null) {

            String[] split = line.split("\t");
            String[] words = split[0].split("\\s+");
            if (words.length > 5 && words.length < 11) {
                pw.println(line);
            }

        }

        pw.close();
    }


}
