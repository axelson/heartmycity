package me.heartmycity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Jason Axelson
 * 
 */
public class ServerUpload {
  protected void tryLogin(String mUsername, String mPassword) {
    HttpURLConnection connection;
    OutputStreamWriter request = null;

    URL url = null;
    String response = null;
    String parameters = "username=" + mUsername + "&password=" + mPassword;

    try {
      url = new URL("your login URL");
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestMethod("POST");

      request = new OutputStreamWriter(connection.getOutputStream());
      request.write(parameters);
      request.flush();
      request.close();
      String line = "";
      InputStreamReader isr = new InputStreamReader(connection.getInputStream());
      BufferedReader reader = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      // Response from server after login process will be stored in response
      // variable.
      response = sb.toString();
      // You can perform UI operations here
      // Toast.makeText(this, "Message from Server: \n" + response, 0).show();
      System.out.println("Message from Server: \n" + response);
      isr.close();
      reader.close();

    }
    catch (IOException e) {
      // Error
    }
  }
}
