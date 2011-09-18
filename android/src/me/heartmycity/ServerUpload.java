package me.heartmycity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
  // private static final String SERVER_URL =
  // "http://posttestserver.com/post.php?dump";

  private static final String SERVER_URL = "http://ec2-107-20-189-184.compute-1.amazonaws.com/json/problems/";

  public void postData(ProblemReport report) {
    // Create a new HttpClient and Post Header
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(SERVER_URL);

    try {
      // Add your data
      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
      nameValuePairs.add(new BasicNameValuePair("description", report.getDescription()));

      String latString = new String();
      latString = new Double(report.getLoc().getLatitude()).toString();
      String longString = new String(new Double(report.getLoc().getLongitude()).toString());

      nameValuePairs.add(new BasicNameValuePair("lat", latString));
      nameValuePairs.add(new BasicNameValuePair("long", longString));
      nameValuePairs.add(new BasicNameValuePair("phone_id", report.getAndroidId()));
      // nameValuePairs.add(new BasicNameValuePair("image", "AndDev is Cool!"));
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
