package models;

import com.google.gson.Gson;
import okhttp3.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import models.LocationInfo;


public class SmsSender {
    private static String Code;

    public static String getCode() {
        return Code;
    }

    public static void setCode(String code) {
        Code = code;
    }
    public static LocationInfo getLocalisation(){
        LocationInfo locationInfo = null;
        try {
            URL ipifyUrl = new URL("https://api.ipify.org");
            try {
                HttpURLConnection connection = (HttpURLConnection) ipifyUrl.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String ipAddress = response.toString();
                URL url = new URL("http://ip-api.com/json/" + ipAddress);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                inputLine = "";
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                locationInfo = gson.fromJson(response.toString(), LocationInfo.class);
                return locationInfo;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        return locationInfo;
    }

    public String SmsPwd(String num)throws IOException {
        Random random = new Random();
            int randomNumber = random.nextInt(9000) + 1000;
            String Code = String.valueOf(randomNumber);
        LocationInfo locationInfo = getLocalisation();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"destinations\":[{\"to\":\"216"+num+"\"}],\"from\":\"Al3abGames\",\"text\":\"Change Password Request,\\n Verify Your Location :"+locationInfo.getRegionName()+", "+locationInfo.getCity()+","+locationInfo.getCountry()+" \\nHere is Your Code To change Your Password:"+Code+"\"}]}");
        Request request = new Request.Builder()
                .url("https://3g33gw.api.infobip.com/sms/2/text/advanced")
                .method("POST", body)
                .addHeader("Authorization", "App 9eb95e4651d4fce98fde6543b502e4f4-ec6d7bfd-32d2-45ec-9161-c87e40b7f19a")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Code;
    }
}


