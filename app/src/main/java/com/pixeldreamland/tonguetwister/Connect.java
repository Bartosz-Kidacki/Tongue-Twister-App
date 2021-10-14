package com.pixeldreamland.tonguetwister;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class Connect {
    static final String WEB_SERVER_URL = "http://ec2-18-218-120-159.us-east-2.compute.amazonaws.com:8080/demo-0.0.1";

    static StringBuilder getDataFromWebServer(URL url) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            urlConnection.disconnect();
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder;
    }
}
