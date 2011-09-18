package me.heartmycity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
  private static final String IMAGE_PATH_KEY = "image-path";

  public static final String DESCRIPTION_KEY = "description";

  private static final long serialVersionUID = 1L;

  private String description = null;
  private Bitmap image = null;

  public static final String PROBLEM_REPORT_KEY = "problemReport";

  public ProblemReport(String description, Bitmap image) {
    this.description = description;
    this.image = image;
  }

  public static ProblemReport fromBundle(Bundle bundle) {
    Utils.checkNull(bundle, "bundle");

    String description = (String) bundle.getCharSequence(DESCRIPTION_KEY);
    String imagePath = (String) bundle.getCharSequence(IMAGE_PATH_KEY);
    Bitmap bMap = BitmapFactory.decodeFile(imagePath);
    System.out.println("got image from dir");
    ProblemReport report = new ProblemReport(description, bMap);

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
}
