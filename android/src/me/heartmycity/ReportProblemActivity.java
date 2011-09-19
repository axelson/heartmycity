package me.heartmycity;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jason Axelson
 * 
 */
public class ReportProblemActivity extends Activity {
  private static final int PICTURE_RESULT = 0;
  ImageView image = null;
  EditText reportDescription = null;
  TextView addressText = null;
  Button sendReportButton = null;
  private Bitmap bitmap;
  private Location location = null;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.report_problem);

    this.image = (ImageView) this.findViewById(R.id.report_image);
    this.reportDescription = (EditText) this.findViewById(R.id.report_description_field);
    this.sendReportButton = (Button) this.findViewById(R.id.send_report_button);
    this.addressText = (TextView) this.findViewById(R.id.address_view_text);

    this.location = this.getLocation();
    
    this.addressText.setText("Address: " + this.locationToAddress(location));

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

    // TODO: Get the real location
    LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    LocationListener mlocListener = new MyLocationListener(getApplicationContext());

    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

    // LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
    Location loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    String description = this.reportDescription.getText().toString();
    Context context = getApplicationContext();
    String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    ProblemReport problemReport = new ProblemReport(description, this.bitmap, loc, android_id);

    Intent intent = new Intent(this, ReportViewActivity.class);
    System.out.println("pic " + this.bitmap);
    System.out.println("image is: " + problemReport.getImage());
    ProblemReport.packageIntent(intent, problemReport, this);
    this.startActivity(intent);
  }

  private Location getLocation() {
    LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    LocationListener mlocListener = new MyLocationListener(getApplicationContext());

    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

    // LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
    Location loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    String Text = "My current location is: " + "Latitud = " + loc.getLatitude()
        + "Longitud = " + loc.getLongitude();
    Toast.makeText(getApplicationContext(),

    Text,

    Toast.LENGTH_SHORT).show();

    return loc;
  }
  
  public String locationToAddress(Location loc) {
    Geocoder geocoder = new Geocoder(getApplicationContext());
    String prettyPrintAddress = null;
    try {
      List<Address> fromLocation = geocoder.getFromLocation(loc.getLatitude(),
          loc.getLongitude(), 1);
      System.out.println("location: " + fromLocation.toString());
      prettyPrintAddress = UseGpsActivity.prettyPrintAddress(fromLocation.get(0));
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return prettyPrintAddress;
  }
}
