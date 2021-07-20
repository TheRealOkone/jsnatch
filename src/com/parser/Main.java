package com.parser;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            String query = "амогус";

            URL url = new URL("https://zeapi.yandex.net/lab/api/yalm/text3");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_4) AppleWebKit/605.1.15");
            conn.setRequestProperty("Origin", "https://yandex.ru");
            conn.setRequestProperty("'Referer", "https://yandex.ru/");

            String input = "{ \"query\": \"" + query + "\",\"intro\": 0, \"filter\": 1}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader is =new InputStreamReader(
                    (conn.getInputStream()));
            BufferedReader br = new BufferedReader(is);
            byte[] bytes = IOUtils.toByteArray(is);
            String str = new String(bytes, "UTF-8");
            str = str.substring(str.indexOf("text")+7,str.length()-2).replace("//","/").replace("\\n","");
            while (str.contains("\\u")){
                String boba = str.substring(str.indexOf("\\u"),str.indexOf("\\u")+6);
                str = str.replace(boba,String.valueOf((char)Integer.parseInt((str.substring(str.indexOf("\\u")+2,str.indexOf("\\u")+6)),16)));
            }
            System.out.println(query + " " + str);
            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }


    }
}
