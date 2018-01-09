package com.example.administrator.pospatrol;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 定位界面
 *
 * @author xulei 2016-1-23
 */
public class LocationActivity extends Activity implements View.OnClickListener {
    boolean nextAct = false;
    @Override
    protected void onDestroy() {
        if(!nextAct)
        {
            Log.e("LocationActivity","退出软件");
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setCustomTitle();
        final LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        Location location =  lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null)
        {
            startPosList(location);
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,50,new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Location location = lm.getLastKnownLocation(provider);
                if(location!=null)
                {
                    startPosList(location);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {
                startPosList(location);
            }
        });
    }
    private void startPosList(Location location)
    {
        Intent intent = new Intent(this, PosListActivity.class);
        intent.putExtra("Latitude",location.getLatitude());
        intent.putExtra("Longitude",location.getLongitude());
        startActivity(intent);
        nextAct = true;
        finish();
    }
    /**
     * 设置自定义的标题栏
     */
    private void setCustomTitle() {
        Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        TextView title = (TextView) findViewById(R.id.title_label);
        title.setText(findStr(R.string.location));
        leftBtn.setText(findStr(R.string.back));
        rightBtn.setText(findStr(R.string.refresh));
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
    }

    /**
     * 查找字符串
     */
    private String findStr(int id) {
        return getResources().getString(id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_right_btn) {

        }else if(v.getId() == R.id.title_left_btn)
        {
            startActivity(new Intent(this, LoginActivity.class));

        }

    }
}
