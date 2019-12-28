package com.bawei.hujintaodemo1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ma_ed)
    EditText maEd;
    @BindView(R.id.ma_bt_create)
    Button maBtCreate;
    @BindView(R.id.ma_bt_camera)
    Button maBtCamera;
    @BindView(R.id.ma_bt_photos)
    Button maBtPhotos;
    @BindView(R.id.ma_bt_tu)
    Button maBtTu;
    @BindView(R.id.ma_img)
    ImageView maImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化ButterKnife
        ButterKnife.bind(this);
        //初始化二维码库
        CodeUtils.init(this);
        maImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CodeUtils.analyzeByImageView(maImg, new CodeUtils.AnalyzeCallback() {
                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        Toast.makeText(MainActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
    }

    @OnClick({R.id.ma_bt_create, R.id.ma_bt_camera, R.id.ma_bt_photos, R.id.ma_bt_tu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ma_bt_create:
                String string = maEd.getText().toString();
                Bitmap image = CodeUtils.createImage(string, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.s1));
                maImg.setImageBitmap(image);
                break;
            case R.id.ma_bt_camera:
                CodeUtils.analyzeByCamera(this);
                break;
            case R.id.ma_bt_photos:
                CodeUtils.analyzeByPhotos(this);
                break;
            case R.id.ma_bt_tu:
                NetUtil.getInstance().getJsonGet("http://blog.zhaoliang5156.cn/api/student/clazzstudent.json", new NetUtil.MyCallBack() {
                    @Override
                    public void onGetJson(String json) {
                        Toast.makeText(MainActivity.this, ""+json, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CodeUtils.onActivityResult(this, requestCode, resultCode, data, new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Toast.makeText(MainActivity.this, ""+result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnalyzeFailed() {
                Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
