package me.heartmycity;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class UseGpsActivity extends Activity {
  public ImageView reportProblemButton = null;
  public ImageView viewProblemsButton = null;
  public ImageView visitWebsiteButton = null;
  private int PICTURE_RESULT = 0;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    this.reportProblemButton = (ImageView) this.findViewById(R.id.report_problem_button);
    this.viewProblemsButton = (ImageView) this.findViewById(R.id.view_problems_button);
    this.visitWebsiteButton = (ImageView) this.findViewById(R.id.visit_website_button);

    System.out.println("created!");

    /* Use the LocationManager class to obtain GPS locations */

    LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    LocationListener mlocListener = new MyLocationListener(getApplicationContext());

    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

    // LocationProvider locationProvider = LocationManager.NETWORK_PROVIDER;
    Location loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    String string = loc.toString();
    System.out.println(string);
    ServerUpload serverUpload = new ServerUpload();
    String android_id = Secure.getString(getApplicationContext().getContentResolver(),
        Secure.ANDROID_ID);
    // ProblemReport report = new ProblemReport("des", null, loc, android_id);
    // ProblemReport.packageIntent(new Intent(), report,
    // this.getApplicationContext());
    // serverUpload.postData(report);

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

    String Text = "My current location is: " + "Latitud = " + loc.getLatitude()
        + "Longitud = " + loc.getLongitude();
    Toast.makeText(getApplicationContext(),

    Text,

    Toast.LENGTH_SHORT).show();

    TextView currentAddress = (TextView) this.findViewById(R.id.address_view);
    currentAddress.setText("Address: " + prettyPrintAddress);

    this.reportProblemButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switchView();
      }
    });

    this.visitWebsiteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
         System.out.println("Trying to visit website");
         Uri uri = Uri.parse("http://heartmycity.me");
         startActivity(new Intent(Intent.ACTION_VIEW, uri));
//        System.out.println("Trying to visit website");
//        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&sensor=false";
//        Uri uri = Uri.parse(mapUrl);
//        startActivity(new Intent(Intent.ACTION_VIEW, uri));
      }
    });
  }

  public static String prettyPrintAddress(Address address) {
    String addressLine = address.getAddressLine(0);
    return addressLine;
  }

  private void switchView() {
    System.out.println("trying to switch view");
    ViewSwitcher viewSwitcher = new ViewSwitcher(this);
    // LayoutInflater inflater = (LayoutInflater) this
    // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    // View layout = inflater.inflate(R.layout.report_problem,
    // (ViewGroup) this.findViewById(R.id.report_problem_layout));

    Intent intent = new Intent(this, ReportProblemActivity.class);
    this.startActivity(intent);
  }
}