package com.example.nbhung.mvpmap.Ui.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

import com.example.nbhung.mvpmap.Di.component.DaggerFragmentComponent;
import com.example.nbhung.mvpmap.Di.component.Direction;
import com.example.nbhung.mvpmap.Di.component.Weather;
import com.example.nbhung.mvpmap.Di.module.ActivityModule;
import com.example.nbhung.mvpmap.Di.module.NetWorkModule;
import com.example.nbhung.mvpmap.R;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import retrofit2.Retrofit;

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
    private ImageButton imggetweather, imgButton;
    private LocationManager locationManager;
    private Location mlocation;
    private Button btn_find, btnGetWeather, btnGetCurrentLocation, btnAddPicker;
    private Boolean booleanOrigin = false, booleanDestination = false;
    private TextView edtDestination, edtOrigin, tvLocationWeather;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODES = 200;
    private LatLng latLngor, latLnged;
    private boolean gps=false;
    private View view, viewWearther;
    private GoogleMap mMap;
    private LocationListener listener;
    private LatLng myLatlng;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFragmentComponent.builder().activityModule(new ActivityModule(this))
                .netWorkModule(new NetWorkModule(getContext())).build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_directions, container, false);
        init();
        return view;
    }

    public void init() {
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        edtDestination = (TextView) view.findViewById(R.id.edtDestination);
        edtOrigin = (TextView) view.findViewById(R.id.edtOrigin);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        imggetweather = (ImageButton) view.findViewById(R.id.btngetwearther);
        edtDestination = (TextView) view.findViewById(R.id.edtDestination);
        btn_find = (Button) view.findViewById(R.id.btnFind);
        imgButton = (ImageButton) view.findViewById(R.id.btn_mlocation);


        viewWearther = view.findViewById(R.id.layout_partof_wearther);
        btnGetWeather = (Button) viewWearther.findViewById(R.id.btn_getwearther);
        btnGetCurrentLocation = (Button) viewWearther.findViewById(R.id.btn_getcurrentlocation);
        btnAddPicker = (Button) viewWearther.findViewById(R.id.btn_picker);
        tvLocationWeather = (TextView) viewWearther.findViewById(R.id.tv_location_getwearther);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        eventDirection();
        evenWeather();
    }

    public void eventDirection() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        imggetweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrisenter.getWeather(retrofitWeather, mlocation);
            }
        });
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleanOrigin = true;
                checkGps();
                if (gps) {
                    edtOrigin.setText("Your Location!");
                    mPrisenter.getLocation(mlocation, mMap,getActivity());
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
                        mPrisenter.direction(retrofitDerection, new LatLng(mlocation.getLatitude(), mlocation.getLongitude()), latLnged, mMap);
                    } else {
                        mPrisenter.direction(retrofitDerection, latLngor, latLnged, mMap);
                    }

                } else {
                    showDialog("Error", "Destination and Origin not empty!");
                }
            }
        });
    }

    public void evenWeather() {
        btnAddPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "add picker", Toast.LENGTH_SHORT).show();
            }
        });
        btnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "get current location", Toast.LENGTH_SHORT).show();
            }
        });
        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "get weather", Toast.LENGTH_SHORT).show();
            }
        });
        tvLocationWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "tv location weather", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                edtOrigin.setText(place.getAddress());
                latLngor = place.getLatLng();
            }
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODES) {

                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                edtDestination.setText(place.getAddress());
                latLnged = place.getLatLng();

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (NetWorkUtils.isNetWorkConnected(getActivity())) {
            updateLocation(mlocation);
        } else {
            Toast.makeText(getActivity(), "No Network To Connect", Toast.LENGTH_SHORT).show();
        }
        checkGps();

    }

    public void updateLocation(Location location) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mPrisenter.getLocation(location, mMap,getActivity());
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
    public void showDirection() {
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.me)).position(new LatLng(mlocation.getLatitude(), mlocation.getLongitude())));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
