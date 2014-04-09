package jira;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Client {
    private String host;
    private String userName;
    private String password;

    public Client(String host, String userName, String password) {
        this.host = host;
        this.userName = userName;
        this.password = password;
    }

    public void callMethod(String method, String properties) {
        try {
            URL targetUrl = new URL(this.buildURL(method) + "?" + properties);

            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("GET");

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + httpConnection.getResponseCode());
            }

            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                    (httpConnection.getInputStream())));

            String output;
            System.out.println("Output from Server:\n");
            while ((output = responseBuffer.readLine()) != null) {
                System.out.println(output);
            }

            httpConnection.disconnect();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildURL (String method) { return this.buildURL(method, "2"); }
    private String buildURL (String method, String apiVersion) {
        String url = "https://";
        if (this.userName != null) {
            url += userName;
            if (this.password != null) {
                url += ":" + this.password;
            }
            url += "@";
        }
        url += this.host + "/rest/api/" + apiVersion + "/" + method;
        return url;
    }
}
