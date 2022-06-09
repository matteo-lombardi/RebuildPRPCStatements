import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class RebuildPRPCStatements {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean close = false;
        boolean debug = false;
        boolean fromFile = false;

        List < String > headers = new ArrayList < String > ();

        try {
            headers = Files.readAllLines(Paths.get("Header.txt"), StandardCharsets.UTF_8);
        } catch (Exception e1) {
            System.out.println("Error while reading header from file: " + e1.toString());
        }

        for (String h: headers) {
            System.out.println(h);
        }

        while (!close) {
            String rebuiltQuery = "";
            String query = "";
            String sqlInserts = "";
            boolean errors = false;
            int i = 0;
            int paramsCount = 0;
            List < String > lines = new ArrayList < String > ();
            String lineNew;

            if (args.length == 0) {
                //USE COMMAND LINE INPUT

                System.out.println("Insert the output from tracer to rebuild the query");
                System.out.println("For example:");
                System.out.println("	...");
                System.out.println("	...");
                System.out.println("	Table Name 	 pegadata.pca_Test \r\n" +
                    "	 SQL Operation 	 select \r\n" +
                    "	 SQL 	 SELECT \"PC0\".\"pyid\" AS \"pyID\" , \"PC0\".\"pystatuswork\" AS \"pyStatusWork\" , ... , ?, ?, ... ?, ?, ....\r\n" +
                    "	 SQL Inserts 	\r\n" +
                    "	 <false> <true> <1> <ABC>\r\n" +
                    "	 Connection ID 	 0 \r\n" +
                    "	 High Level Op ID 	 2959456 \r\n" +
                    "	 High Level Op 	 list ");
                System.out.println("	...");
                System.out.println("	...");
                System.out.println("\nOtherwise:");
                System.out.println("Insert the SQL line, like:");
                System.out.println("	SQL SELECT \"PC0\".\"pyid\" AS \"pyID\" , \"PC0\".\"pystatuswork\" AS \"pyStatusWork\" , ... , ?, ?, ... ?, ?, ....");
                System.out.println("Then, insert the SQL Inserts line, like:");
                System.out.println("	SQL Inserts <false> <true> <1> <ABC> ...");
                System.out.println("\nAt the end, press Enter TWICE:");

                try {

                    while (true) {
                        lineNew = br.readLine().trim();
                        lines.add(lineNew);

                        if (lineNew.equals("")) {
                            break;
                        }
                    }

                } catch (Exception ex) {
                    System.out.println("Error while reading input from command line");
                }
            } else {
                //USE INPUT FROM FILE
                fromFile = true;
                try {
                    lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
                } catch (Exception e1) {
                    System.out.println("Error while reading input from file: " + e1.toString());
                }

            }

            for (int k = 0; k < lines.size() && (sqlInserts.equals("") || query.equals("")); k++) {
                String token = lines.get(k).trim();
                if (debug) System.out.println(k + ":" + token);
                if (debug) System.out.println("START " + k);
                if (sqlInserts.equals("") && token.startsWith("SQL Inserts")) {
                    if (debug) System.out.println("FOUND INSERTS:" + token);
                    sqlInserts = token.replaceAll("SQL Inserts", "").trim();
                    if (debug) System.out.println("INDEX: " + sqlInserts.indexOf("<"));
                    if (sqlInserts.indexOf("<") < 0)
                        sqlInserts = lines.get(k + 1).trim();
                } else {
                    if (query.equals("") && (token.startsWith("SQL ") && !token.startsWith("SQL Operation"))) {
                        if (debug) System.out.println("FOUND QUERY:" + token);
                        query = token.replaceAll("SQL ", "").trim();
                    }
                }

                if (debug) System.out.println("END " + k);

            }

            System.out.println("\nYour query string is:\n" + query);

            for (int j = 0; j < query.length(); j++)
                if (query.charAt(j) == '?') paramsCount++;

            System.out.println("\nYour query string has " + paramsCount + " parameters.");

            String[] queryArray = query.split("\\?");

            if (paramsCount > 0) {

                System.out.println("\nYour SQL Inserts string is:\n" + sqlInserts);

                try {
                    sqlInserts = sqlInserts.substring(sqlInserts.indexOf("<") + 1, sqlInserts.lastIndexOf(">"));
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("\nSQL Inserts has an invalid format! Please retry.");
                    errors = true;
                }

                if (!errors) {
                    String[] sqlInsertsArray = sqlInserts.split("> <");
                    System.out.println("\nParameters found in SQL Inserts: " + sqlInsertsArray.length);
                    if (paramsCount != sqlInsertsArray.length) {
                        System.out.println("\nParameters do not match! Please retry.");
                        errors = true;
                    } else {
                        for (; i < sqlInsertsArray.length; i++)
                            rebuiltQuery += queryArray[i] + "\'" + sqlInsertsArray[i] + "\'";
                    }
                }
            } else
                System.out.println("\n+++ Your query does not have any \"?\" parameters. +++\n");

            if (!errors) {
                for (; i < queryArray.length; i++)
                    rebuiltQuery += queryArray[i];

                System.out.println("\nYour rebuilt SQL string is:\n");
                System.out.println(rebuiltQuery);
            }

            if (fromFile)
                System.out.println("\nPress anything to quit");
            else
                System.out.println("\nContinue with another query? Type \"1\", or press Enter to quit");

            String userInput = "";
            try {
                userInput = br.readLine();
            } catch (IOException e) {
                System.out.println("Error while reading your choice...");
            }

            if (fromFile || !userInput.equals("1"))
                close = true;

        }
        try {
            br.close();
        } catch (IOException e) {
            System.out.println("Error while closing the application...");
        }
        System.out.println("\nBye!");
    }
}