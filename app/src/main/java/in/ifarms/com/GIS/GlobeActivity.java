package in.ifarms.com.GIS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidmapsextensions.ClusterGroup;
import com.androidmapsextensions.ClusterOptions;
import com.androidmapsextensions.ClusterOptionsProvider;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MapView;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.FaultReportActivity.BuildingLocationResponse;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import in.ifarms.com.TaskSearch.GetBuildingsResponse;
import in.ifarms.com.TaskSearch.PmTaskActivity;
import in.ifarms.com.Search.EditFaultReportActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class GlobeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private OverlappingMarkerSpiderfier oms;
    private ClusteringSettings clusterSettings;
    private ProgressDialog mProgress;
    private String token, workspace;
    private Spinner zone_spinner_globeFault, zone_spinner_locationFault;
    private com.google.android.gms.maps.GoogleMap map;
    private Button showFaultButton, showTaskButton;
    private MapView mapViewFault;
    private ArrayList<String> tn0_List = new ArrayList<String>();
    private ArrayList<String> frid_List = new ArrayList<String>();
    private List<String> buildingList = new ArrayList<>();
    private List<Integer> buildingIdList = new ArrayList<>();
    private ArrayAdapter<String> buildingAdapter;
    private List<String> locationList = new ArrayList<>();
    private List<Integer> locationIdList = new ArrayList<>();
    private ArrayAdapter<String> locationAdapter;
    private int buildingIdInt, locationIdInt, index = 0;
    private String TAG, user;
    private List<LatLng> latlogofTaskLoc = new ArrayList<LatLng>();
    private List<LatLng> latlogofFaultLoc = new ArrayList<LatLng>();
    private LatLng pointsOfTask, pointsofFault = new LatLng(0.001, 0.002);
    private Marker marker;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globe);
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("worklist");
        Log.d(TAG, "onCreate: gggggggggggggg" + user);
        mapViewFault = findViewById(R.id.map_view);
        mapViewFault.onCreate(savedInstanceState);
        mapViewFault.getExtendedMapAsync(this);

        toolbar=findViewById(R.id.toolbar_globe);
        setSupportActionBar(toolbar);

        mProgress = new ProgressDialog(GlobeActivity.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        showFaultButton = findViewById(R.id.showFaultButton);
        showTaskButton = findViewById(R.id.showTaskButton);
        buildingList.clear();
        buildingList.add("Select the building");
        buildingIdList.add(1);
        locationList.clear();
        locationList.add("Select the location");
        locationIdList.add(1);

        zone_spinner_globeFault = findViewById(R.id.spinner_zone_globe_activity);
        zone_spinner_locationFault = findViewById(R.id.spinner_locaton_globe_activity);

        buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, buildingList);
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationList);
        zone_spinner_locationFault.setAdapter(locationAdapter);
        zone_spinner_globeFault.setAdapter(buildingAdapter);

        getBuildingList(token, workspace);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateClusteringRadius();
        oms = new OverlappingMarkerSpiderfier(mMap);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isCluster()) {
                    if (mMap.getCameraPosition().zoom >= 15)
                        oms.spiderListener(marker);
                    else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                marker.getPosition(),
                                mMap.getCameraPosition().zoom + dynamicZoomLevel()));
                        updateClusteringRadius();
                    }
                    return true;
                }
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(this);
    }

    private void addDemoMarkersAround(GoogleMap map, List<LatLng> centerList, List<String> titleList) {
        MarkerOptions options = new MarkerOptions();
        Random r = new Random();
        for (int k = 0; k < centerList.size(); k++) {

            double centerLatitude = centerList.get(k).latitude;
            double centerLongitude = centerList.get(k).longitude;
            marker = map.addMarker(options.title(titleList.get(k).toString()).position(new LatLng(centerLatitude + r.nextGaussian() * 0.0002,
                    centerLongitude + r.nextGaussian() * 0.0002)).clusterGroup(ClusterGroup.FIRST_USER));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerList.get(k).latitude, centerList.get(k).longitude), 15));
        }
    }

    private float dynamicZoomLevel() {
        float currZoomLvl = mMap.getCameraPosition().zoom;
        final float minZoomStepAtZoom = 17.3F, minZoomStep = 1.8F;
        final float maxZoomStepAtZoom = 7F, maxZoomStep = 2.8F;

        if (currZoomLvl >= minZoomStepAtZoom)
            return minZoomStep;
        else if (currZoomLvl <= maxZoomStepAtZoom)
            return maxZoomStep;
        else
            return (currZoomLvl - maxZoomStepAtZoom)
                    * (maxZoomStep - minZoomStep)
                    / (maxZoomStepAtZoom - minZoomStepAtZoom) + maxZoomStep;
    }

    private int clusterRadiusCalculation() {
        final int minRad = 0, maxRad = 150;
        final float minRadZoom = 10F, maxRadZoom = 7.333F;

        if (mMap.getCameraPosition().zoom >= minRadZoom) {

            return minRad;

        } else if (mMap.getCameraPosition().zoom <= maxRadZoom)
            return maxRad;
        else
            return (int) (maxRad - (maxRadZoom - mMap.getCameraPosition().zoom) *
                    (maxRad - minRad) / (maxRadZoom - minRadZoom));
    }

    private void updateClusteringRadius() {
        if (clusterSettings == null) {
            clusterSettings = new ClusteringSettings();
            clusterSettings.addMarkersDynamically(true);
            clusterSettings.clusterSize(clusterRadiusCalculation());

            ClusterOptionsProvider provider = new ClusterOptionsProvider() {
                @Override
                public ClusterOptions getClusterOptions(List<Marker> markers) {
                    float hue;
                    switch (markers.get(0).getClusterGroup()) {
                        case ClusterGroup.FIRST_USER:
                            hue = BitmapDescriptorFactory.HUE_ORANGE;
                            break;
                        case ClusterGroup.DEFAULT:
                            hue = BitmapDescriptorFactory.HUE_GREEN;
                            break;
                        default:
                            hue = BitmapDescriptorFactory.HUE_ROSE;
                            break;
                    }
                    BitmapDescriptor defaultIcon = BitmapDescriptorFactory.defaultMarker(hue);
                    return new ClusterOptions().icon(defaultIcon);
                }
            };
            mMap.setClustering(clusterSettings.clusterOptionsProvider(provider));
        } else if (mMap.getCameraPosition().zoom > 13F) {

        } else {
            clusterSettings.clusterSize(clusterRadiusCalculation());
        }
    }

    private void getBuildingList(String token, String workspace) {

        Call<List<GetBuildingsResponse>> call = APIClient.getUserServices().getBuildingList(token, workspace);
        call.enqueue(new Callback<List<GetBuildingsResponse>>() {
            @Override
            public void onResponse(Call<List<GetBuildingsResponse>> call, Response<List<GetBuildingsResponse>> response) {
                buildingIdList.add(1);
                if (response.code() == 200) {
                    buildingList.clear();
                    buildingList.add("Select the building");
                    buildingIdList.clear();
                    buildingIdList.add(1);
                    for (GetBuildingsResponse getBuildingsResponse : response.body()) {
                        if (getBuildingsResponse.getBuild_name() != null) {
                            buildingList.add(getBuildingsResponse.getBuild_name());
                            zone_spinner_globeFault.setAdapter(buildingAdapter);
                            buildingIdList.add(getBuildingsResponse.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetBuildingsResponse>> call, Throwable t) {
            }
        });

        zone_spinner_globeFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String buildingName = zone_spinner_globeFault.getSelectedItem().toString();
                int index = buildingList.indexOf(buildingName);
                if (buildingIdList.isEmpty()) {
                    Log.d(TAG, "onItemSelected: ");
                } else {
                    buildingIdInt = buildingIdList.get(index);
                }
                getLocationBuilding(buildingIdInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                buildingIdList.clear();
            }
        });

    }

    private void getLocationBuilding(int buildingIdInt) {

        String buildindIdString = String.valueOf(buildingIdInt);
        Call<ArrayList<BuildingLocationResponse>> listCallLocation = APIClient.getUserServices().buildingLocationCall(buildindIdString, workspace, token);

        listCallLocation.enqueue(new Callback<ArrayList<BuildingLocationResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<BuildingLocationResponse>> call, Response<ArrayList<BuildingLocationResponse>> response) {
                if (response.code() == 200) {

                    locationIdList.clear();
                    locationIdList.add(1);
                    List<BuildingLocationResponse> list = response.body();
                    locationList.clear();
                    locationList.add("Select the location");

                    for (int i = 0; i < list.size(); i++) {
                        String locationName = list.get(i).getName();
                        locationList.add(locationName);
                        zone_spinner_locationFault.setAdapter(locationAdapter);
                        int locId = list.get(i).getId();
                        locationIdList.add(locId);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BuildingLocationResponse>> call, Throwable t) {
                Toast.makeText(GlobeActivity.this, "Failed! Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        zone_spinner_locationFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String locationName = zone_spinner_locationFault.getSelectedItem().toString();
                locationIdList.add(1);
                int locId = locationList.indexOf(locationName);
                if (locationIdList.isEmpty()) {
                    Log.d(TAG, "onItemSelected: This is empty");
                } else {
                    locationIdInt = locationIdList.get(locId);
                    String locIdString = String.valueOf(locationIdInt);
                    if (locIdString.equals("1")) {
                        Log.d(TAG, "onItemSelected: Can't proceed with location id as 1");
                    } else {
                        loadMap(buildindIdString, locIdString, token, workspace);
                        mProgress.show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void loadMap(String buildingIdString, String locIdString, String token, String workspace) {
        latlogofTaskLoc.clear();
        tn0_List.clear();

        Call<JsonObject> call = APIClient.getUserServices().getMap(buildingIdString, locIdString, token, workspace);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    mProgress.dismiss();

                    JsonObject jsonObject = response.body();
                    assert jsonObject != null;
                    JsonArray jsonArray = jsonObject.getAsJsonArray("tasklocationlist");
                    JsonObject jo = null;
                    double latadd = 0.000004, lonadd = 0.000002;

                    for (int i = 0; i < jsonArray.size(); i++) {
                        latadd = latadd + 0.0003;
                        lonadd = lonadd + 0.004;
                        jo = jsonArray.get(i).getAsJsonObject();
                        if (jo != null) {
                            String tno = jo.get("tno").getAsString();
                            tn0_List.add(tno);

                            JsonElement lat = jo.get("lat");
                            JsonElement lng = jo.get("lng");

                            String l = lat.toString();
                            String lo = lng.toString();

                            if (!l.equals("null") & !lo.equals("null")) {
                                double la = Double.parseDouble(l);
                                double ln = Double.parseDouble(lo);
                                pointsOfTask = new LatLng(la, ln);
                                latlogofTaskLoc.add(pointsOfTask);
                            }
                        }
                    }

                    JsonArray jsonArrayFaultList = jsonObject.getAsJsonArray("faultreportloclist");
                    JsonObject jsonObjectoffault = null;

                    for (int j = 0; j < jsonArrayFaultList.size(); j++) {
                        jsonObjectoffault = jsonArrayFaultList.get(j).getAsJsonObject();
                        if (jsonObjectoffault != null) {
                            String frId = jsonObjectoffault.get("frId").getAsString();
                            frid_List.add(frId);

                            JsonElement lat = jsonObjectoffault.get("lat");
                            JsonElement lng = jsonObjectoffault.get("lng");

                            String l = lat.toString();
                            String lo = lng.toString();

                            if (!l.equals("null") & !lo.equals("null")) {
                                double la = Double.parseDouble(l);
                                double ln = Double.parseDouble(lo);
                                pointsofFault = new LatLng(la, ln);
                                latlogofFaultLoc.add(pointsofFault);
                            }
                        }
                    }
                }

                showFaultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMap.clear();
                        addDemoMarkersAround(mMap, latlogofFaultLoc, frid_List);
                    }
                });

                showTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMap.clear();
                        addDemoMarkersAround(mMap, latlogofTaskLoc, tn0_List);
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(GlobeActivity.this, "Failed to load  Map", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
                mProgress.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapViewFault.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewFault.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapViewFault.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapViewFault.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewFault.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapViewFault.onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewFault.onLowMemory();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker.getTitle().substring(0, 3).equals("PMT")) {
            Intent intent = new Intent(GlobeActivity.this, PmTaskActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("workspace", workspace);
            intent.putExtra("username", user);
            intent.putExtra("taskNumber", marker.getTitle());
            startActivity(intent);
        } else {
            Intent intent = new Intent(GlobeActivity.this, EditFaultReportActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("workspace", workspace);
            intent.putExtra("user", user);
            intent.putExtra("frId", marker.getTitle());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.admin).setTitle("Hello : " + user);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutmenu) {

            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            LogoutService logoutService=new LogoutService();
            logoutService.callForLogout(GlobeActivity.this,user);
        }
        return true;
    }

}
