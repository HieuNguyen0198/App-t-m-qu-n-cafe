package com.example.myapplication3.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.myapplication3.R;
import com.example.myapplication3.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindWay extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener,
        PermissionsListener, MapboxMap.OnMapClickListener {

    private MapView mapView;
    private MapboxMap map;
    private FloatingSearchView floatingSearchView1;
    private FloatingSearchView floatingSearchView2;
    //private Button startButton;
    private FloatingActionButton startButton;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point currentPositon;
    //private Location chooseLocation;
    //private Point choosePosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private Marker currentMarker;
    //private Point exPosition;
    private NavigationMapRoute navigationMapRoute;
    private static  final String TAG = "MainActivity";

    //firebase
    String TAG1 = "FIREBASE";
    ListView lvContact;
    ArrayAdapter<String> adapter;

    //frament
    fra1 fragment;
    View fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_find_way);
        getSupportActionBar().hide();
        addControls();
        fragment1 = findViewById(R.id.simpleFrameLayout);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        addEvent();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void addEvent() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableLocation();
                /*NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .origin(originPosition)
                        .destination(destinationPosition)
                        .shouldSimulateRoute(true)
                        .build();
                NavigationLauncher.startNavigation(MainActivity.this, options);*/
            }
        });
    }

    private void addControls() {
        startButton = findViewById(R.id.startButton);
        mapView =  findViewById(R.id.mapView2);
        floatingSearchView1 = findViewById(R.id.floating_search_view);
        floatingSearchView2 = findViewById(R.id.floating_search_view2);
    }

    void init(){
        Fragment fragment = null;
        fragment = new fra1();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();
        addFirebase();

        //lang nghe
        MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyBroadcastReceiver.ACTION_FIRST_ACTION);
        registerReceiver(myBroadcastReceiver, intentFilter);

        map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //lấy đối tượng FirebaseDatabase
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
                final DatabaseReference myRef = database.getReference("cafe");
                //truy suất và lắng nghe sự thay đổi dữ liệu
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String key = data.getKey();
                            String value = data.getValue().toString();
                            User user = new User(value);
                            //xu li
                            if(marker.getPosition().getLatitude() == Double.parseDouble(user.getVido()) && marker.getPosition().getLongitude() == Double.parseDouble(user.getKinhdo())){
                                //Đoạn này mở activity và truyền temp qua
                                Intent intent = new Intent(FindWay.this, item.class);
                                intent.putExtra("name3", user.name);
                                intent.putExtra("hinhanh3", user.hinhdanh);
                                intent.putExtra("thongtin3", user.thongtin);
                                intent.putExtra("kinhdo3", user.kinhdo);
                                intent.putExtra("vido3", user.vido);
                                startActivity(intent);
                                //Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });

                //Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        //Lang nghe intent
        listenIntent();
    }

    private void listenIntent() {
        Intent intent = getIntent();
        User user = new User();
        user.setKinhdo(intent.getStringExtra("kinhdo2"));
        user.setVido(intent.getStringExtra("vido2"));
        user.setHinhdanh(intent.getStringExtra("hinhanh2"));
        user.setThongtin(intent.getStringExtra("thongtin2"));
        user.setName(intent.getStringExtra("name2"));
        currentPositon = Point.fromLngLat(Double.parseDouble(user.getKinhdo()), Double.parseDouble(user.getVido()));
        originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
        getRoute(originPosition, currentPositon);
    }

    private void addFirebase() {
        //lấy đối tượng FirebaseDatabase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
        final DatabaseReference myRef = database.getReference("cafe");
        //truy suất và lắng nghe sự thay đổi dữ liệu

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //list.clear();
                //adapter.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //lấy key của dữ liệu
                    String key = data.getKey();
                    //lấy giá trị của key (nội dung)
                    String value = data.getValue().toString();
                    User user = new User(value);
                    //list.add(user);
                    //adapter.add(user.name);


                    //add marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(user.name);
                    markerOptions.position(new LatLng(user.getVidoD(), user.getKinhdoD()));
                    IconFactory iconFactory = IconFactory.getInstance(FindWay.this);
                    Icon icon = iconFactory.fromResource(R.drawable.cafe6);
                    markerOptions.icon(icon);
                    map.addMarker(markerOptions);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    //lop lang nghe
    class MyBroadcastReceiver extends BroadcastReceiver {

        public static final String ACTION_FIRST_ACTION  = "toado";

        MyBroadcastReceiver() {
        }
        //Code thi hành khi Receiver nhận được Intent
        @Override
        public void onReceive(Context context, Intent intent) {

            //Kiểm tra Action của Intent nhận được có tên irst-broadcastintent
            if (intent.getAction().equals(MyBroadcastReceiver.ACTION_FIRST_ACTION)) {
                //Đọc dữ liệu trong Intent
                String d = intent.getStringExtra("kinhdo");
                String e = intent.getStringExtra("vido");
                //Toast.makeText(context, d + e, Toast.LENGTH_SHORT).show();
                currentPositon = Point.fromLngLat(Double.parseDouble(d), Double.parseDouble(e));
                originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
                getRoute(originPosition, currentPositon);
            }
        }
    }

    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            //do some stuff
            //lay last location
            initializeLocationEngine();

            //lay location
            initializeLocationLayer();
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint({"MissingPermission", "Range"})
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationLayer() {
        if(locationLayerPlugin == null){
            locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        }
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.COMPASS);
    }

    private  void setCameraPosition(Location location)
    {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 12.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        //nhan du lieu
        if(destinationPosition != null){
            map.removeMarker(destinationMarker);
        }
        destinationMarker = map.addMarker(new MarkerOptions().position(point));

        destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        //LatLng latLng = new LatLng();
        //latLng.setLongitude(106.83704271573023);
        //latLng.setLatitude(10.81302276168788);
        //originPosition = Point.fromLngLat( 106.83704271573023, 10.81302276168788 );
        //currentMarker = map.addMarker((new MarkerOptions().position(latLng)));

        originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
        getRoute(originPosition, destinationPosition);

        //startButton.setEnabled(true);
        //startButton.setBackgroundResource(R.color.mapbox_blue);

        /*map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });*/
    }

    private  void  getRoute(Point origin, Point destination){
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body() == null){
                            Log.e(TAG, "no router found, abc");
                            return;
                        }else if(  response.body().routes().size()==0) {
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        DirectionsRoute currentRoute = response.body().routes().get(0);

                        if(navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        }else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }
                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error" + t.getMessage());
                    }
                });
    }


    @Override
    @SuppressLint("MissingPermission")
    public void onConnected()
    {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //present toast or dialog.
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    @SuppressLint("MissingPermission")
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin != null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin!= null){
            locationLayerPlugin.onStop();
        }
        //Toast.makeText(this,"stop",Toast.LENGTH_LONG).show();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine != null){
            locationEngine.deactivate();
        }
        //Toast.makeText(this,"detroy",Toast.LENGTH_LONG).show();
        mapView.onDestroy();
    }
}










