package me.heartmycity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author Jason Axelson
 * 
 */
public class MyLocationListener implements LocationListener {

  private Context context;

  public MyLocationListener(Context context) {
    this.context = context;
  }

  /** {@inheritDoc} */
  @Override
  public void onLocationChanged(Location loc) {
    System.out.println("location changed");
    loc.getLatitude();

    loc.getLongitude();

    String Text = "My current location is: " +

    "Latitud = " + loc.getLatitude() +

    "Longitud = " + loc.getLongitude();

    Toast.makeText(getApplicationContext(),

    Text,

    Toast.LENGTH_SHORT).show();

  }

  /**
   * @return
   */
  private Context getApplicationContext() {
    return this.context;
  }

  /** {@inheritDoc} */
  @Override
  public void onProviderDisabled(String provider) {
    System.out.println("provider disabled");
    Toast.makeText(getApplicationContext(),

    "Gps Disabled",

    Toast.LENGTH_SHORT).show();
  }

  /** {@inheritDoc} */
  @Override
  public void onProviderEnabled(String provider) {
    System.out.println("provider enabled");
    Toast.makeText(getApplicationContext(),

    "Gps Enabled",

    Toast.LENGTH_SHORT).show();
  }

  /** {@inheritDoc} */
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub
    System.out.println("Status changed");
  }

}
