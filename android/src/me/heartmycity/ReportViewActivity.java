package me.heartmycity;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
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
  private ImageView viewOnMapButton = null;
  
  private Location loc = null;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.report_view);

    Intent intent = this.getIntent();
    ProblemReport problemReport = ProblemReport.fromBundle(intent
        .getBundleExtra(ProblemReport.PROBLEM_REPORT_KEY));

    this.image = (ImageView) this.findViewById(R.id.report_view_image);
    this.descriptionField = (TextView) this.findViewById(R.id.report_view_description);
    this.viewOnMapButton = (ImageView) this.findViewById(R.id.view_on_map_button);

    // FIXME: Hack because we can't serialize bitmaps
    ImageView reportImage = (ImageView) this.findViewById(R.id.report_image);
    Bitmap bitmap = problemReport.getImage();
    // bitmap.writeToParcel(null, BIND_AUTO_CREATE)
    this.image.setImageBitmap(bitmap);
    this.descriptionField.setText(problemReport.getDescription());

    ServerUpload serverUpload = new ServerUpload();
    // serverUpload.tryUpload(problemReport);
    System.out.println("upload to imgur");
    String imgurLink = serverUpload.uploadImgur(problemReport);
    serverUpload.postData(problemReport, imgurLink);
    
    this.loc = problemReport.getLoc();

    this.viewOnMapButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        System.out.println("switching to map");
        viewMap();
      }
    });

  }

  private void viewMap() {
//    String mapLink = "https://maps.googleapis.com/maps/api/staticmap?parameters";
//    String mapLink = "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12";
//    String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&sensor=false";
    String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=";
    String coords = loc.getLatitude() + "," + loc.getLongitude();
//    40.714728,-73.998672
    String zoom = "&zoom=12&size=400x400&sensor=false";
    
    String url = mapUrl + coords + zoom;
    // Intent intent = new Intent(this, MapViewActivity.class);
    Uri uri = Uri.parse(url);
    startActivity(new Intent(Intent.ACTION_VIEW, uri));
  }
}
