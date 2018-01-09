package com.example.administrator.pospatrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.pospatrol.adapter.PosListAdapter;
import com.example.administrator.pospatrol.beans.PosBean;
import com.example.administrator.pospatrol.utils.CommUtils;
import com.example.administrator.pospatrol.utils.NetUtils;
import com.example.administrator.pospatrol.utils.XmlUtils;

/**
 * 终端列表界面
 *
 * @author xulei 2016-1-23
 */
public class PosListActivity extends Activity implements View.OnClickListener {
    ListView posList;
    Handler handler;
    List<PosBean> posData;
    PosListAdapter adapter;
    boolean nextAct = false;
    String Latitude;
    String Longitude;
    @Override
    protected void onDestroy() {
        if(!nextAct)
        {
            Log.e("PosListActivity","退出软件");
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_list);
        setCustomTitle();
        Intent intent = getIntent();
        if(intent!=null)
        {double lati = intent.getDoubleExtra("Latitude",-1);
            double Long = intent.getDoubleExtra("Longitude",-1);
            Latitude = CommUtils.locationToString(lati,7);
            Longitude = CommUtils.locationToString(Long,8);
        }
        posList = (ListView) findViewById(R.id.pos_listview);
        posList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(PosListActivity.this,
                        FormListActivity.class);
                PosBean pos = posData.get(position);
                intent.putExtra("posNo", pos.getPosNo());
                intent.putExtra("posName", pos.getPosName());
                startActivity(intent);
                nextAct = true;
                finish();
            }

        });

        // 数据需要从网络下载，创建handler用于解析数据后的界面更新
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    adapter = new PosListAdapter(PosListActivity.this, posData);
                    posList.setAdapter(adapter);
                } else {
                    Toast.makeText(PosListActivity.this,
                            findStr(R.string.loaddata_err), Toast.LENGTH_LONG)
                            .show();
                }
            }
        };

        getPosData();
    }

    /**
     * 从服务器端获取终端数据并解析
     */
    private void getPosData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    posData = XmlUtils.getPos(getAssets().open("items.xml"));
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    /**
     * 设置自定义的标题栏
     */
    private void setCustomTitle() {
        Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        TextView title = (TextView) findViewById(R.id.title_label);
        title.setText(findStr(R.string.pos_list));
        leftBtn.setVisibility(View.GONE);
        rightBtn.setText(findStr(R.string.refresh));
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

        }
    }
}
