package com.example.myapplication3.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication3.R;
import com.example.myapplication3.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    Context context;
    ArrayList<User> users;
    LayoutInflater inflater;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return  users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       ViewH viewH;
       if(convertView == null){
           viewH = new ViewH();
           convertView = inflater.inflate(R.layout.item, null);
           viewH.txt = convertView.findViewById(R.id.txtItem);
           viewH.txt2 = convertView.findViewById(R.id.txtItem2);
           //imageView = convertView.findViewById(R.id.imageView);
           viewH.imageView = convertView.findViewById(R.id.imageView);
           convertView.setTag(viewH);
       }
       else {
           viewH = (ViewH) convertView.getTag();
       }

       User user = users.get(position);
       viewH.txt.setText(user.getName());
       viewH.txt2.setText(user.getThongtin());
       if(viewH.imageView != null) {
           new BitmapWorkerTask(viewH.imageView).execute(user.getHinhdanh());
       }
       return convertView;
    }

    /*private class Loadimg extends AsyncTask<String, Void, Bitmap>{
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
            imageView.setImageBitmap(bitmap);
        }
    }*/

    // ----------------------------------------------------
    // Load bitmap in AsyncTask
    // ref:
    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String imageUrl;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            return LoadImage(imageUrl);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        private Bitmap LoadImage(String URL) {
            Bitmap bitmap = null;
            InputStream in = null;
            try {
                in = OpenHttpConnection(URL);
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e1) {
            }
            return bitmap;
        }

        private InputStream OpenHttpConnection(String strURL)
                throws IOException {
            InputStream inputStream = null;
            URL url = new URL(strURL);
            URLConnection conn = url.openConnection();

            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.getInputStream();
                }
            } catch (Exception ex) {
            }
            return inputStream;
        }
    }

    public static class ViewH{
        public TextView txt;
        public  TextView txt2;
        public ImageView imageView;
    }
}
