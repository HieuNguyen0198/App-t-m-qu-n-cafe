package com.example.myapplication3.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication3.R;
import com.example.myapplication3.model.Account;
import com.example.myapplication3.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class Login extends Activity {
    EditText txtUserName;
    EditText txtPassword;
    Button btnLogin;
    CheckBox checkBox;
    TextView textView;
    private static  final String TAG = "Login";
    private ArrayList<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accounts = new ArrayList<Account>();
        firebase();
        addControls();
        addEvents();
    }

    private void addEvents() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 for(int i = 0; i < accounts.size(); i++)
                 {
                     if (accounts.get(i).username.equals(txtUserName.getText().toString()) && accounts.get(i).password.equals(txtPassword.getText().toString())) {
                         Intent intent = new Intent(Login.this, MainActivity.class);
                         intent.putExtra("username", accounts.get(i).username);
                         Toast.makeText(Login.this, "Chào mừng" + " " + accounts.get(i).username + " quay trở lại", Toast.LENGTH_LONG).show();
                         startActivity(intent);
                         finish();
                         return;
                     }
                 }
                 Toast.makeText(Login.this, "Sai mật khẩu hoặc tên đăng nhập", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void firebase() {
        //lấy đối tượng FirebaseDatabase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
        final DatabaseReference myRef = database.getReference("account");
        //truy suất và lắng nghe sự thay đổi dữ liệu

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue().toString();
                Account account = new Account(value);
                accounts.add(account);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassWord);
        btnLogin = findViewById(R.id.button_signin);
        checkBox = findViewById(R.id.check);
        textView = findViewById(R.id.txtSigUp);
    }
}