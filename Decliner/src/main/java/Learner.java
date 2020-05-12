import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A class that updates and shares learner model information
 */
@WebServlet("/Learner")
public class Learner extends HttpServlet {
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
     * Check if there is a learner model created for a user. Respond with null if not, respond with the learner model if yes
     * @param request contains the username
     * @param response all the learner model information
     * @throws IOException
     * @throws ServletException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        setAccessControlHeaders(response);

        request.setCharacterEncoding("utf-8");

        String name = request.getParameter("username");

        ResultSet rs = data.getModel(name);
        ResultSet rsDecl = data.getDecls(name);


        try {

            if (!rs.next()) {

                data.setRecord(name);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                JSONArray jsonArray = new JSONArray();

                JSONObject jsb = new JSONObject();
               jsb.put("number", JSONObject.NULL);
                jsb.put("infl", JSONObject.NULL);
                jsb.put("level", JSONObject.NULL);
                jsb.put("username", name);
                jsb.put("declensions", JSONObject.NULL);

                jsonArray.put(jsb);
                out.println(jsonArray);

                out.close();

            } else {


                ResultSetMetaData rsmd = rs.getMetaData();


                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                //get the declensions

                ResultSetMetaData rsmdDecl = rsDecl.getMetaData();
                rsDecl.next();

                ArrayList<String> aL = new ArrayList<>();
                for(int k = 2; k < 11; k++){

                    if(rsDecl.getShort(k) == 1){
                        aL.add(rsmdDecl.getColumnLabel(k));
                    }
                }

                JSONArray decls = new JSONArray(aL); //store the declensions

                JSONArray jsonArray = new JSONArray();
                do {

                    for (int j = 2; j < 14; j++) {

                        JSONObject jsb = new JSONObject();

                        if(rs.getString(j)!=null){

                                jsb.put("username", name);
                                jsb.put("declensions", decls);


                                if (rsmd.getColumnLabel(j).endsWith("Sg")) {
                                    jsb.put("number", "singular");
                                } else {
                                    jsb.put("number", "plural");
                                }

                                if (rsmd.getColumnName(j).startsWith("nominative")) {
                                    jsb.put("infl", "nominative");
                                } else if (rsmd.getColumnName(j).startsWith("genitive")) {
                                    jsb.put("infl", "genitive");
                                } else if (rsmd.getColumnName(j).startsWith("dative")) {
                                    jsb.put("infl", "dative");
                                } else if (rsmd.getColumnName(j).startsWith("accusative")) {
                                    jsb.put("infl", "accusative");
                                } else if (rsmd.getColumnName(j).startsWith("instrumental")) {
                                    jsb.put("infl", "instrumental");
                                } else if (rsmd.getColumnName(j).startsWith("locative")) {
                                    jsb.put("infl", "locative");
                                }


                            jsb.put("level", rs.getString(j));
                        }

                        if(!jsb.isEmpty()) {

                            jsonArray.put(jsb);
                        }
                    }

                } while (rs.next());

                if(!jsonArray.isEmpty()){
                    out.println(jsonArray);
                }

                else{
                    JSONObject jsb = new JSONObject();
                    jsb.put("number", JSONObject.NULL);
                    jsb.put("infl", JSONObject.NULL);
                    jsb.put("level", JSONObject.NULL);
                    jsb.put("name", name);
                    jsb.put("declensions", JSONObject.NULL);
                    jsonArray.put(jsb);
                    out.println(jsonArray);
                }


                out.close();
            }

        } catch (SQLException e) {
            e.getMessage();
        }

    }

    /**
     * Update learner model and respond with the current learner model
     * @param request the learner model
     * @param response the updated learner model
     * @throws IOException
     * @throws ServletException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        setAccessControlHeaders(response);

        request.setCharacterEncoding("utf-8");
        StringBuffer sb = new StringBuffer();
        String line;


        //get the parameters
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (Exception e) {
            e.getMessage();
        }

        JSONArray jsonArray = new JSONArray(sb.toString());

        String username = null;
        ArrayList<String> declensions = null;
        JSONArray js = null;
        for(Object o : jsonArray){
            String number = ((JSONObject) o).getString("number");
            String infl = ((JSONObject) o).getString("infl");
            String level = ((JSONObject) o).getString("level");
            username = ((JSONObject) o).getString("username");
            js = ((JSONObject) o).getJSONArray("declensions");
            declensions = new ArrayList<String>();
            for (int i=0; i< js.length(); i++){
                declensions.add(js.getString(i));
            }

            data.updateModel(infl,number,level,username);
            data.updateDecls(declensions, username);
        }



        try {

            JSONArray arr = new JSONArray();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            ResultSet rs = data.getModel(username);
            ResultSetMetaData rsmd = rs.getMetaData();

            while(rs.next()) {

                for (int j = 2; j < 14; j++) {

                    JSONObject jsb = new JSONObject();

                    if (rs.getString(j) != null) { //the inflection has been selected

                            jsb.put("username", username);
                            jsb.put("declensions", js);


                            if (rsmd.getColumnLabel(j).endsWith("Sg")) {
                                jsb.put("number", "singular");
                            } else {
                                jsb.put("number", "plural");
                            }

                            if (rsmd.getColumnName(j).startsWith("nominative")) {
                                jsb.put("infl", "nominative");
                            } else if (rsmd.getColumnName(j).startsWith("genitive")) {
                                jsb.put("infl", "genitive");
                            } else if (rsmd.getColumnName(j).startsWith("dative")) {
                                jsb.put("infl", "dative");
                            } else if (rsmd.getColumnName(j).startsWith("accusative")) {
                                jsb.put("infl", "accusative");
                            } else if (rsmd.getColumnName(j).startsWith("instrumental")) {
                                jsb.put("infl", "instrumental");
                            } else if (rsmd.getColumnName(j).startsWith("locative")) {
                                jsb.put("infl", "locative");
                            }


                        jsb.put("level", rs.getString(j));
                    }

                    if (!jsb.isEmpty()) {

                        arr.put(jsb);
                    }
                }
            }

            out.println(arr);
            out.close();


        } catch (Exception e) {
            e.getMessage();
        }

    }

}
