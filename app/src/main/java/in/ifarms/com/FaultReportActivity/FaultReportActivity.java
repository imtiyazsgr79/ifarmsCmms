package in.ifarms.com.FaultReportActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import in.ifarms.com.Search.BeforePictureActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class FaultReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText requestorNameEditText, contactNumberEditText, locationDescriptionEditText, faultDescriptionEditText;
    private Spinner departmentSpinner, buildingSpinner, locationSpinner, prioritySpinner, divisionSpinner, faultCategorySpinner, selectMaintenanceSpinner;
    private Button buttonCreateFaultReport;
    private TextView datePickerEdit, timePickerEdit;
    private int tHour, tMinute;
    private static final String TAG = "FaultReportActivity";
    private JSONObject jsonBuildingObject = null;
    private String buildingName;
    private Integer buildingId = null;
    private String faultCategoryName, faultCategorySpinnerName = "";
    private String departmentSpinnerName, prioritySpinnerName = "";
    private String locationNameString, selectMaintenanceSpinnerName = "";
    private int locationIdNameInt, locationIdInt;
    private Toolbar toolbar;
    private String divisionName;
    private String divisionSpinnerName;
    private Integer faultIdInt = null;
    private Integer deptIdInt = null;
    private Integer priorityIdInt = null;
    private Integer locationIdLongInt = null;
    private Integer buildingIdInt = null;
    private Integer mainIdInt = null;
    private Integer divisionIdInt = null;
    private String receivedTokenfromRecycler;
    private String receivedValue, user;

    private List<Integer> deptListInt = new ArrayList<>();
    private List<Integer> buildingIdList = new ArrayList<>();
    private List<Integer> locationListId = new ArrayList<>();
    private List<Integer> priorityListInt = new ArrayList<>();
    private List<Integer> faultCategoryListInt = new ArrayList<>();
    private List<Integer> mainGrpListInt = new ArrayList<>();
    private List<Integer> divisionViewListInt = new ArrayList<>();

    private ProgressDialog progressDialog;

    List<String> deptNameList = new ArrayList<>();
    private List<String> buildingNameList = new ArrayList<>();
    private List<String> locationList = new ArrayList<>();
    List<String> priorityNameList = new ArrayList<>();
    List<String> faultNameList = new ArrayList<>();
    List<String> mainGrpNameList = new ArrayList<>();
    List<String> divisionNameList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_report);

        toolbar = findViewById(R.id.toolbar_fault);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        LinearLayout linearLayout = findViewById(R.id.line_layout);
        linearLayout.requestFocus();
        requestorNameEditText = findViewById(R.id.requestorNameEditText);
        contactNumberEditText = findViewById(R.id.contactNumber_fault);
        locationDescriptionEditText = findViewById(R.id.LocationDescriptionEditText);
        faultDescriptionEditText = findViewById(R.id.FaultDescriptionEditText);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        buildingSpinner = findViewById(R.id.buildingSpinner);
        locationSpinner = findViewById(R.id.locationSpinner);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        divisionSpinner = findViewById(R.id.divisionSpinner);
        faultCategorySpinner = findViewById(R.id.faultCategorySpinner);
        selectMaintenanceSpinner = findViewById(R.id.selectMaintenanceSpinner);
        datePickerEdit = findViewById(R.id.datePickerFault);
        timePickerEdit = findViewById(R.id.timePickerFault);
        buttonCreateFaultReport = findViewById(R.id.buttonCreateFaultReport);
        buttonCreateFaultReport.setEnabled(false);

        toolbar = findViewById(R.id.toolbar_fault);
        setSupportActionBar(toolbar);


        Intent receivedIntent = getIntent();
        Bundle bundle = receivedIntent.getExtras();
        user = receivedIntent.getStringExtra("username");
        receivedValue = (String) bundle.get("Workspace");
        receivedTokenfromRecycler = (String) bundle.get("token");


        deptNameList.add("Select Department");
        priorityNameList.add("Select Priority");
        mainGrpNameList.add("Select Maintenance Group");
        divisionNameList.add("Select Division");
        faultNameList.add("Select Fault Category");


        buildingNameList.add(0, "Select Building");
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, buildingNameList);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<Integer> faultCategoryIdList = new ArrayList<>();
        faultCategoryIdList.add(1);


        faultCategoryListInt.add(1);
        ArrayAdapter<String> faultCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, faultNameList);
        faultCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faultCategorySpinner.setAdapter(faultCategoryAdapter);


        deptListInt.add(1);
        ArrayAdapter<String> deptListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, deptNameList);
        deptListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(deptListAdapter);


        priorityListInt.add(1);
        ArrayAdapter<String> priorityListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorityNameList);
        priorityListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityListAdapter);


        mainGrpListInt.add(1);
        ArrayAdapter<String> mainGrpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mainGrpNameList);
        mainGrpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMaintenanceSpinner.setAdapter(mainGrpAdapter);

        divisionViewListInt.add(1);
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, divisionNameList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(divisionAdapter);

        locationList.add("Select Location");
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        Call<FaultReportResponse> listCall = APIClient.getUserServices().retrieveInfoInFaultReport(receivedTokenfromRecycler, receivedValue);

        listCall.enqueue(new Callback<FaultReportResponse>() {
            @Override
            public void onResponse(Call<FaultReportResponse> call, Response<FaultReportResponse> response) {
                if (response.isSuccessful()) {
                    FaultReportResponse post = response.body();


                    JSONArray jsonBuildingArray = new JSONArray(post.buildingList);
                    Log.d(TAG, "onResponse: building list " + jsonBuildingArray);
                    buildingIdList.add(1);
                    try {
                        for (int i = 0; i < jsonBuildingArray.length(); i++) {
                            jsonBuildingObject = jsonBuildingArray.getJSONObject(i);
                            buildingName = jsonBuildingObject.getString("name");
                            buildingNameList.add(buildingName);
                            String idBuilding = jsonBuildingObject.getString("id");
                            buildingIdList.add(Integer.valueOf(idBuilding));
                            buildingSpinner.setAdapter(buildingAdapter);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "The error message is : " + e.getMessage());
                    }

                    JSONArray faultCategoryArray = new JSONArray(post.faultCodeList);

                    for (int i = 0; i < faultCategoryArray.length(); i++) {
                        try {
                            JSONObject faultCategoryArrayJSONObject = faultCategoryArray.getJSONObject(i);
                            String faultCat = faultCategoryArrayJSONObject.getString("name");
                            faultNameList.add(faultCat);
                            faultCategoryName = faultCategoryArrayJSONObject.getString("id");
                            faultCategoryListInt.add(Integer.valueOf(faultCategoryName));
                            faultCategorySpinner.setAdapter(faultCategoryAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray deptListArray = new JSONArray(post.deptList);

                    String deptListName = "";
                    for (int i = 0; i < deptListArray.length(); i++) {
                        try {
                            JSONObject deptListObject = deptListArray.getJSONObject(i);
                            String deptStringName = deptListObject.getString("name");
                            deptNameList.add(deptStringName);
                            deptListName = deptListObject.getString("id");
                            deptListInt.add(Integer.valueOf(deptListName));
                            departmentSpinner.setAdapter(deptListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray divisionListArray = new JSONArray(post.divisions);
                    Log.d(TAG, "onResponse: division list " + divisionListArray);
                    divisionName = "";
                    for (int i = 0; i < divisionListArray.length(); i++) {
                        try {
                            JSONObject divisionObject = divisionListArray.getJSONObject(i);
                            String divisionStringName = divisionObject.getString("name");
                            divisionNameList.add(divisionStringName);
                            divisionName = divisionObject.getString("id");
                            divisionViewListInt.add(Integer.valueOf(divisionName));
                            divisionSpinner.setAdapter(divisionAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray priorityListArray = new JSONArray(post.priorityList);
                    String priorityListName = "";
                    for (int i = 0; i < priorityListArray.length(); i++) {
                        try {
                            JSONObject priorityObject = priorityListArray.getJSONObject(i);
                            String priorityStringName = priorityObject.getString("name");
                            priorityNameList.add(priorityStringName);
                            priorityListName = priorityObject.getString("id");
                            priorityListInt.add(Integer.valueOf(priorityListName));
                            prioritySpinner.setAdapter(priorityListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray maintGrpListArray = new JSONArray(post.maintGrpList);
                    String mainGrpName = "";
                    for (int i = 0; i < maintGrpListArray.length(); i++) {
                        try {
                            JSONObject mainGrpObject = maintGrpListArray.getJSONObject(i);
                            String mainGrpString = mainGrpObject.getString("name");
                            mainGrpNameList.add(mainGrpString);
                            mainGrpName = mainGrpObject.getString("id");
                            mainGrpListInt.add(Integer.valueOf(mainGrpName));
                            selectMaintenanceSpinner.setAdapter(mainGrpAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FaultReportResponse> call, Throwable t) {

                Toast.makeText(FaultReportActivity.this, "Retrieval failed", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });

        //Department spinner dropdown menu
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                departmentSpinnerName = departmentSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Building spinner dropdown menu
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String buildingIdName = buildingSpinner.getSelectedItem().toString();
                int index = buildingNameList.indexOf(buildingIdName);

                if (buildingIdList.isEmpty()) {
                    Log.d(TAG, "No response");
                } else {
                    buildingId = buildingIdList.get(index);

                    Call<ArrayList<BuildingLocationResponse>> listCallLocation = APIClient.getUserServices().buildingLocationCall(String.valueOf(buildingId), receivedValue, receivedTokenfromRecycler);
                    listCallLocation.enqueue(new Callback<ArrayList<BuildingLocationResponse>>() {
                        @Override
                        public void onResponse(Call<ArrayList<BuildingLocationResponse>> call, Response<ArrayList<BuildingLocationResponse>> response) {

                            if (response.code() == 200) {
                                Log.d(TAG, "onResponse: " + response.body());
                                locationNameString = "";
                                locationList.clear();
                                locationListId.clear();
                                locationList.add("Select Location");
                                locationListId.add(1);

                                List<BuildingLocationResponse> list = response.body();
                                if (buildingSpinner.getSelectedItem().toString().equals("Select Building")) {
                                    Log.d(TAG, "onResponse: ");
                                } else {
                                    for (int i = 0; i < list.size(); i++) {
                                        locationNameString = list.get(i).getName();
                                        locationList.add(locationNameString);
                                        locationIdNameInt = list.get(i).getId();
                                        locationListId.add(locationIdNameInt);
                                        Log.d(TAG, "onResponse: " + list.get(0).getName());
                                    }
                                    locationSpinner.setAdapter(locationAdapter);
                                }
                                buttonCreateFaultReport.setEnabled(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<BuildingLocationResponse>> call, Throwable t) {
                            Toast.makeText(FaultReportActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //LocationSpinner dropdown menu
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String locationSpinnerName = locationSpinner.getSelectedItem().toString();

                int index = locationList.indexOf(locationSpinnerName);

                if (!locationListId.isEmpty()) {
                    locationIdInt = locationListId.get(index);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //priority spinner dropdown menu
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prioritySpinnerName = prioritySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //division spinner dropdown menu
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                divisionSpinnerName = divisionSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //faultcategory spinner
        faultCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                faultCategorySpinnerName = faultCategorySpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //select maintenance spinner
        selectMaintenanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectMaintenanceSpinnerName = selectMaintenanceSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        datePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDailog();
            }
        });

        timePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        FaultReportActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tHour = i;
                                tMinute = i1;
                                String hour = pad(tHour);
                                String minute = pad(tMinute);

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, Integer.parseInt(hour), Integer.parseInt(minute));
                                timePickerEdit.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();
            }
        });


        buttonCreateFaultReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPost();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void createPost() throws ParseException {


        String reqName = requestorNameEditText.getText().toString();
        String faultDescriptionName = faultDescriptionEditText.getText().toString();
        String locationDescriptionName = locationDescriptionEditText.getText().toString();
        String userContactNumber = contactNumberEditText.getText().toString();

        if (!(userContactNumber.length() < 8)) {
            progressDialog.show();

            if (!buildingSpinner.equals("Select Building")) {
                int indexB = buildingSpinner.getSelectedItemPosition();
                buildingIdInt = (buildingIdList.get(indexB));
            }
            if (!faultCategorySpinnerName.equals("Select Fault Category")) {
                int index = faultCategorySpinner.getSelectedItemPosition();
                faultIdInt = ((faultCategoryListInt.get(index)));
            }
            if (!departmentSpinnerName.equals("Select Department")) {
                int indexDe = departmentSpinner.getSelectedItemPosition();
                deptIdInt = (deptListInt.get(indexDe));
            }
            if (!prioritySpinnerName.equals("Select priority")) {
                int indexPrior = prioritySpinner.getSelectedItemPosition();
                priorityIdInt = (priorityListInt.get(indexPrior));
            }

            if (!locationSpinner.equals("Select Location")) {
                int indexLoc = locationSpinner.getSelectedItemPosition();
                locationIdLongInt = locationListId.get(indexLoc);
            }
            if (!selectMaintenanceSpinnerName.equals("Select Maintenance Group")) {
                int indexMain = selectMaintenanceSpinner.getSelectedItemPosition();
                mainIdInt = (mainGrpListInt.get(indexMain));
            }
            if (!divisionSpinnerName.equals("Select Division")) {
                int indexDivision = divisionSpinner.getSelectedItemPosition();
                divisionIdInt = divisionViewListInt.get(indexDivision);
            }

            String currentTime = null;
            String currentwTime = timePickerEdit.getText().toString();
            if (!timePickerEdit.getText().toString().isEmpty()) {
                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                Date date = format1.parse(currentwTime);
                SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
                currentTime = format2.format(date);
            }

            String idateReported = datePickerEdit.getText().toString();
            String currentDate = null;
            if (!datePickerEdit.getText().toString().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
                currentDate = (sdf2.format(Objects.requireNonNull(sdf.parse(idateReported))));
            }

            CreateFaultRequest createFaultRequest = new CreateFaultRequest(
                    reqName,
                    deptIdInt,
                    userContactNumber,
                    currentDate,
                    priorityIdInt,
                    buildingIdInt,
                    locationIdLongInt,
                    locationDescriptionName,
                    faultIdInt,
                    faultDescriptionName,
                    mainIdInt,
                    currentTime,
                    divisionIdInt);

            Call<CreateReportResponse> listCreateButtonCall = APIClient.getUserServices().createFaultRequest(createFaultRequest, receivedTokenfromRecycler, receivedValue);

            listCreateButtonCall.enqueue(new Callback<CreateReportResponse>() {
                @Override
                public void onResponse(Call<CreateReportResponse> call, Response<CreateReportResponse> response) {
                    if (response.code() == 201) {
                        Toast.makeText(FaultReportActivity.this, "Fault Report created successfully", Toast.LENGTH_SHORT).show();
                        CreateReportResponse createReportResponse = response.body();
                        String frId = createReportResponse.frId.toString();
                        Intent uploadPictureIntent = new Intent(FaultReportActivity.this, BeforePictureActivity.class);
                        uploadPictureIntent.putExtra("token", receivedTokenfromRecycler);
                        uploadPictureIntent.putExtra("workspace", receivedValue);
                        uploadPictureIntent.putExtra("frId", frId);
                        uploadPictureIntent.putExtra("value", "Before");
                        uploadPictureIntent.putExtra("user", user);
                        startActivity(uploadPictureIntent);
                        finish();
                    } else if (response.code() == 500) {
                        Toast.makeText(FaultReportActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(FaultReportActivity.this, "Error: " + response.code() + ", " + response.message(), Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<CreateReportResponse> call, Throwable t) {
                    Toast.makeText(FaultReportActivity.this, "Failed : " + t.getCause(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else
            Toast.makeText(this, "Please Enter Valid Contact Number ", Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDailog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Date date = new Date(year - 1900, month, day);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String cdate = formatter.format(date);

        datePickerEdit.setText(cdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle("Hello : " + user);
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
            logoutService.callForLogout(FaultReportActivity.this,user);


        }
        return true;
    }

    public String pad(int input) {

        String str = "";

        if (input > 10) {

            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);

        }
        return str;
    }
}