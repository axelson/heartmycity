package me.heartmycity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

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
      
//      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      SimpleMultipartEntity entity = new SimpleMultipartEntity();

      
      File imageFile = new File(report.getImagePath());
      FileInputStream fileInputStream = new FileInputStream(imageFile);
      FileEntity fileEntity = new FileEntity(imageFile, "image/jpeg");
//      entity.addPart("image", report.getImagePath(), fileInputStream, "Content-Type: image/jpeg");

      entity.addPart("description", "mpe description");
      entity.addPart("phone_id", "phonid");

      Bitmap bitmap = report.getImage();
      Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 640, 480, true); 
      
      MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();  
      
      // CompressFormat set up to JPG, you can change to PNG or whatever you want;  
      bmpCompressed.compress(CompressFormat.JPEG, 100, bos);  
      byte[] data = bos.toByteArray(); 
      multipartEntity.addPart("image", new ByteArrayBody(data, "image/jpeg", "temp.jpg"));
//      bitmap = BitmapFactory.decodeFile(exsistingFileName);  

      multipartEntity.addPart("description", new StringBody("full multi description")); 
      multipartEntity.addPart("phone_id", new StringBody("phonemulti description")); 
      
      httppost.setEntity(multipartEntity);

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
