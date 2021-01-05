package com.example.myapplication3.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication3.R;
import com.example.myapplication3.adapter.UserAdapter;
import com.example.myapplication3.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
public class fra1 extends Fragment {
    private ArrayList<User> users;
    String TAG = "FIREBASE";
    UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fra1, null);
        final ListView lvHocSinh = view.findViewById(R.id.lvHocSinh);
        users = new ArrayList<User>();
        //fakeData();
        addFirebase();
        userAdapter = new UserAdapter(getActivity(), users);

        lvHocSinh.setAdapter(userAdapter);
        lvHocSinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), item2.class);
                intent.putExtra("name", users.get(position).getName());
                intent.putExtra("hinhanh", users.get(position).getHinhdanh());
                intent.putExtra("thongtin", users.get(position).getThongtin());
                intent.putExtra("kinhdo", users.get(position).getKinhdo());
                intent.putExtra("vido", users.get(position).getVido());
                startActivity(intent);
            }
        });;
        return  view;
    }

    private void addFirebase() {
        //lấy đối tượng FirebaseDatabase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
        final DatabaseReference myRef = database.getReference("cafe");
        //truy suất và lắng nghe sự thay đổi dữ liệu
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue().toString();
                User user = new User(value);
                users.add(user);
                userAdapter.notifyDataSetChanged();
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

    private void fakeData() {
        users.add(new User("Toco", "https://lh5.googleusercontent.com/p/AF1QipOmpmNYnixMHYKj6cL9xsQ_WGGG7zTfTIKwUAj_=w408-h544-k-no", "100", "50", "ABC"));
        users.add(new User("Toco2", "https://lh5.googleusercontent.com/p/AF1QipMER91gJKgA3euC6baetHRmbCQoIEUA87KcpZwm=w426-h240-k-no", "100", "50", "ABC"));
        users.add(new User("Toco3", "https://lh5.googleusercontent.com/p/AF1QipPOGajkXw16hzorgy74W8v--uno9cZIbt9T4vBz=w408-h306-k-no", "100", "50", "ABC"));
        users.add(new User("Toco4", "1", "100", "50", "ABC"));
        users.add(new User("Toco5", "1", "100", "50", "ABC"));;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}

