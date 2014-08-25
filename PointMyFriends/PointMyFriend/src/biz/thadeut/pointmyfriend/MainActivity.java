package biz.thadeut.pointmyfriend;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener, SensorEventListener {
  private TextView latituteField;
  private TextView longitudeField;
  private LocationManager locationManager;
  private String provider;

  // define the display assembly compass picture
  private ImageView image;
  	 
  // record the compass picture angle turned
  private float currentDegree = 0f;
  
  // device sensor manager
  SensorManager mSensorManager;
  	 
  TextView tvHeading;
  
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    latituteField = (TextView) findViewById(R.id.TextView02);
    longitudeField = (TextView) findViewById(R.id.TextView04);
    image = (ImageView) findViewById(R.id.imageView1);
    
    // TextView that will tell the user what degree is he heading
    tvHeading = (TextView) findViewById(R.id.textView1);
    	 
	// initialize your android device sensor capabilities
    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    

    // Get the location manager
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Define the criteria how to select the location provider -> use
    // default
    Criteria criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    Location location = locationManager.getLastKnownLocation(provider);

    // Initialize the location fields
    if (location != null) {
      System.out.println("Provider " + provider + " has been selected.");
      onLocationChanged(location);
    } else {
      latituteField.setText("Location not available");
      longitudeField.setText("Location not available");
    }
  }

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(provider, 400, 1, this);
  }

  /* Remove the locationlistener updates when Activity is paused */
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
    }

  @Override
  public void onLocationChanged(Location location) {
    float lat = (float) (location.getLatitude());
    float lng = (float) (location.getLongitude());
    latituteField.setText(String.valueOf(lat));
    longitudeField.setText(String.valueOf(lng));
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onProviderEnabled(String provider) {
    Toast.makeText(this, "Enabled new provider " + provider,
        Toast.LENGTH_SHORT).show();

  }

  @Override
  public void onProviderDisabled(String provider) {
    Toast.makeText(this, "Disabled provider " + provider,
        Toast.LENGTH_SHORT).show();
  }
  
  public void onSensorChanged(SensorEvent event) {
 	 
 	        // get the angle around the z-axis rotated
 	        float degree = Math.round(event.values[0]);
 	 
 	        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
 	 
 	        // create a rotation animation (reverse turn degree degrees)
 	        RotateAnimation ra = new RotateAnimation(
 	                currentDegree,
 	                -degree,
 	                Animation.RELATIVE_TO_SELF, 0.5f,
 	                Animation.RELATIVE_TO_SELF,
 	                0.5f);
 	 
 	        // how long the animation will take place
 	        ra.setDuration(210);
 	 
 	        // set the animation after the end of the reservation status
 	        ra.setFillAfter(true);
 	 
 	        // Start the animation
 	        image.startAnimation(ra);
 	        currentDegree = -degree;
 	 
 	    }
 	 
 	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
 	        // not in use
 	    }
 	}
