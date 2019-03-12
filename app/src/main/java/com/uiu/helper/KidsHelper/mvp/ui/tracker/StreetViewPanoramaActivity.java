package com.uiu.helper.KidsHelper.mvp.ui.tracker;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.R;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StreetViewPanoramaActivity extends BaseActivity {

    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    Double longitude,latitude;
    @Override
    public int getID() {
        return R.layout.activity_street_view;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        latitude =  getIntent().getDoubleExtra("LATITUDE",0);
        longitude = getIntent().getDoubleExtra("LONGITUDE",0);
        toolbar.findViewById(R.id.header_btn_right).setVisibility(View.GONE);
        setToolBar(toolbar,"",true);



        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetViewPanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                panorama -> {
                    progressBar.show();
                    panorama.setPosition(new LatLng(latitude,longitude));
                    panorama.setOnStreetViewPanoramaChangeListener(streetViewPanoramaLocation -> {
                        progressBar.hide();
                        if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
                            // location is present
                            tvMessage.setVisibility(View.GONE);
                        } else {
                            tvMessage.setVisibility(View.VISIBLE);
                            // location not available
                        }
                    });

                });
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }
}
