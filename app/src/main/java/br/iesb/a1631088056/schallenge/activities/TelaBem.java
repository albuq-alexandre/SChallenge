package br.iesb.a1631088056.schallenge.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import br.iesb.a1631088056.schallenge.Manifest;
import br.iesb.a1631088056.schallenge.R;
import br.iesb.a1631088056.schallenge.helpers.AlarmeUtil;
import br.iesb.a1631088056.schallenge.helpers.Receiver;
import br.iesb.a1631088056.schallenge.helpers.dummy.DummyContent;

public class TelaBem extends AppCompatActivity implements OnMapReadyCallback {


    TextView tvNRBem, tVDescricaoBem, tvPBMSBem, tvStatusBem;
    ImageView imgCategoria;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;
    Button btnVoltar, btnAgendar;
    TextView tvLocation;

    private GoogleMap mMap;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bem);

        Intent intent = getIntent();
        DummyContent.DummyItem bem = (DummyContent.DummyItem) intent.getSerializableExtra("Bem");

        tvNRBem =  (TextView) findViewById(R.id.tVNRBem);
        tVDescricaoBem = (TextView) findViewById(R.id.tVDescricaoBem);
        tvPBMSBem = (TextView) findViewById(R.id.tvBemPBMS);
        tvStatusBem = (TextView) findViewById(R.id.tvStatusBem);
        imgCategoria = (ImageView) findViewById(R.id.imgCategoria);
        //mMap = (GoogleMap) findViewById(R.id.myMap);

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
            case 3 : {
                imgCategoria.setImageResource(R.drawable.ic_domain);
                break;
            }
            case 2 : {
                imgCategoria.setImageResource(R.drawable.ic_palette);
                break;
            }
            case 1 : {
                imgCategoria.setImageResource(R.drawable.ic_devices);
                break;
            }


        }

        btnVoltar = findViewById(R.id.btnBemVoltar);
        btnAgendar = findViewById(R.id.btnBemAgendar);


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Receiver.ACTION);
                intent.putExtra("Bem", bem);
                // Agenda para daqui a 5 seg
                AlarmeUtil.schedule(TelaBem.this, intent, getTime());
                //sendBroadcast(intent);
                Toast.makeText(TelaBem.this,"Alarme agendado.",Toast.LENGTH_SHORT).show();



            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
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
            if (! (lastKnownLocation==null)) {
                LatLng me = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(me).title("Inventariado aqui"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 10));
            }
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



    // Data/Tempo para agendar o alarme
    public long getTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 5);
        long time = c.getTimeInMillis();
        return time;
    }
}
