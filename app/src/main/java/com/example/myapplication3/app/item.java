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

public class item extends FragmentActivity {
    TextView txtTieuDe;
    TextView txtNoiDung;
    Button btnMore;
    Button btnClose;
    ImageView imageCofe;
    ImageView imageLike;
    String kd;
    String vd;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(item.this, MainActivity.class);
                //intent.putExtra("kinhdo", kd);
                //intent.putExtra("vido", vd);
                //sendBroadcast(intent);
                //startActivity(intent);
                //finish();
                String action = "toado";
                Intent intent = new Intent(action);
                intent.setAction(action);
                intent.putExtra("kinhdo", kd);
                intent.putExtra("vido", vd);
                sendBroadcast(intent);
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
        txtTieuDe = this.<TextView>findViewById(R.id.txtTieuDe);
        txtNoiDung = this.<TextView>findViewById(R.id.txtNoiDung);
        btnClose = this.<Button>findViewById(R.id.btnClose);
        btnMore = this.<Button>findViewById(R.id.btnMore);
        imageCofe = findViewById(R.id.imgCafe);
        imageLike = findViewById(R.id.imgLike);

        imageLike.setImageResource(R.drawable.tim);

        //Nhận dữ liệu
        Intent intent = getIntent();
        User user = new User();
        user.setHinhdanh((String) intent.getSerializableExtra("hinhanh3"));
        user.setKinhdo(((String)(intent.getSerializableExtra("kinhdo3"))));
        user.setVido(((String)(intent.getSerializableExtra("vido3"))));
        user.setName((String) intent.getSerializableExtra("name3"));
        user.setThongtin((String) intent.getSerializableExtra("thongtin3"));
        kd = user.getKinhdo();
        vd = user.getVido();
        img = user.getHinhdanh();
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