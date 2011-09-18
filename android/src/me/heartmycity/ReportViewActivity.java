package me.heartmycity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Jason Axelson
 * 
 */
public class ReportViewActivity extends Activity {
  private ImageView image = null;
  private TextView descriptionField = null;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.report_view);

    Intent intent = this.getIntent();
    ProblemReport problemReport = ProblemReport.fromBundle(intent
        .getBundleExtra(ProblemReport.PROBLEM_REPORT_KEY));

    this.image = (ImageView) this.findViewById(R.id.report_view_image);
    this.descriptionField = (TextView) this.findViewById(R.id.report_view_description);

    // FIXME: Hack because we can't serialize bitmaps
    ImageView reportImage = (ImageView) this.findViewById(R.id.report_image);
    Bitmap bitmap = problemReport.getImage();
//  bitmap.writeToParcel(null, BIND_AUTO_CREATE)
    this.image.setImageBitmap(bitmap);
    this.descriptionField.setText(problemReport.getDescription());
    
    ServerUpload serverUpload = new ServerUpload();
//    serverUpload.tryUpload(problemReport);
    serverUpload.postData(problemReport);
  }
}
