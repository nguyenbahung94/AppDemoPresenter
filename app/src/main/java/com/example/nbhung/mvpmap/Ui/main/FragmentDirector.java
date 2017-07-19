package com.example.nbhung.mvpmap.Ui.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbhung.mvpmap.Di.component.Direction;
import com.example.nbhung.mvpmap.Di.component.Weather;
import com.example.nbhung.mvpmap.R;
import com.example.nbhung.mvpmap.Service.LocationService;
import com.example.nbhung.mvpmap.Ui.Presenter.PresenterImp;
import com.example.nbhung.mvpmap.Ui.View.MainView;
import com.example.nbhung.mvpmap.utils.NetWorkUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class FragmentDirector extends Fragment implements OnMapReadyCallback, MainView {
    @Inject
    PresenterImp mPrisenter;
    @Inject
    @Direction
    Retrofit retrofitDerection;
    @Inject
    @Weather
    Retrofit retrofitWeather;
    LocationService locationService;
    private GoogleMap mMap;
    private ImageButton imggetweather, imgButton;
    private LocationManager locationManager;
    private Location mlocation;
    private Button btn_find;
    private Boolean booleanOrigin = false, booleanDestination = false;
    private TextView edtDestination, edtOrigin;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODES = 200;
    private LatLng latLngor, latLnged;
    private boolean gps;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_directions, container, false);
        checkPermissions();
        init();
        return view;
    }

    public void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        edtDestination = (TextView) view.findViewById(R.id.edtDestination);
        edtOrigin = (TextView) view.findViewById(R.id.edtOrigin);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        imggetweather = (ImageButton) view.findViewById(R.id.btngetwearther);
        edtDestination = (TextView) view.findViewById(R.id.edtDestination);
        btn_find = (Button) view.findViewById(R.id.btnFind);
        imgButton = (ImageButton) view.findViewById(R.id.btn_mlocation);
        event();
    }

    public void event() {
        imggetweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrisenter.getWeather(retrofitWeather, locationService.getLocation());
            }
        });
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleanOrigin = true;
                if (locationService.getIsGPSTrackingEnabled()) {
                    edtOrigin.setText("Your Location!");
                    mPrisenter.getLocation(locationService.getLocation(), mMap);
                }
            }
        });
        edtOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                booleanOrigin = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        edtDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                booleanDestination = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODES);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (booleanOrigin && booleanDestination) {
                    mMap.clear();
                    Toast.makeText(getActivity(), "Please Wait....", Toast.LENGTH_SHORT).show();
                    if (edtOrigin.getText().toString().equalsIgnoreCase("YOUR LOCATION!")) {
                        Location tam = locationService.getLocation();
                        mPrisenter.direction(retrofitDerection, new LatLng(tam.getLatitude(), tam.getLongitude()), latLnged, mMap);
                    } else {
                        mPrisenter.direction(retrofitDerection, latLngor, latLnged, mMap);
                    }

                } else {
                    showDialog("Error", "Destination and Origin not empty!");
                }
            }
        });
    }

    public void checkGps() {
        gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gps) {
            showDialog("Gps not enable", "you need to enable gps");
        }
        Log.e("gps", String.valueOf(gps));
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We can now safely use the API we requested access to
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                edtOrigin.setText(place.getAddress());
                latLngor = place.getLatLng();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                Log.i("place", status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {
//                Log.i("place", "canceled");
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODES) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                edtDestination.setText(place.getAddress());
                latLnged = place.getLatLng();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                Log.i("place", status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {
//                Log.i("place", "canceled");
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkPermissions();
        Log.e("ok i'm here", "say hi");
        if (NetWorkUtils.isNetWorkConnected(getActivity())) {
            updateLocation();
        } else {
            Toast.makeText(getActivity(), "No Network To Connect", Toast.LENGTH_SHORT);
        }
        checkGps();

    }

    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mPrisenter.getLocation(mlocation, mMap);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showError(String error) {
        showDialog("error", error);
    }

    @Override
    public void showWeather(String temp) {

        Toast.makeText(getActivity(), temp, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        locationService.stopUsingGPS();
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
