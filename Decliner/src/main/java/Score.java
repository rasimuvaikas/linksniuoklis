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
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet("/Score")
public class Score extends HttpServlet {

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
     * Get user's info and respond with the amount of correct vs. incorrect answers the user has made overall during the current session
     * @param request user's info
     * @param response correct and incorrect responses
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


        String username = jsonObject.getString("username");
        String time = jsonObject.getString("time");
        String inflection = jsonObject.getString("inflection");
        String number = jsonObject.getString("number");
        String declension = jsonObject.getString("declension");
        int correct = jsonObject.getInt("correct");
        int incorrect = jsonObject.getInt("incorrect");

        data.updateScores(inflection, number, username, time, declension, correct, incorrect);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JSONObject js = new JSONObject();

        ResultSet rs = data.getScoresOverall(username, time);
        int correctOverall = 0;
        int incorrectOverall = 0;

        try {
            while (rs.next()) {
                correctOverall = rs.getInt("sum(correct)");
                incorrectOverall = rs.getInt("sum(incorrect)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        js.put("correct", correctOverall);
        js.put("incorrect", incorrectOverall);

        out.println(js);

        out.close();


    }


    /**
     * Get user's results from previous learning sessions
     * @param request user's info
     * @param response user's results from previous sessions (for different inflections and declensions)
     * @throws UnsupportedEncodingException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {


        setAccessControlHeaders(response);

        request.setCharacterEncoding("utf-8");

        String user = request.getParameter("username");
        String time = request.getParameter("time");

        ResultSet rs = data.getRows("inflection", user, time);
        ResultSet rsDecl = data.getRows("declension", user, time);


        try {
            ArrayList<String> inflections;
            if(rs.next()){

                inflections = new ArrayList<>();
                do {

                    String i = rs.getString("inflection");
                    if(!inflections.contains(i)){
                        inflections.add(i);
                    }
                } while (rs.next());

                //if inflections result set is not null, then declensions will not be null either
                ArrayList<String> declensions = new ArrayList<>();
                while(rsDecl.next()){
                    String d = rsDecl.getString("declension");
                    if(!declensions.contains(d)){
                        declensions.add(d);
                    }
                }

                JSONArray infl = new JSONArray(); //store all inflection scores in one array first
                for(String s : inflections){
                    JSONObject js = new JSONObject();
                    ResultSet temp = data.getColumnScores("inflection", user, s, time);

                    String num;
                    if (s.endsWith("Sg")) {
                        num = "singular";
                    } else {
                        num = "plural";
                    }

                    js.put("number", num);

                    String inflection = "";

                    if (s.startsWith("nominative")) {
                        inflection = "nominative";
                    } else if (s.startsWith("genitive")) {
                        inflection = "genitive";
                    } else if (s.startsWith("dative")) {
                        inflection = "dative";
                    } else if (s.startsWith("accusative")) {
                        inflection = "accusative";
                    } else if (s.startsWith("instrumental")) {
                        inflection = "instrumental";
                    } else if (s.startsWith("locative")) {
                        inflection = "locative";
                    }

                    js.put("inflection", inflection);

                    temp.next();
                    int correct = temp.getInt("sum(correct)");
                    int incorrect = temp.getInt("sum(incorrect)");

                    js.put("correct", correct);
                    js.put("incorrect", incorrect);

                    infl.put(js);


                }

                JSONArray dec = new JSONArray(); //store all declension scores in one array first
                for(String s : declensions){


                    JSONObject all = new JSONObject(); //into dec

                    JSONObject js = new JSONObject(); //into all

                    ResultSet temp = data.getColumnScores("declension", user, s, time);
                    temp.next();
                    int correct = temp.getInt("sum(correct)");
                    int incorrect = temp.getInt("sum(incorrect)");

                    js.put("declension", s);
                    js.put("correct", correct);
                    js.put("incorrect", incorrect);

                    //find all the inflections this declension appeared in
                    ArrayList<String> decin = new ArrayList<>();
                    ResultSet in = data.getDeclensionInflections(s, user, time);
                    while(in.next()){
                        String i = in.getString("inflection");
                        if(!decin.contains(i)){
                            decin.add(i);
                        }
                    }

                    JSONArray decInside = new JSONArray(); //store all scores of one declension in different cases
                    for(String t : decin){
                        JSONObject d = new JSONObject();
                        ResultSet sc = data.getDeclensionScores(s, t, user, time);
                        sc.next();
                        int cor = sc.getInt("sum(correct)");
                        int incor = sc.getInt("sum(incorrect)");

                        d.put("correct", cor);
                        d.put("incorrect", incor);
                        d.put("declension", s);

                        String num;
                        if (t.endsWith("Sg")) {
                            num = "singular";
                        } else {
                            num = "plural";
                        }

                        d.put("number", num);

                        String inflection = "";

                        if (t.startsWith("nominative")) {
                            inflection = "nominative";
                        } else if (t.startsWith("genitive")) {
                            inflection = "genitive";
                        } else if (t.startsWith("dative")) {
                            inflection = "dative";
                        } else if (t.startsWith("accusative")) {
                            inflection = "accusative";
                        } else if (t.startsWith("instrumental")) {
                            inflection = "instrumental";
                        } else if (t.startsWith("locative")) {
                            inflection = "locative";
                        }

                        d.put("inflection", inflection);

                        decInside.put(d);


                    }

                    all.put("declension", js);
                    all.put("dinflections", decInside);

                   dec.put(all);

                }

                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();


                JSONObject result = new JSONObject();

                result.put("inflections", infl);
                result.put("declension", dec);
                System.out.println("score: " + result.toString());

                out.println(result);
                out.close();


            }

            else{ //first time user
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONObject result = new JSONObject();
                result.put("inflections", JSONObject.NULL);
                result.put("declension", JSONObject.NULL);
                out.println(result);
                out.close();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }


}
