package com.example.myapplication3.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication3.R;
import com.example.myapplication3.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Otp extends Activity {
    EditText txtOTP;
    Button btnCreate;
    Account account;
    String verificationCodeBySystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        verificationCodeBySystem = "123456";
        addcontrols();
        addEvents();
    }

    private void addEvents() {
        listenIntent();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckOTP())
                {
                    XuLiSignUp();
                    Intent intent = new Intent(Otp.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    txtOTP.setHint("Sai mã OTP");
                }
            }
        });
    }

    private boolean CheckOTP(){
        if(txtOTP.getText().toString().equals(verificationCodeBySystem)){

            Toast.makeText(Otp.this, "Done", Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(Otp.this, "Fail", Toast.LENGTH_LONG).show();
        return false;
    }

    private void listenIntent() {
        Intent intent = getIntent();
        account = new Account();
        account.setUsername(intent.getStringExtra("username"));
        account.setPassword(intent.getStringExtra("password"));
        account.setPhone(intent.getStringExtra("phone"));
    }

    private void addcontrols() {
        txtOTP = findViewById(R.id.txtOTP);
        btnCreate = findViewById(R.id.btnCreate);
    }

    private void XuLiSignUp() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("account");
        String userId = databaseReference.push().getKey();
        databaseReference.child(userId).setValue(account);
        Toast.makeText(Otp.this, "Thành công! Hãy đăng nhập ngay!", Toast.LENGTH_LONG).show();
    }
}