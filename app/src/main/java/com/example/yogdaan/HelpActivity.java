package com.example.yogdaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class HelpActivity extends AppCompatActivity {
    private Button helpatother;
    private Button helpatthis;
    private double lat;
    private double lng;
    private TextView hrs;
    private EditText name;
    private EditText number;
    private FirebaseFirestore ff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ff = FirebaseFirestore.getInstance();
        name = findViewById(R.id.Name);
        number = findViewById(R.id.PhoneNumber);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lat=location.getLatitude();
        lng=location.getLongitude();
        helpatthis=findViewById(R.id.GiveHelp);
        helpatthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id= UUID.randomUUID().toString();
                String user_name=name.getText().toString();
                String p_number=number.getText().toString();
                Map<String,Object> map=new HashMap<>();
                map.put("Name",user_name);
                map.put("Number",p_number);
                map.put("Latitude",lat);
                map.put("Longitude",lng);
                ff.collection("HelperInformation").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(HelpActivity.this,PleaseCalmDown.class));
                            finish();
                        }
                        else{
                            Toast.makeText(HelpActivity.this,"Please Try Again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        helpatother=findViewById(R.id.GoProvideHelp);
        helpatother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this,MapsActivity.class));
            }
        });


    }

}
