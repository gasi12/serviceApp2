package com.example.serviceApp.security.twilio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwilioService {
    private static final String ACCOUNT_SID = "";
    private static final String AUTH_TOKEN = "";
    private static final String SERVICE_SID = "";

    public void sendToken(String phoneNumber) throws IOException, InterruptedException {
        String url = "https://verify.twilio.com/v2/Services/" + SERVICE_SID + "/Verifications";

        String encodedPhoneNumber = URLEncoder.encode(phoneValidator(phoneNumber), StandardCharsets.UTF_8.toString());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString("To=" + encodedPhoneNumber + "&Channel=sms"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Token sent to {}. Status: {}", phoneNumber, response.statusCode());
    }

    public Boolean validateToken(String phoneNumber, String token) throws IOException, InterruptedException {
        String url = "https://verify.twilio.com/v2/Services/" + SERVICE_SID + "/VerificationCheck";

        String encodedPhoneNumber = URLEncoder.encode(phoneValidator(phoneNumber), StandardCharsets.UTF_8);
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString("To=" + encodedPhoneNumber + "&Code=" + encodedToken))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info(response.body());
        return response.statusCode() == 200;
    }



    private String phoneValidator(String phoneNumber){
        phoneNumber = "+48"+phoneNumber;
        String e164regex= "^\\+[1-9]\\d{1,14}$";
        Pattern p = Pattern.compile(e164regex);
        Matcher m = p.matcher(phoneNumber);
        if(m.find()){
            return phoneNumber;
        }
        else {
            throw new IllegalArgumentException("invalid phone number");


        }

    }
}
