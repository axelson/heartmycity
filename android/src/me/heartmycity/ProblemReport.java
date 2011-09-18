package me.heartmycity;

import java.io.Serializable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Contains all the information needed for a problem report
 * @author Jason Axelson
 * 
 */
public class ProblemReport implements Serializable {
  private static final long serialVersionUID = 1L;

  private String description = null;
//  private Bitmap image = null;
  /**
   * 
   */
  public static final String PROBLEM_REPORT_KEY = "problemReport";

  public ProblemReport(String description, Bitmap image) {
    this.description = description;
//    this.image = image;
  }

  public static ProblemReport fromBundle(Bundle bundle) {
    Utils.checkNull(bundle, "bundle");

    Object object = bundle.get(PROBLEM_REPORT_KEY);
    Utils.checkNull(object, "object");
    ProblemReport report = (ProblemReport) bundle.getSerializable(PROBLEM_REPORT_KEY);
    return report;
  }

  public static void packageIntent(Intent intent, ProblemReport report) {
    Utils.checkNull(intent, "intent");
    Utils.checkNull(report, "report");

    Bundle bundle = new Bundle();
    bundle.putSerializable(PROBLEM_REPORT_KEY, report);
    System.out.println("report is " + report);
    System.out.println("intent is " + intent);
    intent.putExtra(PROBLEM_REPORT_KEY, bundle);
//    Utils.checkNull(extras, "extras");
//    extras.putSerializable(PROBLEM_REPORT_KEY, report.getDescription());
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
//    return image;
    return null;
  }

  /**
   * @param image the image to set
   */
  public void setImage(Bitmap image) {
//    this.image = image;
  }
}
