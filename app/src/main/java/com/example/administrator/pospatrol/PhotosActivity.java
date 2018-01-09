package com.example.administrator.pospatrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;

import com.example.administrator.pospatrol.utils.CommUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 配置界面
 * @author xulei
 * 2016-1-23
 */
public class PhotosActivity extends Activity implements View.OnClickListener{
    int currPhoto = 0;
    File imagFile;
    ImageView[] imageView = new ImageView[6];
    Bitmap[] bitMaps = new Bitmap[6];
    boolean nextAct = false;
    @Override
    protected void onDestroy() {
        if(!nextAct)
        {
            Log.e("PhotosActivity","退出软件");
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        setCustomTitle();
        findViewById(R.id.camery).setOnClickListener(this);

        imageView[0] = (ImageView)findViewById(R.id.photo1);
        imageView[1] = (ImageView)findViewById(R.id.photo2);
        imageView[2] = (ImageView)findViewById(R.id.photo3);
        imageView[3] = (ImageView)findViewById(R.id.photo4);
        imageView[4] = (ImageView)findViewById(R.id.photo5);
        imageView[5] = (ImageView)findViewById(R.id.photo6);

    }

    /**
     * 设置自定义的标题栏
     */
    private void setCustomTitle() {
        Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        TextView title = (TextView) findViewById(R.id.title_label);
        title.setText(findStr(R.string.camer));
        leftBtn.setVisibility(View.GONE);
        rightBtn.setText(findStr(R.string.submit));
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent
                        (PhotosActivity.this,SubmitActivity.class);
                startActivity(intent);
                nextAct = true;
                finish();
            }
        });
    }

    /**
     * 查找字符串
     */
    private String findStr(int id){
        return getResources().getString(id);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.camery)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imagFile = new File(CommUtils.basePath,
                    System.currentTimeMillis()+"_"+currPhoto+".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imagFile));
            startActivityForResult(intent,1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1)
        {
            try {
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inSampleSize = 4;
                bitMaps[currPhoto] = BitmapFactory.decodeStream
                        (new FileInputStream(imagFile),null,op);
                int width = bitMaps[currPhoto].getWidth();
                int height = bitMaps[currPhoto].getHeight();
                Bitmap newBitmap = Bitmap.createBitmap
                        (width, height, Config.ARGB_8888);
                Canvas canvas = new Canvas(newBitmap);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(16);
                String text = String.format("%tF %<tT",System.currentTimeMillis());
                int textwidth = text.length()*16/2;
                canvas.drawText(text,width-textwidth-10,height-16-10, paint);
                canvas.drawBitmap(bitMaps[currPhoto],0,0, paint);
                Matrix matrix = new Matrix();
                if(width>height)
                {
                    matrix.setRotate(90);
                    newBitmap = Bitmap.createBitmap(newBitmap,0,0,
                            width,height,matrix,true);
                }
                bitMaps[currPhoto] = newBitmap;
                imageView[currPhoto].setImageBitmap(newBitmap);
                OutputStream stream = new FileOutputStream(imagFile);
                newBitmap.compress(CompressFormat.JPEG,100,stream);
                currPhoto++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
