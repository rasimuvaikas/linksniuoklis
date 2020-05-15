import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * A class that shares sentences with requested noun forms and information about the user's current learning level
 */
@WebServlet("/Share")
public class Share extends HttpServlet {

    Data data = null;

    public void init() {
        data = new Data();
    }


    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET");
        resp.setHeader("Access-Control-Allow-Headers", "content-type, x-customauthheader");
    }

    /**
     * Find a sentence in a database, share it
     * @param request the requirements for a kind of sentence
     * @param response the sentence along with information about the noun it contains
     * @throws IOException
     * @throws ServletException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        setAccessControlHeaders(response);

        request.setCharacterEncoding("utf-8");

        System.out.println("gavo request");

        String infl = request.getParameter("inflection");
        String num = request.getParameter("number");

        String pattern = request.getParameter("declension");

        System.out.println("su šitais param: " + infl + " " + num + " " + pattern);

        System.out.println("prašo resultset");

        ResultSet rs = data.getSentence(infl, num, pattern);

        System.out.println("gavo resultset");

        try {


            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            JSONObject jsb = new JSONObject();

            if(rs.next()) {


                String stressed = rs.getString("stressed");
                System.out.println(stressed);
                String simple = rs.getString("clean");
                System.out.println(simple);
                String english = rs.getString("english");
                System.out.println(english);
                String noun = rs.getString("noun");
                System.out.println(noun);
                int position = rs.getInt("position");
                System.out.println(position);
                String lemma = rs.getString("lemma");
                System.out.println(lemma);
                String info = rs.getString("info");
                System.out.println(info);
                String inflection = rs.getString("inflection");
                System.out.println(inflection);
                String number = rs.getString("number");
                System.out.println(number);
                String declension = rs.getString("pattern");
                System.out.println(declension);


                //create slices - plain text surrounding the noun in question; and strices - stressed text surrounding the noun in question
                String[] slice = simple.split(" ?(?<!\\G)((?<=[^\\p{Punct}])(?=\\p{Punct})|\\b) ?");
                String slice1 = "";
                String slice2 = "";
                for (int i = 0; i < position; i++) {
                    if (i == 0) {
                        slice1 += slice[i];
                    } else if (Pattern.matches("\\p{Punct}", slice[i])) {
                        slice1 += slice[i] + " ";
                    } else if (i == position - 1) {
                        slice1 += " " + slice[i] + " ";
                    } else {
                        slice1 += " " + slice[i];
                    }
                }

                for (int i = position + 1; i < slice.length; i++) {
                    if (i == position + 1) {
                        slice2 += " " + slice[i];
                    } else if (Pattern.matches("\\p{Punct}", slice[i])) {
                        slice2 += slice[i] + " ";
                    } else if (i == slice.length - 1) {
                        slice2 += " " + slice[i] + " ";
                    } else {
                        slice2 += " " + slice[i];
                    }
                }

                System.out.println("slices: " + slice1 + " " + slice2);

                String[] strice = stressed.split(" ?(?<!\\G)((?<=[^\\p{Punct}])(?=\\p{Punct})|\\b) ?");
                String strice1 = "";
                String strice2 = "";
                for (int i = 0; i < position; i++) {
                    if (i == 0) {
                        strice1 += strice[i];
                    } else if (Pattern.matches("\\p{Punct}", strice[i])) {
                        strice1 += strice[i] + " ";
                    } else if (i == position - 1) {
                        strice1 += " " + strice[i] + " ";
                    } else {
                        strice1 += " " + strice[i];
                    }
                }

                for (int i = position + 1; i < strice.length; i++) {
                    if (i == position + 1) {
                        strice2 += " " + strice[i];
                    } else if (Pattern.matches("\\p{Punct}", strice[i])) {
                        strice2 += strice[i] + " ";
                    } else if (i == strice.length - 1) {
                        strice2 += " " + strice[i] + " ";
                    } else {
                        strice2 += " " + strice[i];
                    }
                }

                System.out.println("strices: " + strice1 + " " + strice2);
                //get declension distractors
                ArrayList<String> l = new ArrayList<>();

                Declension decl = new Declension();
                Noun n = decl.createNoun(noun, lemma, info);
                for (String s : decl.generateDist(n)) {
                    l.add(s);

                }
                l.add(noun);


                Collections.shuffle(l);
                JSONArray nounDist = new JSONArray(l);

                System.out.println("distractors: " + l.toString());


                //get accentuation distractors
                ArrayList<String> m = new ArrayList<>();

                Stress str = new Stress();
                ArrayList<ArrayList<String>> dist = str.findDistractors(strice[position]);
                for (ArrayList<String> a : dist) {
                    String d = str.generateDistractor(a, slice[position]);
                    m.add(d);
                }

                m.add(strice[position]);

                Collections.shuffle(m);
                JSONArray stressDist = new JSONArray(m);

                System.out.println("stressers: " + m.toString());

                String gender = "";
                if(n.getGender().equals("m")){
                    gender = "masculine";
                } else if(n.getGender().equals("f")){
                    gender = "feminine";
                }
                jsb.put("gender", gender);

                jsb.put("stressed", stressed);
                jsb.put("simple", simple);
                jsb.put("english", english);
                jsb.put("noun", noun);
                jsb.put("accNoun", strice[position]);
                jsb.put("position", position);
                jsb.put("lemma", lemma);
                jsb.put("info", info);
                jsb.put("inflection", inflection);
                jsb.put("number", number);
                jsb.put("slice1", slice1);
                jsb.put("slice2", slice2);
                jsb.put("strice1", strice1);
                jsb.put("strice2", strice2);
                jsb.put("nounDist", nounDist);
                jsb.put("stressDist", stressDist);
                jsb.put("declension", declension);
            }

            else{
                jsb.put("gender", JSONObject.NULL);

                jsb.put("stressed", JSONObject.NULL);
                jsb.put("simple", JSONObject.NULL);
                jsb.put("english", JSONObject.NULL);
                jsb.put("noun", JSONObject.NULL);
                jsb.put("accNoun", JSONObject.NULL);
                jsb.put("position", JSONObject.NULL);
                jsb.put("lemma", JSONObject.NULL);
                jsb.put("info", JSONObject.NULL);
                jsb.put("inflection", JSONObject.NULL);
                jsb.put("number", JSONObject.NULL);
                jsb.put("slice1", JSONObject.NULL);
                jsb.put("slice2", JSONObject.NULL);
                jsb.put("strice1", JSONObject.NULL);
                jsb.put("strice2", JSONObject.NULL);
                jsb.put("nounDist", JSONObject.NULL);
                jsb.put("stressDist", JSONObject.NULL);
                jsb.put("declension", JSONObject.NULL);
            }


            out.println(jsb);

            out.close();


        } catch (SQLException e) {
            e.getMessage();
        }

    }

    /**
     * Get info of a sentence that appeared in a successfully completed exercise, ,update the progress table, respond with the user's progress regarding the current inflection/number, declension
     * @param request
     * @param response
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        setAccessControlHeaders(response);

        request.setCharacterEncoding("utf-8");
        StringBuffer sb = new StringBuffer();
        String line;


        //get the parameters

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            sb.append(line);

        JSONObject jsonObject = new JSONObject(sb.toString());

        String username = null;

        String number = jsonObject.getString("number");
        String infl = jsonObject.getString("inflection");
        String decl = jsonObject.getString("declension");
        username = jsonObject.getString("username");
        String level = jsonObject.getString("level");

        data.updateProgress(infl, number, decl, username, level);


        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            ResultSet rs = data.getProgress(infl, number, username, level);
            ResultSetMetaData rsmd = rs.getMetaData();

            JSONObject jsb = new JSONObject();
            while(rs.next()){

                String user = rs.getString(1);

                jsb.put("username", user);

                String num;
                if (rs.getString(2).endsWith("Sg")) {
                    num = "singular";
                } else {
                    num = "plural";
                }

                jsb.put("number", num);

                String inflection = "";

                if (rs.getString(2).startsWith("nominative")) {
                    inflection = "nominative";
                } else if (rs.getString(2).startsWith("genitive")) {
                    inflection = "genitive";
                } else if (rs.getString(2).startsWith("dative")) {
                    inflection = "dative";
                } else if (rs.getString(2).startsWith("accusative")) {
                    inflection = "accusative";
                } else if (rs.getString(2).startsWith("instrumental")) {
                    inflection = "instrumental";
                } else if (rs.getString(2).startsWith("locative")) {
                    inflection = "locative";
                }

                jsb.put("inflection", inflection);

                jsb.put("total",  rs.getInt(4)); //number of exercises completed in total

                jsb.put("level", rs.getString(3));

                JSONArray decls = new JSONArray(); //store the declensions

                for(int i = 5; i < 14; i++){

                    if(rs.getInt(i) > 0){
                        JSONObject temp = new JSONObject();

                        ResultSet countDecl = data.getNumNouns(inflection, num, rsmd.getColumnLabel(i));
                        countDecl.next();
                        int count = countDecl.getInt("count(*)");
                        temp.put(rsmd.getColumnLabel(i), rs.getInt(i));
                        temp.put("count", count); //number of sentences with this declension
                        decls.put(temp);
                    }

                }

                jsb.put("declensions", decls);

                ResultSet declensions = data.getDecls(inflection, num);
                declensions.next();

                jsb.put("total_declensions", declensions.getInt("count(distinct pattern)")); //the number of distinct declensions that appear in this inflection




            }

            out.println(jsb);

            out.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}