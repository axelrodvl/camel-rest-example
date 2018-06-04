package co.axelrod.camel.rest.route.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 04.06.2018.
 */
public class HttpUtils {
    private HttpUtils() {
        // Utility class
    }

    public static String get(URL url) throws IOException {
        StringBuilder response = new StringBuilder();

        BufferedInputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String nextLine = reader.readLine();
            while (nextLine != null) {
                response.append(nextLine);
                nextLine = reader.readLine();
            }
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            if(connection != null) {
                connection.disconnect();
            }
        }

        return response.toString();
    }

    public static String post(URL url, String contentType, String requestBody) throws IOException {
        StringBuilder response = new StringBuilder();

        BufferedOutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoOutput(true);

            outputStream = new BufferedOutputStream(connection.getOutputStream());
            outputStream.write(requestBody.getBytes());
            outputStream.flush();

            inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String nextLine = reader.readLine();
            while (nextLine != null) {
                response.append(nextLine);
                nextLine = reader.readLine();
            }
        } finally {
            if(outputStream != null) {
                outputStream.close();
            }
            if(inputStream != null) {
                inputStream.close();
            }
            if(connection != null) {
                connection.disconnect();
            }
        }

        return response.toString();
    }
}
