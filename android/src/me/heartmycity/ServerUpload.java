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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  public void postData(ProblemReport report, String imgurLink) {
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
      System.out.println("uploading image url: " + imgurLink);
      nameValuePairs.add(new BasicNameValuePair("image_url", imgurLink));
      // nameValuePairs.add(new BasicNameValuePair("image", "AndDev is Cool!"));

      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      SimpleMultipartEntity entity = new SimpleMultipartEntity();

      File imageFile = new File(report.getImagePath());
      FileInputStream fileInputStream = new FileInputStream(imageFile);
      FileEntity fileEntity = new FileEntity(imageFile, "image/jpeg");
      // entity.addPart("image", report.getImagePath(), fileInputStream,
      // "Content-Type: image/jpeg");

      entity.addPart("description", "mpe description");
      entity.addPart("phone_id", "phonid");

      Bitmap bitmap = report.getImage();
//      Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 640, 480, true);
      Bitmap bmpCompressed = bitmap;

      MultipartEntity multipartEntity = new MultipartEntity(
          HttpMultipartMode.BROWSER_COMPATIBLE);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      // CompressFormat set up to JPG, you can change to PNG or whatever you
      // want;
      bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
      byte[] data = bos.toByteArray();
      multipartEntity.addPart("image", new ByteArrayBody(data, "image/jpeg", "temp.jpg"));
      // bitmap = BitmapFactory.decodeFile(exsistingFileName);

      multipartEntity.addPart("description", new StringBody("full multi description"));
      multipartEntity.addPart("phone_id", new StringBody("phonemulti description"));

//      httppost.setEntity(multipartEntity);

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

  public String uploadImgur(ProblemReport report) {
    // Create a new HttpClient and Post Header
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://api.imgur.com/2/upload");

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

      // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      SimpleMultipartEntity entity = new SimpleMultipartEntity();

      File imageFile = new File(report.getImagePath());
      FileInputStream fileInputStream = new FileInputStream(imageFile);
      FileEntity fileEntity = new FileEntity(imageFile, "image/jpeg");
      // entity.addPart("image", report.getImagePath(), fileInputStream,
      // "Content-Type: image/jpeg");

      entity.addPart("description", "mpe description");
      entity.addPart("phone_id", "phonid");

      Bitmap bitmap = report.getImage();
//      Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 640, 480, true);
      Bitmap bmpCompressed = bitmap;

      MultipartEntity multipartEntity = new MultipartEntity(
          HttpMultipartMode.BROWSER_COMPATIBLE);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      // CompressFormat set up to JPG, you can change to PNG or whatever you
      // want;
      bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
      byte[] data = bos.toByteArray();
      multipartEntity.addPart("image", new ByteArrayBody(data, "image/jpeg", "temp.jpg"));
      // bitmap = BitmapFactory.decodeFile(exsistingFileName);

      multipartEntity.addPart("key", new StringBody("e619c693117fef9d08bca5a23b3eba36"));

      httppost.setEntity(multipartEntity);

      // Execute HTTP Post Request
      HttpResponse response = httpclient.execute(httppost);
      InputStream content = response.getEntity().getContent();
      StringBuilder inputStreamToString = this.inputStreamToString(content);
      System.out.println(inputStreamToString.toString());
      String string = inputStreamToString.toString();

      String imgurUrl = ServerUpload.parseString(string);

      System.out.println(response.toString());
      System.out.println(response.getAllHeaders());
      System.out.println(response.getStatusLine());
      return imgurUrl;

    }
    catch (ClientProtocolException e) {
      System.out.println("exception");
      System.out.println("exception: " + e.getStackTrace());
      // TODO Auto-generated catch block
    }
    catch (IOException e) {
      System.out.println("exception");
      System.out.println("exception: " + e.getStackTrace());
      // TODO Auto-generated catch block
    }
    System.out.println("Got bad imgur address!");
    return "http://i.imgur.com/koo98.jpg";
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

  public static String parseString(String toParse) {
    System.out.println("parsing");
//    toParse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><upload><image><name/><title/><caption/><hash>koo98</hash><deletehash>YNC29U4k1s3M3Lg</deletehash><datetime>2011-09-19 01:38:22</datetime><type>image/jpeg</type><animated>false</animated><width>640</width><height>480</height><size>167358</size><views>0</views><bandwidth>0</bandwidth></image><links><original>http://i.imgur.com/koo98.jpg</original><imgur_page>http://imgur.com/koo98</imgur_page><delete_page>http://imgur.com/delete/YNC29U4k1s3M3Lg</delete_page><small_square>http://i.imgur.com/koo98s.jpg</small_square><large_thumbnail>http://i.imgur.com/koo98l.jpg</large_thumbnail></links></upload>";

    Pattern pattern = Pattern.compile("http://(.*?)\\.jpg");
    Matcher m = pattern.matcher(toParse);
//    while (m.find()) {
//      System.out.println("found match");
//      String s = m.group(1);
//      System.out.println("s is "+ s);
//       s now contains "BAR"
//    }
    if (m.find()) {
      System.out.println("found match");
      String s = m.group(1);
      System.out.println("s is "+ s);
      // s now contains "BAR"
      return "http://"+ s + ".jpg";
    }
    else {
      System.out.println("Warning! Didn't find match");
      return "http://i.imgur.com/koo98.jpg";
    }
  }
}
