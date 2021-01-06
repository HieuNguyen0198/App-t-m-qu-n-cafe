package com.example.myapplication3.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication3.R;
import com.example.myapplication3.model.Account;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class SignUp extends Activity {

    EditText txtUsername;
    EditText txtPassword;
    EditText txtConfirm;
    EditText txtPhone;
    TextView txtLogin;
    Button btnContinue;
    private ArrayList<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        accounts = new ArrayList<Account>();
        firebase();
        addControls();
        addEvents();
    }

    private void addEvents() {
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckControls())
                {
                    if(CheckUserName()){
                        //XuLiSignUp();
                        //Intent intent = new Intent(SignUp.this, Login.class);
                        //startActivity(intent);
                        //finish();
                        Intent intent = new Intent(SignUp.this, Otp.class);
                        intent.putExtra("username", txtUsername.getText().toString());
                        intent.putExtra("password", txtPassword.getText().toString());
                        intent.putExtra("phone", txtPhone.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean CheckControls() {
        if(!txtPassword.getText().toString().equals(txtConfirm.getText().toString()))
        {
            Toast.makeText(SignUp.this, "Sai confirm!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtPassword.getText().toString().equals("") || txtPassword.getText().toString().equals("") || txtPhone.getText().toString().equals(""))
        {
            Toast.makeText(SignUp.this, "Không được để trống!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean CheckUserName() {
        for(int i = 0; i < accounts.size(); i++ )
        {
            if(txtUsername.getText().toString().equals(accounts.get(i).username))
            {
                Toast.makeText(SignUp.this, "Username đã tồn tại!", Toast.LENGTH_LONG).show();
                return  false;
            }
            if(txtPhone.getText().toString().equals(accounts.get(i).phone))
            {
                Toast.makeText(SignUp.this, "SĐT đã tồn tại!", Toast.LENGTH_LONG).show();
                return  false;
            }
        }
        return true;
    }

    private void addControls() {
        txtUsername = findViewById(R.id.txtUserNameSU);
        txtPassword = findViewById(R.id.txtPassWordSU);
        txtConfirm = findViewById(R.id.txtConfirm);
        txtLogin = findViewById(R.id.txtLogin);
        txtPhone = findViewById(R.id.txtPhone);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void firebase() {
        //lấy đối tượng FirebaseDatabase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Kết nối tới node có tên là account (node này do ta định nghĩa trong CSDL Firebase)
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
}