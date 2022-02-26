package com.shrewd.poppydealers.utilities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.ActivityMapsBinding;
import com.shrewd.poppydealers.response.ApiResponse;
import com.shrewd.poppydealers.retrofit.APIClient;
import com.shrewd.poppydealers.retrofit.GetResult;
import com.shrewd.poppydealers.views.activity.MainActivity;
import com.shrewd.poppydealers.views.activity.edit.EditProfile;
import com.shrewd.poppydealers.views.activity.intro.Intro;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GetResult.MyListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng dynamic;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng coimbatore = new LatLng(11.0168, 76.9558);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coimbatore);
        markerOptions.title(getAddress(coimbatore));
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coimbatore, 10));
        mMap.addMarker(markerOptions);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                dynamic = latLng;

                sessionManager.putString(Constants.KEY_LATITUDE, String.valueOf(latLng.latitude));
                sessionManager.putString(Constants.KEY_LONGITUDE, String.valueOf(latLng.longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getAddress(latLng));
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.addMarker(markerOptions);

                binding.mapAddress.setText(getAddress(latLng));


            }
        });


        binding.mapAddressSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.mapAddress.getText().toString().isEmpty()) {

                    Bundle bundle = getIntent().getExtras();
                    if (bundle != null) {
                        if (bundle.getString(Constants.KEY_CLASS_TYPE).equalsIgnoreCase("edit")) {
                            Intent intent = new Intent(MapsActivity.this, EditProfile.class);
                            intent.putExtra(Constants.KEY_MAP_ADDRESS, getAddress(dynamic));
                            startActivity(intent);
                            finish();
                        } else if (bundle.getString(Constants.KEY_CLASS_TYPE).equalsIgnoreCase("intro")) {
                            Intent intent = new Intent(MapsActivity.this, Intro.class);
                            intent.putExtra(Constants.KEY_MAP_ADDRESS, getAddress(dynamic));
                            intent.putExtra(Constants.KEY_CLASS_TYPE, "signUp");
                            intent.putExtra(Constants.KEY_NAME, bundle.getString(Constants.KEY_NAME));
                            startActivity(intent);
                            finish();
                        } else if (bundle.getString(Constants.KEY_CLASS_TYPE).equalsIgnoreCase("login")) {
                            insertLatLng();
                        }


                    }


                } else {
                    Toast.makeText(getApplicationContext(), "please pick address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertLatLng() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.DEALER_ID, sessionManager.getString(Constants.DEALER_ID));
            jsonObject.put(Constants.KEY_LATITUDE, dynamic.latitude);
            jsonObject.put(Constants.KEY_LONGITUDE, dynamic.longitude);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().setLocation(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForResponse(call, "1");

    }

    private String getAddress(LatLng latLng) {

        String addressLine;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressLine = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (IOException e) {
            e.printStackTrace();
            return "No Address Found";

        }

        return addressLine;
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startActivity(new Intent(MapsActivity.this, Intro.class).putExtra(Constants.KEY_CLASS_TYPE, "signUp"));
            this.finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        if (callNo.equalsIgnoreCase("1")) {
            Gson gson = new Gson();
            ApiResponse response = gson.fromJson(result.toString(), ApiResponse.class);
            Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            if (response.getSuccess() == true) {
                sessionManager.putBoolean(Constants.KEY_LOGIN, true);
                sessionManager.putBoolean(Constants.KEY_LOCATION_STATUS, true);
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}