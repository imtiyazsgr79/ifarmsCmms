package in.ifarms.com.TaskSearch;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ifarms.com.APIClient;
import in.ifarms.com.FaultReportActivity.BuildingLocationResponse;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class TaskSearchActivity extends AppCompatActivity {
    private Spinner zone_spiner, location_spiner;
    private String token, workspace;
    private String TAG;
    private RecyclerView recyclerView;
    private List<String> spinnerList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    TextView z;
    private List<String> buildingList = new ArrayList<>();
    private List<Integer> buildingIdList = new ArrayList<>();
    private ArrayAdapter<String> buildingAdapter;
    private List<String> locationList = new ArrayList<>();
    private List<Integer> locationIdList = new ArrayList<>();
    private ArrayAdapter<String> locationAdapter;
    private int buildingIdInt, locationIdInt;
    private String tNoString, locationString;
    private ArrayList<TaskCardDetails> taskCardAdapterArrayList = new ArrayList<>();
    private TasksRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog mProgress;
    private Toolbar toolbar;
    private String user;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutmenu) {
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            LogoutService logoutService=new LogoutService();
            logoutService.callForLogout(TaskSearchActivity.this,user);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_search);

        toolbar = findViewById(R.id.toolbar_taskSearch);
        setSupportActionBar(toolbar);

        zone_spiner = findViewById(R.id.spiner_zoneTask);
        location_spiner = findViewById(R.id.spiner_locationTask);
        recyclerView = findViewById(R.id.list_view_tast_search);
        Toolbar toolbar = findViewById(R.id.toolbar_taskSearch);
        locationList.add("Select the location");
        buildingList.add("Select the building");

        mProgress = new ProgressDialog(TaskSearchActivity.this);
        mProgress.setTitle("Searching Tasks...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, buildingList);
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationList);
        location_spiner.setAdapter(locationAdapter);
        zone_spiner.setAdapter(buildingAdapter);


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("worklist");
        user = intent.getStringExtra("username");

        //get building list
        getBuildingList(token, workspace);
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
                            zone_spiner.setAdapter(buildingAdapter);
                            buildingIdList.add(getBuildingsResponse.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetBuildingsResponse>> call, Throwable t) {
            }
        });

        zone_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String buildingName = zone_spiner.getSelectedItem().toString();
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
                        location_spiner.setAdapter(locationAdapter);
                        int locId = list.get(i).getId();
                        locationIdList.add(locId);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BuildingLocationResponse>> call, Throwable t) {

                Toast.makeText(TaskSearchActivity.this, "Failed to load Location List", Toast.LENGTH_SHORT).show();
            }
        });

        location_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String locationName = location_spiner.getSelectedItem().toString();
                locationIdList.add(1);
                int locId = locationList.indexOf(locationName);
                if (locationIdList.isEmpty()) {

                } else {
                    locationIdInt = locationIdList.get(locId);
                    String locIdString = String.valueOf(locationIdInt);
                    if (locIdString.equals("1")) {
                        recyclerView.setAdapter(null);
                        taskCardAdapterArrayList.clear();

                    } else
                        loadTaskZone(buildindIdString, locIdString, token, workspace);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadTaskZone(String bldgId, String locId, String token, String workspace) {

        recyclerView.setAdapter(null);
        taskCardAdapterArrayList.clear();
        mProgress.show();
        Call<ArrayList<TaskSearchResponse>> call = APIClient.getUserServices().getTask(bldgId, locId, token, workspace);

        call.enqueue(new Callback<ArrayList<TaskSearchResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TaskSearchResponse>> call, Response<ArrayList<TaskSearchResponse>> response) {
                if (response.body().isEmpty()) {
                    Toast.makeText(TaskSearchActivity.this, "No Records Found", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<TaskSearchResponse> arrayList = response.body();
                    for (int i = 0; i < arrayList.size(); i++) {
                        tNoString = arrayList.get(i).getTno();
                        locationString = arrayList.get(i).getLocation();

                        taskCardAdapterArrayList.add(new TaskCardDetails(tNoString, locationString));
                        recyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(TaskSearchActivity.this);
                        mAdapter = new TasksRecyclerAdapter(taskCardAdapterArrayList);

                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new TasksRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(getApplicationContext(), PmTaskActivity.class);
                                intent.putExtra("token", token);
                                intent.putExtra("workspace", workspace);
                                intent.putExtra("taskNumber", arrayList.get(position).getTno());
                                intent.putExtra("username", user);
                                startActivity(intent);
                            }
                        });
                    }
                }
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<TaskSearchResponse>> call, Throwable t) {
                Toast.makeText(TaskSearchActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle("Hello : " + user);
        return true;
    }
}