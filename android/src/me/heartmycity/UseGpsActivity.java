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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class UseGpsActivity extends Activity {
  public Button takePictureButton = null;
  public Button reportProblemButton = null;
  private int PICTURE_RESULT = 0;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    this.takePictureButton = (Button) this.findViewById(R.id.take_picture_button);
    this.reportProblemButton = (Button) this.findViewById(R.id.report_problem_button);

    System.out.println("created!");
    ServerUpload serverUpload = new ServerUpload();
    serverUpload.postData(null);

    /* Use the LocationManager class to obtain GPS locations */

    LocationManager mlocManager =

    (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    LocationListener mlocListener = new MyLocationListener(getApplicationContext());

    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

    // LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
    Location loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    String string = loc.toString();
    System.out.println(string);

    Geocoder geocoder = new Geocoder(getApplicationContext());
    String prettyPrintAddress = null;
    try {
      List<Address> fromLocation = geocoder.getFromLocation(loc.getLatitude(),
          loc.getLongitude(), 1);
      System.out.println("location: " + fromLocation.toString());
      prettyPrintAddress = this.prettyPrintAddress(fromLocation.get(0));
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String Text = "My current location is: " + "Latitud = " + loc.getLatitude()
        + "Longitud = " + loc.getLongitude();
    Toast.makeText(getApplicationContext(),

    Text,

    Toast.LENGTH_SHORT).show();

    TextView currentAddress = (TextView) this.findViewById(R.id.address_view);
    currentAddress.setText(prettyPrintAddress);

    this.takePictureButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        System.out.println("take pic button");
        takePicture();
      }
    });

    this.reportProblemButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switchView();
      }
    });
  }

  private String prettyPrintAddress(Address address) {
    String addressLine = address.getAddressLine(0);
    return addressLine;
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
      ImageView imagePicture = (ImageView) this.findViewById(R.id.imageView1);
      imagePicture.setImageBitmap(pic);
    }
  }

  private void switchView() {
    System.out.println("trying to switch view");
    ViewSwitcher viewSwitcher = new ViewSwitcher(this);
//    LayoutInflater inflater = (LayoutInflater) this
//        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    View layout = inflater.inflate(R.layout.report_problem,
//        (ViewGroup) this.findViewById(R.id.report_problem_layout));

    Intent intent = new Intent(this, ReportProblemActivity.class);
    this.startActivity(intent);
  }
}