package com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.events.DirectionCreatedEvent;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.DirectionsItem;
import com.uiu.helper.R;

import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.FileOutputStream;

import butterknife.BindView;


public class ChooseSafePlaceFragment extends BaseFragment implements View.OnClickListener,
        OnMapReadyCallback {


    public static final String TAG = "ChooseDestination";
    @BindView(R.id.choose_destination_search_on_map)
    TextView mSearchOnMapBtn;
    @BindView(R.id.choose_destination_create)
    TextView mCreateBtn;
    @BindView(R.id.choose_destination_cancel)
    TextView mCancelBtn;
    @BindView(R.id.destination_manual_destination)
    TextView mDestinationAddress;
    @BindView(R.id.choose_destination_wiser_location)
    EditText mDestinationTitle;

    @BindView(R.id.tvLocationAddTitle)
    TextView tvViewTitle;

    private GoogleMap mMap;
    private Marker currentMarker;

    private DirectionsItem newDirection;


    @Override
    public int getID() {
        return R.layout.choose_destination_fragment;
    }

    @Override
    public void initUI(View view) {

        tvViewTitle.setText(getContext().getString(R.string.add_safe_place));
        view.findViewById(R.id.llTop).setOnTouchListener((v, event) -> {
            Utils.hidekeyPad(getActivity(), ((LinearLayout) view.findViewById(R.id.llTop)));
            return false;
        });


        mSearchOnMapBtn.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        view.findViewById(R.id.touchble_popup_background).setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public static ChooseSafePlaceFragment newInstance() {

        Bundle args = new Bundle();
        ChooseSafePlaceFragment cdf = new ChooseSafePlaceFragment();
        cdf.setArguments(args);

        return cdf;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.choose_destination_search_on_map:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), 1234);
                    getView().findViewById(R.id.mapView).setVisibility(View.VISIBLE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                break;

            case R.id.choose_destination_create:
                if (newDirection == null) {
                    Utils.showAlert(getActivity(),
                            getResources().getString(R.string.pick_a_location)
                            , getResources().getColor(R.color.directions_slide_title_color));
                    return;
                }
                if (mDestinationTitle.getText().toString().equals("") || mDestinationAddress.getText().toString().equals(getString(R.string.no_address))) {
                    Utils.showAlert(getActivity(),
                            getResources().getString(R.string.enter_a_destination)
                            , getResources().getColor(R.color.directions_slide_title_color));
                    mDestinationTitle.requestFocus();
                    Utils.showkeyPad(getActivity(), mDestinationTitle);
                    break;
                }
                newDirection.setTitle(mDestinationTitle.getText().toString());

                CaptureMapScreen(newDirection);
                break;

            case R.id.touchble_popup_background:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.choose_destination_cancel:
                getFragmentManager().popBackStackImmediate();
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.d("choose", "onActivityResult");

        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(getContext(), intent);

            Log.d("adding direction", "place " + place.getName());

            newDirection = new DirectionsItem();
            newDirection.setLatitude(String.valueOf(place.getLatLng().latitude));
            newDirection.setLongitude(String.valueOf(place.getLatLng().longitude));
            addMarkerOnMap(place.getLatLng());
            newDirection.setAddress((String) place.getAddress());
            if (place.getAddress().toString().isEmpty()) {
                mDestinationAddress.setText(place.getName());
                mDestinationTitle.setText("");
            } else {
                mDestinationAddress.setText(place.getAddress());
            }

        }else
            getView().findViewById(R.id.mapView).setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void addMarkerOnMap(LatLng latLng){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.5f));


    }

    public void CaptureMapScreen(DirectionsItem directionsItem)
    {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmap = snapshot;
                try {
                    String image =Utils.bitmapToBase64(bitmap);
                    directionsItem.setImage(image);
                    EventBus.getDefault().post(new DirectionCreatedEvent(directionsItem));
                    Utils.hidekeyPad(getActivity(), getView());
                    getFragmentManager().popBackStackImmediate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mMap.snapshot(callback);

       }

}
