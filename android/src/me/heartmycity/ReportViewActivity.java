package me.heartmycity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    this.image.setImageBitmap(problemReport.getImage());
    this.descriptionField.setText(problemReport.getDescription());
  }
}
