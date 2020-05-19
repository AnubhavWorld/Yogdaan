package com.example.yogdaan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity  {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 999;
    private static final int ERROR_DIALOG_REQUEST =1919 ;
    public int a;
    private boolean mPermissionGranted;
    private Button help;
    private Button need;
    private int PERMISSIONS_REQUEST_ENABLE_GPS=1000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGoogleMap();
        isServiceOk();
        checkMapServices();


        need=findViewById(R.id.Need);
        need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission_All=1;
                String[] permissions={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE};
                if(hasPermission(MainActivity.this,permissions)) {
                    startActivity(new Intent(MainActivity.this, NeedActivity.class));
                    a=1;
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,permissions,permission_All);
                }
            }
        });
        help=findViewById(R.id.Help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission_All=1;
                String[] permissions={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
                if(hasPermission(MainActivity.this,permissions)) {
                    startActivity(new Intent(MainActivity.this, HelpActivity.class));
                    a=2;
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,permissions,permission_All);
                }
            }
        });

    }
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
             mPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){


        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests

            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initGoogleMap() {
        if(isServiceOk()) {
            if (mPermissionGranted) {
            }
            else{
                requestLocationPermission();
            }
        }
    }

    private boolean isServiceOk(){
        GoogleApiAvailability googleApiAvailability=GoogleApiAvailability.getInstance();
        int result=googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(result== ConnectionResult.SUCCESS){
            return true;
        }
        return false;
    }

    private void requestLocationPermission(){
        if(!mPermissionGranted){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
            mPermissionGranted=true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    public static boolean hasPermission(Context context,String... permissions){
        for(String permission:permissions){
            if(ActivityCompat.checkSelfPermission(context,permission)!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
