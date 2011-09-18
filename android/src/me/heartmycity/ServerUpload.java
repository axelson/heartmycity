package me.heartmycity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author Jason Axelson
 * 
 */
public class ServerUpload {
  /**
   * 
   */
  private static final String SERVER_URL = "http://posttestserver.com/post.php?dump";

  protected void tryUpload(ProblemReport report) {
    System.out.println("uploading to server!");
    HttpURLConnection connection;
    OutputStreamWriter request = null;

    URL url = null;
    String response = null;
    String mUsername = null;
    String mPassword = null;
    // String parameters = "username=" + mUsername + "&password=" + mPassword;
    String parameters = "description" + report.getDescription() + "bob";

    try {
      url = new URL(SERVER_URL);
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
      System.out.println("erorr: " + e);
      // Error
    }
  }

  public void postData(ProblemReport report) {
    // Create a new HttpClient and Post Header
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(SERVER_URL);

    try {
      // Add your data
      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
      nameValuePairs.add(new BasicNameValuePair("id", "12345"));
      nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      // Execute HTTP Post Request
      HttpResponse response = httpclient.execute(httppost);
      InputStream content = response.getEntity().getContent();
      StringBuilder inputStreamToString = this.inputStreamToString(content);
      System.out.println(inputStreamToString.toString());
      System.out.println(response.toString());
      System.out.println(response.getAllHeaders());
      System.out.println(response.getStatusLine());

    }
    catch (ClientProtocolException e) {
      System.out.println("exception: " + e.getStackTrace());
      // TODO Auto-generated catch block
    }
    catch (IOException e) {
      System.out.println("exception: " + e.getStackTrace());
      // TODO Auto-generated catch block
    }
  }

  // Fast Implementation
  private StringBuilder inputStreamToString(InputStream is) {
    String line = "";
    StringBuilder total = new StringBuilder();

    // Wrap a BufferedReader around the InputStream
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

    // Read response until the end
    try {
      while ((line = rd.readLine()) != null) {
        total.append(line);
      }
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("e " + e.getStackTrace());
    }

    // Return full string
    return total;
  }
}
