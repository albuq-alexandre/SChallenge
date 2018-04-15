package br.iesb.a1631088056.schallenge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

import br.iesb.a1631088056.schallenge.Manifest;
import br.iesb.a1631088056.schallenge.R;
import br.iesb.a1631088056.schallenge.helpers.dummy.DummyContent;
import br.iesb.a1631088056.schallenge.models.Bem;

public class TelaBem extends AppCompatActivity implements OnMapReadyCallback {


    TextView tvNRBem, tVDescricaoBem, tvPBMSBem, tvStatusBem;
    ImageView imgCategoria;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    Button btnVoltar;
    TextView tvLocation;

    private GoogleMap mMap;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bem);

        Intent intent = getIntent();
        DummyContent.DummyItem bem = (DummyContent.DummyItem) intent.getSerializableExtra("Bem");

        tvNRBem.setText(bem.mCodBem);
        tVDescricaoBem.setText(bem.mNomeBem);
        tvPBMSBem.setText(bem.mPBMS);

        if (bem.mStatus) {
            tvStatusBem.setText("Inventariado");
            tvStatusBem.setTextColor(Color.BLACK);
        } else {
            tvStatusBem.setText("Não Inventariado");
            tvStatusBem.setTextColor(Color.RED);
        }

        switch (bem.mCategoryBem) {
            case 1 : {
                imgCategoria.setImageResource(R.drawable.ic_palette);
            }
            case 2 : {
                imgCategoria.setImageResource(R.drawable.ic_devices);
            }
            case 3 : {
                imgCategoria.setImageResource(R.drawable.ic_domain);
            }


        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        SuportMapFragment mapFragment = (SuportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MAP_PERMISSION_ACCESS_COURSE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MAP_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {
            getLastLocation();
            getLocation();
        }

        LatLng iesbSul = new LatLng(-15.7571194, -47.8788442);
        mMap.addMarker(new MarkerOptions().position(iesbSul).title("IESB Sul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iesbSul, 15));
    }


    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(me).title("Inventariado aqui));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 10));
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(me).title("Local atual"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 10));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_ACCESS_COURSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                    getLocation();
                } else {
                    //Permissão negada
                }
                return;
            }
        }
    }

}
