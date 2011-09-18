package me.heartmycity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Jason Axelson
 * 
 */
public class ReportProblemActivity extends Activity {
  private static final int PICTURE_RESULT = 0;
  ImageView image = null;
  EditText reportDescription = null;
  Button sendReportButton = null;
  private Bitmap bitmap;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.report_problem);

    this.image = (ImageView) this.findViewById(R.id.report_image);
    this.reportDescription = (EditText) this.findViewById(R.id.report_description_field);
    this.sendReportButton = (Button) this.findViewById(R.id.send_report_button);

    this.image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        System.out.println("take picture on report screen");
        takePicture();
      }
    });

    this.sendReportButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        uploadPicture();
      }
    });
  }

  private void takePicture() {
    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    this.startActivityForResult(camera, PICTURE_RESULT);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    System.out.println("activity result");
    if (requestCode == PICTURE_RESULT) {
      System.out.println("picture result");
      if (resultCode == Activity.RESULT_OK) {
        System.out.println("picture ookay");
        takePicture2(data);
      }
      else if (resultCode == Activity.RESULT_CANCELED) {

      }
    }
  }

  protected void takePicture2(Intent data) {
    Bundle b = data.getExtras();
    Bitmap pic = (Bitmap) b.get("data");
    if (pic != null) {
      // ImageView imagePicture = (ImageView)
      // this.findViewById(R.id.report_image);
      this.image.setImageBitmap(pic);
      this.bitmap = pic;
    }
  }

  private void uploadPicture() {
    // TODO: Actually upload picture
    String text = "Uploaded picture!";
    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

    String description = this.reportDescription.getText().toString();
    ProblemReport problemReport = new ProblemReport(description, this.bitmap);

    Intent intent = new Intent(this, ReportViewActivity.class);
    ProblemReport.packageIntent(intent, problemReport);
    this.startActivity(intent);
  }
}
