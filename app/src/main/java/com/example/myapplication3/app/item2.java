package com.example.myapplication3.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication3.R;
import com.example.myapplication3.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class item2 extends FragmentActivity {
    TextView txtTieuDe;
    TextView txtNoiDung;
    Button btnMore;
    Button btnClose;
    ImageView imageCofe;
    ImageView imageLike;
    String kd;
    String vd;
    String img;
    String name;
    String tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item2);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(item2.this, MainActivity.class);
                intent.putExtra("kinhdo2", kd);
                intent.putExtra("vido2", vd);
                intent.putExtra("hinhanh2", img);
                intent.putExtra("name2", name);
                intent.putExtra("thongtin2", tt);
                startActivity(intent);
                finish();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls() {
        txtTieuDe = this.<TextView>findViewById(R.id.txtTieuDe2);
        txtNoiDung = this.<TextView>findViewById(R.id.txtNoiDung2);
        btnClose = this.<Button>findViewById(R.id.btnClose2);
        btnMore = this.<Button>findViewById(R.id.btnMore2);
        imageCofe = findViewById(R.id.imgCafe2);
        imageLike = findViewById(R.id.imgLike2);

        imageLike.setImageResource(R.drawable.tim);

        //Nhận dữ liệu
        Intent intent = getIntent();
        User user = new User();
        user.setHinhdanh((String) intent.getSerializableExtra("hinhanh"));
        user.setKinhdo(((String)(intent.getSerializableExtra("kinhdo"))));
        user.setVido(((String)(intent.getSerializableExtra("vido"))));
        user.setName((String) intent.getSerializableExtra("name"));
        user.setThongtin((String) intent.getSerializableExtra("thongtin"));
        kd = user.getKinhdo();
        vd = user.getVido();
        img = user.getHinhdanh();
        tt = user.getThongtin();
        name = user.getName();

        txtTieuDe.setText("Tên quán: " + user.name);
        txtNoiDung.setText(user.thongtin);
        new Loadimg().execute(img);
    }


    //Async loaing img from url
    private class Loadimg extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmapHinh = null;
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                bitmapHinh = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmapHinh;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageCofe.setImageBitmap(bitmap);
        }
    }
}