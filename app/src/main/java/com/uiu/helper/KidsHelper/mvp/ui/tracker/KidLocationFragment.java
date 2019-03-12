package com.uiu.helper.KidsHelper.mvp.ui.tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.uiu.helper.KidsHelper.mvp.BaseFragment;

import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.model.Location;
import com.uiu.helper.KidsHelper.mvp.model.response.Tracker;
import com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places.SafePlacesFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places.SafePlacesPresenter;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KidLocationFragment extends BaseFragment implements KidLocationContract.View, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private boolean firstLoad=true;


    public static KidLocationFragment newInstance() {
        Bundle args = new Bundle();
        KidLocationFragment instance = new KidLocationFragment();
        instance.setArguments(args);
        return instance;
    }

    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @BindView(R.id.locations_container)
    FrameLayout frameLayout;

    private GoogleMap mMap;
    private Marker currentMarker;
    private KidLocationContract.Presenter mPresenter;

    private List<DirectionsItem> geoList;

    INavigation handler;

    @Override
    public int getID() {
        return R.layout.fragment_kid_location;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(getActivity());
        geoList= new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
 /*       if (this.isVisible()) {
// we check that the fragment is becoming visible
            if (isFragmentVisible_ && mMap!=null ) {
                mMap.clear();
                mPresenter.loadGeofences();
            }
        }*/
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        handler = (INavigation) activity;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        // Add a marker in Sydney and move the camera
        if(mPresenter!=null)
            mPresenter.start();



    }

    public void createFence(LatLng fence,String title,boolean firstLoad){
        CircleOptions circleOptions = new CircleOptions()
                .center( fence ).radius(15)
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(1);


        mMap.addCircle(circleOptions); // these are fences
        BitmapDescriptor descriptor = createPureTextIcon(title);
        if(descriptor!=null)
            mMap.addMarker(new MarkerOptions().icon(descriptor).position(fence)); // these are titles
        if (firstLoad)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fence,16.5f));
        else
            mMap.animateCamera(CameraUpdateFactory.newLatLng(fence));

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
       // tvMessage.setVisibility(View.VISIBLE);
       // tvMessage.setText(message);
    }

    @Override
    public void onGeofenceLoaded(List<DirectionsItem> directions,boolean firstLoad) {
        geoList.clear();
        geoList.addAll(directions);
        for(DirectionsItem directionsItem:directions) {
            if(directionsItem.getLatitude()!=null && directionsItem.getLongitude()!=null)
            createFence(new LatLng(Double.valueOf(directionsItem.getLatitude()), Double.valueOf(directionsItem.getLongitude())), directionsItem.getTitle(),firstLoad);

        }
        mPresenter.getKidLocation();
    }

    @Override
    public void onKidLocationReceived(Tracker tracker) {
        if(mMap!=null)
        {
            LatLng latLng = new LatLng(tracker.getLocation().getLatitude(),tracker.getLocation().getLongitude());
            try {

                MarkerOptions markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(convertIcon()))
                        .position(latLng);
                if(currentMarker!=null)
                    currentMarker.remove();
                currentMarker = mMap.addMarker(markerOptions);
                if(firstLoad)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.5f));
                    else
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                firstLoad=false;
                tvMessage.setVisibility(View.GONE);

            }catch (Exception e){

            }
        }
    }


    public Bitmap convertIcon(){
        int height = 140;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_loc_marker);
        height=bitmapdraw.getBitmap().getHeight()/10;
        width = bitmapdraw.getBitmap().getWidth()/10;
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }

    @Override
    public void setPresenter(KidLocationContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.REQ_LOCATION) {
            JSONObject jsonObject = receiveEvent.getNotificationResponse();
            Location location =  new Gson().fromJson(jsonObject.toString(),Location.class);
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(convertIcon()))
                    .position(latLng);
            currentMarker.remove();
            currentMarker = mMap.addMarker(markerOptions);
            onGeofenceLoaded(geoList,false);
            if(geoList.isEmpty())
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Direction direction) {
        if(!direction.getDirections().isEmpty()){
            if(currentMarker!=null)
            currentMarker.remove();
            currentMarker = null;
            mMap.clear();
            onGeofenceLoaded(direction.getDirections(),false);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(currentMarker))
        {
            Intent streetViewIntent = new Intent(getActivity(),StreetViewPanoramaActivity.class);
            streetViewIntent.putExtra("LATITUDE",marker.getPosition().latitude);
            streetViewIntent.putExtra("LONGITUDE",marker.getPosition().longitude);
            startActivityForResult(streetViewIntent,0);
        }
        return false;
    }

    @OnClick(R.id.btnAddNew)
    public void onAddNewClick(){
        handler.moveToSlide(Constant.SLIDE_INDEX_SAFE_PLACES);
    }

    @OnClick(R.id.btnRemove)
    public void onRemoveClick(){
        handler.moveToSlide(SLIDE_INDEX_SAFE_PLACES);
    }
    public BitmapDescriptor createPureTextIcon(String text) {

        try {



            // The gesture threshold expressed in dip
            float GESTURE_THRESHOLD_DIP = 16.0f;

// Convert the dips to pixels
            final float scale = getContext().getResources().getDisplayMetrics().density;
            int mGestureThreshold = (int) (GESTURE_THRESHOLD_DIP * scale + 0.5f);

            Paint textPaint = new Paint(); // Adapt to your needs
            textPaint.setTextSize(mGestureThreshold);
            textPaint.setTextAlign(Paint.Align.CENTER);
            float textWidth = textPaint.measureText(text);
            float textHeight = textPaint.getTextSize();
            int width = (int) (textWidth);
            int height = (int) (textHeight);
            int noOfLines = text.split("\\s").length;
            Bitmap image = Bitmap.createBitmap(width, height*noOfLines + (10*noOfLines), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(image);

            canvas.translate(0, height);

            // For development only:
            // Set a background in order to see the
            // full size and positioning of the bitmap.
            // Remove that for a fully transparent icon.
            //canvas.drawColor(Color.LTGRAY);
            Rect rect = new Rect();
            canvas.getClipBounds(rect);
            int x = canvas.getWidth()/2;
            int y = 0;
            // canvas.drawText(text, 0, 0, textPaint);
            for (String line: text.split("\\s")) {
                canvas.drawText(line, x, y, textPaint);
                y += textPaint.descent() - textPaint.ascent();
            }

            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);
            return icon;
        }catch (NullPointerException ex){
            Log.e("KidLocationFragment",ex.getMessage());
        }
        return null;
    }


private void loadLocationsFragment(){
    //SafePlacesFragment directionFragment = SafePlacesFragment.newInstance();
    //SafePlacesPresenter safePlacesPresenter = new SafePlacesPresenter(directionFragment,slideItem, PreferenceUtil.getInstance(getActivity()) ,, repository);
}

}
