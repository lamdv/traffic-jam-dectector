package com.example.jiji.dataacquisition;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by JiJi on 12/16/2016.
 */
public class UpdateLocationClick implements View.OnClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    TextView coordinates;
    TextView address;
    @Override
    public void onClick(View view) {
        if (ServiceApp.LOCATION != null) {
            double lat = ServiceApp.LOCATION.getLatitude();
            double lon = ServiceApp.LOCATION.getLongitude();
            coordinates.setText(lat + " " + lon);
            Geocoder geocoder = new Geocoder(getApplicationContext(), new Locale("en"));
            try {
                // get address from location
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                if (addresses != null && addresses.size() != 0) {
                    StringBuilder builder = new StringBuilder();
                    Address returnAddress = addresses.get(0);
                    for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                        builder.append(returnAddress.getAddressLine(i));
                        builder.append(" ");
                    }
                    address.setText(builder);
                    address.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "Addresses null");
                }
            } catch (IOException e) {
                Log.e(TAG, "Geocoder exception " + e);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Check GPS status and internet connection", Toast.LENGTH_LONG).show();
            coordinates.setText("No location yet");
            address.setVisibility(View.INVISIBLE);
        }
    }

    public Context getApplicationContext() {
        throw new RuntimeException("Stub!");
    }
}

