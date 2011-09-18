package me.heartmycity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;

/**
 * Contains all the information needed for a problem report
 * @author Jason Axelson
 * 
 */
public class ProblemReport implements Serializable {
  /**
   * 
   */
  private static final String ANDROID_ID_KEY = "androidId";
  private static final String LOCATION_KEY = "location";
  private static final String IMAGE_PATH_KEY = "image-path";
  public static final String DESCRIPTION_KEY = "description";

  private static final long serialVersionUID = 1L;

  private String description = null;
  private Bitmap image = null;
  private Location loc = null;
  private String androidId = null;

  public static final String PROBLEM_REPORT_KEY = "problemReport";

  public ProblemReport(String description, Bitmap image, Location loc, String androidId) {
    Utils.checkNull(description, "description");
    // Utils.checkNull(image, "image");
    Utils.checkNull(loc, "location");

    this.description = description;
    this.image = image;
    this.loc = loc;
    this.androidId = androidId;
    
  }

  public static ProblemReport fromBundle(Bundle bundle) {
    Utils.checkNull(bundle, "bundle");

    String description = (String) bundle.getCharSequence(DESCRIPTION_KEY);
    String imagePath = (String) bundle.getCharSequence(IMAGE_PATH_KEY);
    Bitmap bMap = BitmapFactory.decodeFile(imagePath);
    Location loc = (Location) bundle.getSerializable(LOCATION_KEY);
    String androidId = (String) bundle.getCharSequence(ANDROID_ID_KEY);
    System.out.println("got image from dir");
    System.out.println("loc is " + loc);
    ProblemReport report = new ProblemReport(description, bMap, loc, androidId);

    return report;
  }

  public static void packageIntent(Intent intent, ProblemReport report, Context context) {
    Utils.checkNull(intent, "intent");
    Utils.checkNull(report, "report");

    Bitmap image = report.getImage();
    System.out.println("image is : " + image);

    FileOutputStream os = null;
    String dirName = "/mydirectory/";

    String fileName = "image.jpg";
    String filePath = null;
    try {
      if (android.os.Environment.getExternalStorageState().equals(
          android.os.Environment.MEDIA_MOUNTED)) {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + dirName);
        dir.mkdirs();

        String root = Environment.getExternalStorageDirectory().toString();
        System.out.println("root is " + root);
        new File(root + "/mvc/mvc").mkdirs();

        // File file = new File(this.getExternalFilesDir(null),
        // this.dirName+fileName); //this function give null pointer exception
        // so im using other one
        File file = new File(dir, fileName);
        filePath = dir.getAbsolutePath() + "/" + fileName;
        System.out.println("file path is " + filePath);
        os = new FileOutputStream(file);
      }
      else {
        os = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      }
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println(e);
    }

    System.out.println("os is: " + os);
    System.out.println("image is: " + image);
    if (image == null) {
      // TODO: Handle null data
    }
    image.compress(CompressFormat.PNG, 100, os);
    try {
      os.flush();
      os.close();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println(e);
    }

    // image.writeToParcel(parcel, 0);
    // bitmap.writeToParcel(null, BIND_AUTO_CREATE)
    Bundle bundle = new Bundle();
    // bundle.putSerializable(PROBLEM_REPORT_KEY, report);
    bundle.putCharSequence(DESCRIPTION_KEY, report.getDescription());
    bundle.putCharSequence(IMAGE_PATH_KEY, filePath);
    bundle.putSerializable(LOCATION_KEY, (Serializable) report.getLoc());
    bundle.putCharSequence(ANDROID_ID_KEY, report.getAndroidId());
    // intent.putExtra("image-path", filePath);
    intent.putExtra(PROBLEM_REPORT_KEY, bundle);
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the loc
   */
  public Location getLoc() {
    return loc;
  }

  /**
   * @param loc the loc to set
   */
  public void setLoc(Location loc) {
    this.loc = loc;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the image
   */
  public Bitmap getImage() {
    return image;
  }

  /**
   * @param image the image to set
   */
  public void setImage(Bitmap image) {
    this.image = image;
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    try {
      json.accumulate("description", this.getDescription());
      // TODO: Fix lat and long and phone id
      json.accumulate("lat", this.getLoc().getLatitude());
      json.accumulate("long", this.getLoc().getLongitude());
//      json.accumulate("phone_id", "123josidf23ZZ");
      json.accumulate("phone_id", this.getAndroidId());

      System.out.println(json.toString(4));
    }
    catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return json;
  }

  /**
   * @return the androidId
   */
  public String getAndroidId() {
    return androidId;
  }

  /**
   * @param androidId the androidId to set
   */
  public void setAndroidId(String androidId) {
    this.androidId = androidId;
  }
}
