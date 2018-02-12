package com.itmg.imachika.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itmg.imachika.R;
import com.itmg.imachika.model.Shop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class MapsActivity extends Activity implements OnMapReadyCallback {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private GoogleMap mMap;
    private Shop.Data.Info mInfo;
    private Circle mCircle;
    private Marker mMarker;
    private String TAG = "MapsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mInfo = (Shop.Data.Info)getIntent().getSerializableExtra("data");

        tvTitle.setText(mInfo.getContent());
    }

    @OnClick({R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        String location [] = mInfo.getLocation().split(",");
        LatLng sydney = new LatLng(Float.parseFloat(location[0]), Float.parseFloat(location[1]));
        Log.i(TAG,"mInfo.getContent() === "+mInfo.getContent());
        mMap.addMarker(new MarkerOptions().position(sydney).title(mInfo.getContent()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
        mMap.addMarker(new MarkerOptions()
                .position(sydney));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.map_title_det_view,null);
                TextView name = view.findViewById(R.id.tv_name);
                TextView add = view.findViewById(R.id.tv_address);
                name.setText(mInfo.getContent());
                add.setText(mInfo.getAddress());
                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

    }

}
