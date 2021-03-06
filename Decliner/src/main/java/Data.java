import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * A class that connects to the mysql server
 */
public class Data {


    public static void main(String[] args) {
        Data d = new Data();
        d.fillTables();
        d.model();
    }


    PreparedStatement pstmt = null;
    Statement stmt = null;
    ResultSet rs = null;
    Connection conn = null;

    String driver;
    String username;
    String password;


    public Data() {


        try {

            //connect to mysql service using a properties file
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream("dbconfig.properties");

            Properties prop = new Properties ();
            prop.load(stream);
            System.out.println("Reading properties...");
            driver = (String) prop.get("db.driver");
            String url = (String) prop.get("db.url");
            username = (String) prop.get("db.username");
            password = (String) prop.get ("db.password");

            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            info.put("characterEncoding", "utf8");
            Class.forName(driver);

            System.out.println("Connecting...");
            conn = DriverManager.getConnection(url, info);
            stmt = conn.createStatement();
            System.out.println("Connected.");

        } catch (SQLException | ClassNotFoundException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeDB(String db){

        try{
            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            info.put("characterEncoding", "utf8");
            Class.forName(driver);

            String url = "jdbc:mysql://localhost/" + db + "?allowLoadLocalInfile=true&useTimezone=true&serverTimezone" +
                    "=UTC&defaultCharacterSet=utf8mb4";

            System.out.println("Connecting to " + db + " database");
            conn = DriverManager.getConnection(url, info);
            stmt = conn.createStatement();
            System.out.println("Connected.");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create databases and tables for statistical data and the sentences containing nouns
     */
    public void fillTables() {
        try {
            //create a database to store data on accentuation mark frequencies

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS stats CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci");

            stmt.executeUpdate("USE stats");

            String tokenSeq = "CREATE TABLE IF NOT EXISTS tokenSeq" +
                    "(clean NVARCHAR(100), " +
                    " stressed NVARCHAR(100), " +
                    " prob FLOAT) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci";

            stmt.executeUpdate(tokenSeq);

            stmt.executeUpdate("SET GLOBAL local_infile = 1");

            String load = "LOAD DATA LOCAL INFILE '" + getClass().getResource("stats/sequenceProbabilities.tsv").toURI().getPath() + "' INTO TABLE tokenSeq CHARACTER SET utf8mb4";
            stmt.executeUpdate(load);

            String conditionalProb = "CREATE TABLE IF NOT EXISTS conditionalProb" +
                    "(clean NVARCHAR(100), " +
                    " stressed NVARCHAR(100), " +
                    " first FLOAT, " +
                    " middle FLOAT, " +
                    " last FLOAT) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci";

            stmt.executeUpdate(conditionalProb);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("/stats/conditionalProbabilities.tsv"), "UTF8"));
            String line;
            while ((line = br.readLine()) != null) {

                String[] split = line.split("\t");
                double first = 0.0;
                double middle = 0.0;
                double last = 0.0;
                for (int i = 0; i < split.length; i++) {
                    if (split[i].equals("first")) {
                        first = Double.parseDouble(split[i + 1]);
                    } else if (split[i].equals("middle")) {
                        middle = Double.parseDouble(split[i + 1]);
                    } else if (split[i].equals("last")) {
                        last = Double.parseDouble(split[i + 1]);
                    }
                }

                stmt.executeUpdate("INSERT INTO conditionalProb VALUES(\"" + split[0] + "\", \"" + split[1] + "\", " + first + ", " + middle + ", " + last + ")");
            }

            br.close();



            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci");


            stmt.executeUpdate("USE text");

            String nouns = "CREATE TABLE IF NOT EXISTS nouns" +
                    "(stressed NVARCHAR(200), " +
                    " clean NVARCHAR(200), " +
                    " english NVARCHAR(200), " +
                    " noun NVARCHAR(50), " +
                    " position MEDIUMINT, " +
                    " lemma NVARCHAR(50), " +
                    " info NVARCHAR(20)," +
                    " pattern NVARCHAR(20)," +
                    " inflection NVARCHAR(20), " +
                    " number NVARCHAR(20))";

            stmt.executeUpdate(nouns);

            URL url = getClass().getResource("infl");
            File temp = new File(url.toURI());

            for (String s : temp.list()) {

                stmt.executeUpdate("LOAD DATA LOCAL INFILE '" + getClass().getResource("infl/" + s).toURI().getPath() + "' INTO TABLE nouns CHARACTER SET utf8mb4 lines TERMINATED BY '\r\n'");

                if (s.endsWith("Pl.tsv")) {
                    stmt.executeUpdate("UPDATE nouns SET number = \"plural\" WHERE number IS NULL");
                } else if (s.endsWith("Sg.tsv")) {
                    stmt.executeUpdate("UPDATE nouns SET number = \"singular\" WHERE number IS NULL");
                }

                if (s.startsWith("nominative")) {
                    stmt.executeUpdate("UPDATE nouns SET inflection = \"nominative\" WHERE inflection IS NULL");
                } else if (s.startsWith("genitive")) {
                    stmt.executeUpdate("UPDATE nouns SET inflection = \"genitive\" WHERE inflection IS NULL");
                } else if (s.startsWith("dative")) {
                    stmt.executeUpdate("UPDATE nouns SET inflection = \"dative\" WHERE inflection IS NULL");
                } else if (s.startsWith("accusative")) {
                    stmt.executeUpdate("UPDATE nouns SET inflection = \"accusative\" WHERE inflection IS NULL");
                } else if (s.startsWith("instrumental")) {
                    stmt.executeUpdate("UPDATE nouns SET inflection = \"instrumental\" WHERE inflection IS NULL");
                } else if (s.startsWith("locative")) {
                    stmt.executeUpdate("UPDATE nouns SET inflection = \"locative\" WHERE inflection IS NULL");
                }
            }

        } catch (SQLException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a database and tables for learner model information
     */
    public void model(){

        try {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS models CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci");

            stmt.executeUpdate("USE models");


            String tab = "CREATE TABLE IF NOT EXISTS learnermodel" +
                    "(username NVARCHAR(100), " +
                    " nominativeSg NVARCHAR(100), " +
                    " genitiveSg NVARCHAR(100)," +
                    " dativeSg NVARCHAR(100)," +
                    " accusativeSg NVARCHAR(100), "+
                    " instrumentalSg NVARCHAR(100)," +
                    " locativeSg NVARCHAR(100)," +
                    " nominativePl NVARCHAR(100), " +
                    " genitivePl NVARCHAR(100)," +
                    " dativePl NVARCHAR(100)," +
                    " accusativePl NVARCHAR(100)," +
                    " instrumentalPl NVARCHAR(100)," +
                    " locativePl NVARCHAR(100))";

            stmt.executeUpdate(tab);

            String tabDecl = "CREATE TABLE IF NOT EXISTS declensions" +
                    "(username NVARCHAR(100), " +
                    " 1mascIAS SMALLINT," +
                    " 1mascIS SMALLINT," +
                    " 2femIA SMALLINT," +
                    " 2femĖ SMALLINT," +
                    " 3masc SMALLINT," +
                    " 3fem SMALLINT," +
                    " 4masc SMALLINT," +
                    " 5masc SMALLINT," +
                    " 5fem SMALLINT," +
                    " exception SMALLINT)";

            stmt.executeUpdate(tabDecl);

            String progress = "CREATE TABLE IF NOT EXISTS PROGRESS" +
                    "(username NVARCHAR(100)," +
                    " inflection NVARCHAR(100)," +
                    " level NVARCHAR(100)," +
                    " total SMALLINT DEFAULT 0," +
                    " 1mascIAS SMALLINT DEFAULT 0," +
                    " 1mascIS SMALLINT DEFAULT 0," +
                    " 2femIA SMALLINT DEFAULT 0," +
                    " 2femĖ SMALLINT DEFAULT 0," +
                    " 3masc SMALLINT DEFAULT 0," +
                    " 3fem SMALLINT DEFAULT 0," +
                    " 4masc SMALLINT DEFAULT 0," +
                    " 5masc SMALLINT DEFAULT 0," +
                    " 5fem SMALLINT DEFAULT 0," +
                    " exception SMALLINT DEFAULT 0)";

            stmt.executeUpdate(progress);


            String scores = "CREATE TABLE IF NOT EXISTS SCORES" +
                    "(username NVARCHAR(100)," +
                    " time NVARCHAR(100)," +
                    " declension NVARCHAR(100)," +
                    " inflection NVARCHAR(100)," +
                    " correct SMALLINT DEFAULT 0," +
                    " incorrect SMALLINT DEFAULT 0)";

            stmt.executeUpdate(scores);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * Get a list of stressed token sequences that correspond to the non stressed sequence
     * @param sequence the non stressed sequence
     * @return a resultset of token sequences that correspond to the non stressed sequence
     */
    public ResultSet getSeq(String sequence){
        try {

            changeDB("stats");

            String sql = "SELECT * FROM tokenSeq where clean = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sequence);
            rs = pstmt.executeQuery();

        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Get a list of probabilities that a token will appear as stressed in different positions of a word
     * @param token a non-stressed token
     * @return a result set of probabilities that a token will appear as stressed in different positions of a word
     */
    public ResultSet getToken(String token){
        try {

            changeDB("stats");

            String sql = "SELECT * FROM conditionalProb where clean = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);
            rs = pstmt.executeQuery();

        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Get a sentence that contains a noun that belong to a particular declension and is in a particular case and number
     * @param infl the case
     * @param num the number
     * @param decl the declension
     * @return a result set that contains a sentence that contains a noun that belong to a particular declension and is in a particular case, number
     */
    public ResultSet getSentence(String infl, String num, String decl){

        ResultSet result = null;
        try {

            changeDB("text");
            String sql = "SELECT * FROM nouns where inflection = ? AND number = ? AND pattern = ? ORDER BY RAND() LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, infl);
            pstmt.setString(2, num);
            pstmt.setString(3, decl);
            result = pstmt.executeQuery();

        } catch (SQLException e) {
            e.getMessage();
        }

        return result;
    }

    /**
     * Initialise learner model
     * @param user username of the learner
     */
    public void setRecord(String user)
    {
        try {

            changeDB("models");
            ResultSet check = stmt.executeQuery("SELECT count(*) FROM learnermodel where username = '" + user +"' COLLATE utf8mb4_bin");
            check.next();
            if(check.getInt("count(*)") == 0) {
                String sql = "INSERT INTO learnermodel VALUES(?, null, null, null, null, null, null, null, null, null, null, null, null)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user);
                pstmt.executeUpdate();

                String sql2 = "INSERT INTO declensions VALUES(?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)";
                pstmt = conn.prepareStatement(sql2);
                pstmt.setString(1, user);
                pstmt.executeUpdate();

            }
        }

        catch(SQLException e)
        {
            e.getMessage();
        }
    }

    /**
     * Get the current information stored about the learner
     * @param username username of the learner
     * @return result set of learner information
     */
    public ResultSet getModel(String username) {

        try {
            changeDB("models");
            String sql = "SELECT * FROM learnermodel WHERE username = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }


    /**
     * Update learner model
     * @param infl inflection
     * @param number number
     * @param level the level the user has reached in a particular inflection/number
     * @param username the username of the learner
     */
    public void updateModel(String infl, String number, String level, String username){
        try {
            changeDB("models");

            String column = generateInfl(infl, number);

            String sql = "UPDATE learnermodel SET " + column + " = ? WHERE username = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, level);
            pstmt.setString(2, username);

            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.getMessage();
        }

    }

    /**
     * Get the current information stored about the learner: which declensions they are practicing
     * @param username username of the learner
     * @return result set of declension information
     */
    public ResultSet getDecls(String username) {

        try {
            changeDB("models");
            String sql = "SELECT * FROM declensions WHERE username = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }


    /**
     * Update the declension table
     * @param decls the declensions the user is currently learning
     * @param username the name of the learner
     */
    public void updateDecls(ArrayList<String> decls, String username){
        try {
            changeDB("models");

            for(String s : decls){
                String sql = "UPDATE declensions SET " + s + " = 1 WHERE username = ? COLLATE utf8mb4_bin";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);

                pstmt.executeUpdate();
            }

            //remove the declensions that the user decided to stop practising
            String sql = "SELECT * FROM declensions WHERE username = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet result = pstmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            result.next();
            for(int i = 2; i < 12; i++){
                if(!decls.contains(rsmd.getColumnLabel(i))){
                    String s = "UPDATE declensions SET " + rsmd.getColumnLabel(i) + " = 0 WHERE username = ? COLLATE utf8mb4_bin";;
                    pstmt = conn.prepareStatement(s);
                    pstmt.setString(1, username);
                    pstmt.executeUpdate();
                }
            }



        } catch (SQLException e) {
            e.getMessage();
        }

    }


    /**
     * Update the progress table with the number of correct answers to a specific inflection and declension exercise
     * @param infl the inflection
     * @param num the number of the noun
     * @param decl the declension
     * @param username the username of the learner
     */
    public void updateProgress(String infl, String num, String decl, String username, String level){
        try {

            changeDB("models");

            String column = generateInfl(infl, num);

            String chck = "SELECT count(*) FROM PROGRESS WHERE username = ? AND inflection = ? AND level = ?";
            pstmt = conn.prepareStatement(chck);
            pstmt.setString(1, username);
            pstmt.setString(2, column);
            pstmt.setString(3, level);

            ResultSet check = pstmt.executeQuery();
            check.next();
            if(check.getInt("count(*)") == 0){

                String sql = "INSERT INTO progress (username, inflection, level, total, " + decl + ") VALUES(?, ?, ?, 1, 1)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, column);
                pstmt.setString(3, level);
                pstmt.executeUpdate();
            } else{
                String sql = "UPDATE progress SET total = total + 1, " + decl + " = " + decl + " + 1 WHERE username = ? AND inflection = ?  AND level = ? COLLATE utf8mb4_bin";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, column);
                pstmt.setString(3, level);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.getMessage();
        }

    }

    /**
     * Get the information of how many exercises of a specific inflection/number and declension a person has successfully completed
     * @param infl the inflection
     * @param num the number
     * @param username the username of the learner
     * @return a result set with the numbers
     */
    public ResultSet getProgress(String infl, String num, String username, String level) {

        try {
            changeDB("models");

            String column = generateInfl(infl, num);

            String sql = "SELECT * FROM progress WHERE username = ? AND inflection = ? AND level = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, column);
            pstmt.setString(3, level);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Find the number of sentences there are with nouns in a specific inflection, number and declension
     * @param infl the inflection
     * @param num number
     * @param decl declension
     * @return a result set with the number
     */
    public ResultSet getNumNouns(String infl, String num, String decl) {

        try {
            changeDB("models");

            String sql = "SELECT count(*) FROM nouns WHERE inflection = ? AND number = ? AND pattern = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, infl);
            pstmt.setString(2, num);
            pstmt.setString(3, decl);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Find the number of distinct declensions that nouns in a specific inflection and number appear in
     * @param infl the infection
     * @param num number
     * @return the result set with the number
     */
    public ResultSet getDeclsNum(String infl, String num) {

        try {
            changeDB("text");

            String sql = "SELECT count(DISTINCT pattern) FROM nouns WHERE inflection = ? AND number = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, infl);
            pstmt.setString(2, num);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }


    /**
     * Update the scores database with a correct or an incorrect answer for a specific inflection/declension pair in a specific learning session
     * @param infl the inflection of a noun
     * @param num the number of a noun
     * @param username the username
     * @param time the current session
     * @param decl the declension of a noun
     * @param correct 1 if answer is correct, 0 if incorrect
     * @param incorrect 1 if answer is incorrect, 0 if correct
     */
    public void updateScores(String infl, String num, String username, String time, String decl, int correct, int incorrect){
        try {

            changeDB("models");

            String column = generateInfl(infl, num);

            String chck = "SELECT count(*) FROM SCORES WHERE username = ? AND inflection = ? AND time = ? AND declension = ?";
            pstmt = conn.prepareStatement(chck);
            pstmt.setString(1, username);
            pstmt.setString(2, column);
            pstmt.setString(3, time);
            pstmt.setString(4, decl);

            ResultSet check = pstmt.executeQuery();
            check.next();
            if(check.getInt("count(*)") == 0){

                String sql = "INSERT INTO scores VALUES(?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, time);
                pstmt.setString(3, decl);
                pstmt.setString(4, column);
                pstmt.setInt(5, correct);
                pstmt.setInt(6, incorrect);
                pstmt.executeUpdate();
            } else{
                String sql = "UPDATE scores SET correct = correct + "+ correct + ", incorrect = incorrect + " + incorrect + " WHERE username = ? AND inflection = ?  AND declension = ? AND time = ? COLLATE utf8mb4_bin";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, column);
                pstmt.setString(3, decl);
                pstmt.setString(4, time);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    /**
     * Get the amount of correct  and incorrect answers the user has made overall during the current session
     * @param username username
     * @param time time of the current session
     * @return a resultset that contains the amount of correct and incorrect answers the user has made overall during the current session
     */
    public ResultSet getScoresOverall(String username, String time) {

        try {
            changeDB("models");

            String sql = "SELECT SUM(correct), SUM(incorrect) FROM scores WHERE username = ? AND time = ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, time);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Get a list of every row in a "column" in scores that appeared in every session other than the current
     * @param column either 'inflection' or 'declension'
     * @param username username
     * @param time current session time
     * @return a list of every row in a "column" in scores that appeared in every session other than the current
     */
    public ResultSet getRows(String column, String username, String time) {

        try {
            changeDB("models");

            String sql = "SELECT " + column + " FROM scores WHERE username = ? AND time <> ? COLLATE utf8mb4_bin"; //make sure the current sessions scores are not included
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);
            pstmt.setString(2, time);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Get the sum of correct an incorrect answers when a column had a certain value in every session other than the current
     * @param column either 'inflection' or 'declension'
     * @param username username
     * @param value the value of either 'inflection' or 'declension'
     * @param time the current session time
     * @return the sum of correct an incorrect answers when a column had a certain value in every session other than the current
     */
    public ResultSet getColumnScores(String column, String username, String value, String time) {

        try {
            changeDB("models");

            String sql = "SELECT SUM(correct), SUM(incorrect) FROM scores WHERE username = ? AND "+ column + " = ? AND time <> ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, value);
            pstmt.setString(3, time);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Find all inflections that a declension appeared in in every session other than the current
     * @param declension the declension
     * @param username username
     * @param time the current session time
     * @return a list of all inflections that a declension appeared in in every session other than the current
     */
    public ResultSet getDeclensionInflections(String declension, String username, String time) {

        try {
            changeDB("models");

            String sql = "SELECT inflection FROM scores WHERE username = ? AND declension = ? AND time <> ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, declension);
            pstmt.setString(3, time);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }

    /**
     * Get the sum of correct and incorrect answers a user made during every session other than the current where inflection and declension had specific values
     * @param declension declension of a noun
     * @param inflection inflection of a noun
     * @param username username
     * @param time current session time
     * @return the sum of correct and incorrect answers a user made during every session other than the current where inflection and declension had specific values
     */
    public ResultSet getDeclensionScores(String declension, String inflection, String username, String time) {

        try {
            changeDB("models");

            String sql = "SELECT SUM(correct), SUM(incorrect) FROM scores WHERE username = ? AND declension = ? AND inflection = ? AND time <> ? COLLATE utf8mb4_bin";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, declension);
            pstmt.setString(3, inflection);
            pstmt.setString(4, time);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
        }

        return rs;
    }


    /**
     * Generate a truncated version of an inflection and its number (one string)
     * @param inflection the inflection a word is in
     * @param number the number a word is in
     * @return the truncated one string version
     */
    private String generateInfl(String inflection, String number){
        String result = "";
        if(inflection.equals("nominative")){
            result+= "nominative";
        } else if(inflection.equals("genitive")){
            result+= "genitive";
        } else if(inflection.equals("dative")){
            result+= "dative";
        } else if(inflection.equals("accusative")){
            result+= "accusative";
        } else if(inflection.equals("locative")){
            result+= "locative";
        } else if(inflection.equals("instrumental")){
            result+= "instrumental";
        }

        if(number.equals("singular")){
            result += "Sg";
        } else {
            result += "Pl";
        }

        return result;
    }
}