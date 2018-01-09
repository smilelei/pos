package com.example.administrator.pospatrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 配置界面
 * @author xulei
 * 2016-1-23
 */
public class SubmitActivity extends Activity implements View.OnClickListener{
    boolean nextAct = false;
    @Override
    protected void onDestroy() {
        if(!nextAct)
        {
            Log.e("SubmitActivity","退出软件");
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit);
        setCustomTitle();
        findViewById(R.id.back_list).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
    }

    /**
     * 设置自定义的标题栏
     */
    private void setCustomTitle() {
        Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        TextView title = (TextView) findViewById(R.id.title_label);
        title.setText(findStr(R.string.sub_success));
        rightBtn.setVisibility(View.GONE);
        leftBtn.setVisibility(View.GONE);

    }

    /**
     * 查找字符串
     */
    private String findStr(int id){
        return getResources().getString(id);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.exit)
        {

            Log.e("SubmitActivity","exit");
            finish();
        }
        else if(v.getId()==R.id.back_list)
        {
            Intent intent = new Intent
                    (SubmitActivity.this,PosListActivity.class);
            Log.e("SubmitActivity","intent");
            startActivity(intent);
        }
    }
}
