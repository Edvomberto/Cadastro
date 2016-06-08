package support;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by android6017 on 01/06/16.
 */
public class WebClient {
    public String post (String json)  {
        try {
        URL url = new URL("https://www.caelum.com.br/mobile");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-type", "application/json");

        connection.setDoInput(true);
        connection.setDoOutput(true);

        PrintStream saida = new PrintStream((connection.getOutputStream()));

        saida.println(json);


            connection.connect();

            String jsonDeResposta = new Scanner(connection.getInputStream()).next();
            return jsonDeResposta;
        }
        catch (IOException ex)
        {
            return "";
        }




    }
}
