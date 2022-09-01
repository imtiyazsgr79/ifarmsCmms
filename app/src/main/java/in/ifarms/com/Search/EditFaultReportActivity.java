package in.ifarms.com.Search;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.FaultReportActivity.BuildingLocationResponse;
import in.ifarms.com.FaultReportActivity.FaultReportResponse;
import in.ifarms.com.General.TechnicianListResponse;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class EditFaultReportActivity extends AppCompatActivity {

    private FloatingActionButton fab_main, fab_before, fab_after, fab_esc;
    private Boolean isMenuOpen = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 100f;
    private static final String TAG = "SearchResponseAdapter";
    private String workspaceString = "";
    private String timeS;
    private String tokenGen;
    private String frId, buildingNameString, costCenterString, departmentString, divisionString, faultString, mainGrpString, priorityString, statusString;
    private int buildingId, unitId;
    private int tHour, tMinute;
    private Spinner statusSpinner, technicianSpinner, costCenterSpinner, mainGrpSpinner,
            faultCategorySpinner, divisionSpinner, unitSpinner, buildingSpinner, priorityNumberSpinner, deptNumberSpinner;
    private TextView timePickerResponse, timePickerStart, timePickerEnd, datePickerResponse, datePickerStart, datePickerEnd;
    private String requestorName;
    private String remarksString, startTimeString;
    private String locationString, locDesc, faultDetailString, locationName;
    private long startDateLong;
    TextView reqNameEditText;
    private EditText observationEditText, faultDetailsEditText, locDescEditText,
            requestorNumberEditText, actionTakenEditText, diagnosisEditText, labourHoursEditText;
    private TextView equipmentTextView;
    private TextView frIdEditText;
    private TextView dateAddedTextView;
    private TextView timeAddedTextView;
    private Button selectEquipmentButton;
    private Button updateFaultReportButton;
    private LinearLayout mlayout;
    private Integer buildingIdInt, deptIdInt, faultCatoryId, priorIdInt, mainGrpIdInt, costCenterIdInt, divisionIdInt, locationId;
    private Integer attendedByIdInt = null;
    private List<String> deptList = new ArrayList<>();
    private List<String> priorityList = new ArrayList<>();
    private List<String> buildingList = new ArrayList<>();
    private List<String> unitList = new ArrayList<>();
    private List<Integer> unitIdList = new ArrayList<>();
    private List<String> divisionList = new ArrayList<>();
    private List<String> faultList = new ArrayList<>();
    private List<String> mainGrpList = new ArrayList<>();
    private List<String> costCenterList = new ArrayList<>();
    private List<String> technicianList = new ArrayList<>();
    private List<Integer> technicianListId = new ArrayList<>();
    private List<String> statusList = new ArrayList<>();
    private List<Integer> buildingIdList = new ArrayList<>();
    private List<Integer> deptIdList = new ArrayList<>();
    private List<Integer> faultCodeIdList = new ArrayList<>();
    private List<Integer> priorityIdList = new ArrayList<>();
    private List<Integer> mainGrpIdList = new ArrayList<>();
    private List<Integer> costCenterIdList = new ArrayList<>();
    private List<Integer> divisionIdList = new ArrayList<>();
    private List<String> remarksList = new ArrayList<>();
    private List<EditText> editTextList = new ArrayList<>();
    private int indexB, idBuildingCurrent, idCostCurrent, intDepartmentCurrent, intDivisionCurrent, intFaultCurrent, priorityCurrent, mainGrpCurrent;
    private ArrayAdapter<String> deptSpinnerAdapter;
    private ArrayAdapter<String> prioritySpinnerAdapter;
    private ArrayAdapter<String> buildingSpinnerAdapter;
    private ArrayAdapter<String> unitSpinnerAdapter;
    private ArrayAdapter<String> divisionSpinnerAdapter;
    private ArrayAdapter<String> faultCategorySpinnerAdapter;
    private ArrayAdapter<String> mainGrpSpinnerAdapter;
    private ArrayAdapter<String> costCenterSpinnerAdapter;
    private ArrayAdapter<String> technicianSpinnerAdapter;
    private ArrayAdapter<String> statusSpinnerAdapter;
    private ProgressDialog progressDialog;
    private String date, user, technicianString;
    private int remarksId;
    private int iaaa = 0;
    private EditText editText;
    private TextWatcher textWatcher;
    private String attendedbynameinCall;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_fault_report);

        Intent intent = getIntent();
        frId = intent.getStringExtra("frId");
        tokenGen = intent.getStringExtra("token");
        workspaceString = intent.getStringExtra("workspace");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        user = intent.getStringExtra("user");
        tokenGen = sharedPreferences.getString("token", "");
        user=sharedPreferences.getString("name","");

        Toolbar toolbar = findViewById(R.id.toolbar_edit_fault);
        setSupportActionBar(toolbar);


        deptList.clear();
        priorityList.clear();
        buildingList.clear();
        unitList.clear();
        divisionList.clear();
        faultList.clear();
        mainGrpList.clear();
        costCenterList.clear();
        technicianList.clear();
        technicianListId.clear();
        statusList.clear();
        buildingIdList.clear();
        deptIdList.clear();
        faultCodeIdList.clear();
        priorityIdList.clear();
        mainGrpIdList.clear();
        costCenterIdList.clear();
        divisionIdList.clear();
        remarksList.clear();

        progressDialog = new ProgressDialog(EditFaultReportActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading");
        progressDialog.setIndeterminate(true);


        updateFaultReportButton = findViewById(R.id.updateFaultReportButton);
        diagnosisEditText = findViewById(R.id.diagnosis);
        actionTakenEditText = findViewById(R.id.actionTaken);
        statusSpinner = findViewById(R.id.statusSpinner);
        technicianSpinner = findViewById(R.id.technicianSpinner);
        costCenterSpinner = findViewById(R.id.costCenter);
        mainGrpSpinner = findViewById(R.id.mainGrp);
        faultCategorySpinner = findViewById(R.id.faultCategory);
        divisionSpinner = findViewById(R.id.divisionNumberSpinner);
        unitSpinner = findViewById(R.id.unitNumber);
        buildingSpinner = findViewById(R.id.buildingNumber);
        priorityNumberSpinner = findViewById(R.id.priorityNumber);
        deptNumberSpinner = findViewById(R.id.dept_number);
        timePickerResponse = findViewById(R.id.timeResponse);
        timePickerEnd = findViewById(R.id.timeEnd);
        timePickerStart = findViewById(R.id.timeStart);
        datePickerEnd = findViewById(R.id.dateEnd);
        datePickerResponse = findViewById(R.id.dateResponse);
        datePickerStart = findViewById(R.id.dateStart);
        requestorNumberEditText = findViewById(R.id.contactNumber);
        frIdEditText = findViewById(R.id.frIdEditText);
        TextView jobTimeTextView = findViewById(R.id.timeJob);
        locDescEditText = findViewById(R.id.locationDescrip);
        labourHoursEditText = findViewById(R.id.labourHours);
        faultDetailsEditText = findViewById(R.id.faultDetails);
        reqNameEditText = findViewById(R.id.reqNameEditTextEditFault);
        equipmentTextView = findViewById(R.id.equipmentTextViewEditFault);
        dateAddedTextView = findViewById(R.id.dateAdded);
        timeAddedTextView = findViewById(R.id.timeAdded);
        Button plusbtn = findViewById(R.id.plusButton);
        Button deletebtn = findViewById(R.id.deleteButton);
        mlayout = findViewById(R.id.layout_remarks);
        TextView textView = new TextView(this);
        observationEditText = findViewById(R.id.observation);
        fab_main = findViewById(R.id.images_id);
        fab_before = findViewById(R.id.before_id);
        fab_after = findViewById(R.id.after_id);
        fab_esc = findViewById(R.id.esc_id);

        updateFaultReportButton.setEnabled(false);

        //TextWatcher
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        datePickerStart.addTextChangedListener(textWatcher);
        datePickerEnd.addTextChangedListener(textWatcher);
        datePickerResponse.addTextChangedListener(textWatcher);
        timePickerEnd.addTextChangedListener(textWatcher);
        timePickerStart.addTextChangedListener(textWatcher);
        timePickerResponse.addTextChangedListener(textWatcher);
        observationEditText.addTextChangedListener(textWatcher);
        actionTakenEditText.addTextChangedListener(textWatcher);


        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mlayout.getChildCount() < 6) {
                    remarksId++;
                    mlayout.addView(createNewEditText(remarksString, remarksId));
                }
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRemarks(view);
            }
        });
        selectEquipmentButton = findViewById(R.id.selectEquipmentButton);
        selectEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, EquipmentSearchActivity.class);
                intent.putExtra("token", tokenGen);
                intent.putExtra("workspace", workspaceString);
                startActivityForResult(intent, 1);
            }
        });


        //method adds the received data to all the spinners
        spinnerDataAlocation();

        //costCenter and Building list
        costCenterListGenerator();

        //method for floating action button
        initializeFab();

        //used to allocate date
        datePicker();

        //time for all the textViews
        timePickerMethod();

        //select equiptment button
        selectEquipmentMethod();

        //building spinner call
        buidlingSpinnerCallMethod();

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void checkFieldsForEmptyValues() {
        updateFaultReportButton.setEnabled(false);
        if (statusSpinner.getSelectedItem().toString().equals("open")) {
            datePickerStart.setEnabled(false);
            timePickerStart.setEnabled(false);
            datePickerEnd.setEnabled(false);
            timePickerEnd.setEnabled(false);

            buttonEnableMethod();

        } else if (statusSpinner.getSelectedItem().toString().equals("KIV")
                || statusSpinner.getSelectedItem().toString().equals("In Progress")) {

            datePickerStart.setEnabled(true);
            timePickerStart.setEnabled(true);
            timePickerResponse.setEnabled(true);
            datePickerResponse.setEnabled(true);

            if (!actionTakenEditText.getText().toString().isEmpty()
                    && !datePickerStart.getText().toString().isEmpty()
                    && !timePickerStart.getText().toString().isEmpty()) {
                buttonEnableMethod();
            }
        } else if (statusSpinner.getSelectedItem().toString().equals("Closed")) {
            datePickerStart.setEnabled(true);
            timePickerStart.setEnabled(true);
            datePickerEnd.setEnabled(true);
            timePickerEnd.setEnabled(true);
            timePickerResponse.setEnabled(true);
            datePickerResponse.setEnabled(true);
            if (!editTextList.isEmpty()) {

                editText = editTextList.get(0);
                if (!datePickerStart.getText().toString().isEmpty()
                        && !timePickerStart.getText().toString().isEmpty()
                        && !datePickerEnd.getText().toString().isEmpty()
                        && !timePickerEnd.getText().toString().isEmpty()
                        && !observationEditText.getText().toString().isEmpty()
                        && !actionTakenEditText.getText().toString().isEmpty()
                        && !editText.getText().toString().isEmpty()) {
                    buttonEnableMethod();
                }
            }
        }
    }

    private void buttonEnableMethod() {
        updateFaultReportButton.setEnabled(true);
        updateFaultReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateFaultReport();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void buidlingSpinnerCallMethod() {

        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String buildingIdName = buildingSpinner.getSelectedItem().toString();
                int index = buildingList.indexOf(buildingIdName);

                buildingId = buildingIdList.get(index);

                Call<ArrayList<BuildingLocationResponse>> listCallLocation = APIClient.getUserServices().buildingLocationCall(String.valueOf(buildingId), workspaceString, tokenGen);

                listCallLocation.enqueue(new Callback<ArrayList<BuildingLocationResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<BuildingLocationResponse>> call, Response<ArrayList<BuildingLocationResponse>> response) {

                        List<BuildingLocationResponse> list = response.body();

                        if (iaaa > 2) {
                            unitList.clear();
                            unitIdList.clear();
                            unitList.add(" ");
                            unitIdList.add(1);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            String listName = list.get(i).getName();
                            if (!listName.equals(locationString)) {
                                unitIdList.add(list.get(i).getId());
                                unitList.add(listName);
                            }
                        }
                        unitSpinner.setAdapter(unitSpinnerAdapter);
                        if (buildingSpinner.getSelectedItem().toString().equals(buildingNameString)) {
                            unitSpinner.setSelection(unitList.indexOf(locationString));
                        }
                        iaaa++;
                    }

                    @Override
                    public void onFailure(Call<ArrayList<BuildingLocationResponse>> call, Throwable t) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initializeFab() {

        fab_before.setAlpha(0f);
        fab_after.setAlpha(0f);
        fab_esc.setAlpha(0f);

        fab_before.setTranslationY(translationY);
        fab_after.setTranslationY(translationY);
        fab_esc.setTranslationY(translationY);


        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }

            }
        });

        fab_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, BeforePictureActivity.class);
                intent.putExtra("token", tokenGen);
                intent.putExtra("workspace", workspaceString);
                intent.putExtra("frId", frId);
                intent.putExtra("value", "Before");
                intent.putExtra("user", user);
                startActivity(intent);

            }
        });
        fab_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, BeforePictureActivity.class);
                intent.putExtra("token", tokenGen);
                intent.putExtra("workspace", workspaceString);
                intent.putExtra("frId", frId);
                intent.putExtra("user", user);
                intent.putExtra("value", "After");
                startActivity(intent);
//
            }
        });
        fab_esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
            }
        });

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fab_esc.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_before.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_esc.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_before.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void updateFaultReport() throws ParseException {

        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Updating");
        mProgressDialog.show();
        String buildingIdName = buildingSpinner.getSelectedItem().toString();
        int index = buildingList.indexOf(buildingIdName);
        if (buildingIdList.isEmpty()) {
            Toast.makeText(this, "Please select a building", Toast.LENGTH_SHORT).show();
        } else {
            buildingIdInt = buildingIdList.get(index);
        }
        String locationString = unitSpinner.getSelectedItem().toString();
        int index1 = unitList.indexOf(locationString);
        if (!locationString.equals("Location/Sub Zone")) {
            locationId = unitIdList.get(index1);
        }
        String reqName = reqNameEditText.getText().toString();
        String contactNumber = requestorNumberEditText.getText().toString();

        String deptIdName = deptNumberSpinner.getSelectedItem().toString();
        int deptIndex = deptList.indexOf(deptIdName);
        if (deptList.isEmpty()) {
            Toast.makeText(this, "Please select a department", Toast.LENGTH_SHORT).show();
        } else {
            deptIdInt = deptIdList.get(deptIndex);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        String dateReported = null;
        String responseDate = null;
        String startDate = null;
        String endDate = null;
        if (!dateAddedTextView.getText().toString().isEmpty()) {
            String idateReported = dateAddedTextView.getText().toString();
            dateReported = idateReported;
        }
        if (!datePickerResponse.getText().toString().isEmpty()) {
            String iresponseDate = datePickerResponse.getText().toString();
            responseDate = (sdf2.format(Objects.requireNonNull(sdf.parse(iresponseDate))));
        }
        if (!datePickerStart.getText().toString().isEmpty()) {
            String istartDate = datePickerStart.getText().toString();
            startDate = (sdf2.format(Objects.requireNonNull(sdf.parse(istartDate))));
        }
        if (!datePickerEnd.getText().toString().isEmpty()) {
            String iendDate = datePickerEnd.getText().toString();
            endDate = (sdf2.format(Objects.requireNonNull(sdf.parse(iendDate))));
        }

        String timeReported = null;
        String responseTime = null;
        String startTime = null;
        String endTime = null;


        if (!timeAddedTextView.getText().toString().isEmpty()) {
            String currentwTime = timeAddedTextView.getText().toString();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            Date date = format1.parse(currentwTime);
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            timeReported = format2.format(date);
        }
        if (!timePickerResponse.getText().toString().isEmpty()) {
            String currentwTime = timePickerResponse.getText().toString();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            Date date = format1.parse(currentwTime);
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            responseTime = format2.format(date);
        }

        if (!timePickerStart.getText().toString().isEmpty()) {
            String currentwTime = timePickerStart.getText().toString();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            Date date = format1.parse(currentwTime);
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            startTime = format2.format(date);
        }
        if (!timePickerEnd.getText().toString().isEmpty()) {
            String currentwTime = timePickerEnd.getText().toString();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            Date date = format1.parse(currentwTime);
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            endTime = format2.format(date);
        }
        String locationDesc = locDescEditText.getText().toString();

        String faultCategoryName = faultCategorySpinner.getSelectedItem().toString();
        int indexFault = faultList.indexOf(faultCategoryName);
        if (!faultList.isEmpty()) {
            faultCatoryId = faultCodeIdList.get(indexFault);
        }
        faultDetailString = faultDetailsEditText.getText().toString();

        String prior = priorityNumberSpinner.getSelectedItem().toString();
        int indexPrior = priorityList.indexOf(prior);
        if (!priorityList.isEmpty()) {
            priorIdInt = priorityIdList.get(indexPrior);
        }

        String mainGrp = mainGrpSpinner.getSelectedItem().toString();
        int indexMainGrp = mainGrpList.indexOf(mainGrp);
        if (!mainGrpList.isEmpty()) {
            mainGrpIdInt = mainGrpIdList.get(indexMainGrp);
        }

        String divisionString = divisionSpinner.getSelectedItem().toString();
        int divisionIndex = divisionList.indexOf(divisionString);
        if (divisionList.isEmpty()) {
            Toast.makeText(this, "Please add division", Toast.LENGTH_SHORT).show();
        } else {
            if (divisionIdList.get(divisionIndex) != 1) {
                divisionIdInt = divisionIdList.get(divisionIndex);
            }
            if (divisionIdList.get(divisionIndex) == 1) {
                divisionIdInt = null;
            }
        }
        String observerString = observationEditText.getText().toString();
        String diagnosesString = diagnosisEditText.getText().toString();
        String actionTakenString = actionTakenEditText.getText().toString();

        String costCenterS = costCenterSpinner.getSelectedItem().toString();
        int costCenterIndex = costCenterList.indexOf(costCenterS);
        if (costCenterList.isEmpty()) {
            Toast.makeText(this, "Please select cost center", Toast.LENGTH_SHORT).show();
        } else {
            if (costCenterIdList.get(costCenterIndex) != 1) {
                costCenterIdInt = costCenterIdList.get(costCenterIndex);
            } else costCenterIdInt = null;
        }

        String statusString = statusSpinner.getSelectedItem().toString();
        String equipmentString = equipmentTextView.getText().toString();


        String technicianStringId = technicianSpinner.getSelectedItem().toString();

        if (!technicianStringId.equals("Select Technician")) {
            Integer techIdIndex = technicianList.indexOf(technicianStringId);
            attendedByIdInt = technicianListId.get(techIdIndex);
        }
        if (!editTextList.isEmpty()) {
            for (int iRem = 0; iRem < editTextList.size(); iRem++) {
                String remarks1String = editTextList.get(iRem).getText().toString();
                remarksList.add(remarks1String);
            }
        } else remarksList = null;

        Integer equipIdNew = null;
        String eqString = null;
        if (!equipmentTextView.getText().toString().isEmpty()) {
            eqString = equipmentTextView.getText().toString();
        }
        EditFaultReportRequest editFaultReportRequest = new EditFaultReportRequest(frId,
                buildingIdInt,
                locationId,
                reqName,
                deptIdInt,
                contactNumber,
                dateReported,
                timeReported,
                responseDate,
                responseTime,
                startDate,
                startTime,
                endDate,
                endTime,
                locationDesc,
                faultCatoryId,
                faultDetailString,
                priorIdInt,
                mainGrpIdInt,
                divisionIdInt,
                observerString,
                diagnosesString,
                actionTakenString,
                costCenterIdInt,
                statusString,
                eqString,
                remarksList,
                attendedByIdInt);

        Call<UpdateFaultReportResponse> callEditFaultReport = APIClient.getUserServices().updateReport(editFaultReportRequest, tokenGen, workspaceString);
        callEditFaultReport.enqueue(new Callback<UpdateFaultReportResponse>() {
            @Override
            public void onResponse(Call<UpdateFaultReportResponse> call, Response<UpdateFaultReportResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(EditFaultReportActivity.this, "Fault Report updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 500) {
                    AlertDialog.Builder emptyDailog = new AlertDialog.Builder(EditFaultReportActivity.this);
                    emptyDailog.setTitle("Error: " + response.code() + ". Please fill all the required fields!");
                    emptyDailog.setIcon(R.drawable.ic_error);
                    emptyDailog.setPositiveButton("Ok", null);
                    emptyDailog.show();
                } else {
                    AlertDialog.Builder dailog = new AlertDialog.Builder(EditFaultReportActivity.this);
                    dailog.setTitle("Error: " + response.code());
                    dailog.setIcon(R.drawable.ic_error);
                    dailog.setPositiveButton("Ok", null);
                    dailog.show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateFaultReportResponse> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void costCenterListGenerator() {

        progressDialog.show();
        Call<FaultReportResponse> listCall = APIClient.getUserServices().retrieveInfoInFaultReport(tokenGen, workspaceString);

        listCall.enqueue(new Callback<FaultReportResponse>() {
            @Override
            public void onResponse(Call<FaultReportResponse> call, Response<FaultReportResponse> response) {
                if (response.code() == 200) {
                    FaultReportResponse post = response.body();
                    JSONArray jsonBuildingArray = new JSONArray(post.buildingList);
                    JSONArray jsonCostCenterArray = new JSONArray(post.costCenterList);

                    try {
                        for (int i = 0; i < jsonBuildingArray.length(); i++) {
                            JSONObject jsonBuildingObject = jsonBuildingArray.getJSONObject(i);
                            String buildingName = jsonBuildingObject.getString("name").trim();

                            if (!buildingName.equals(buildingNameString)) {
                                buildingList.add(buildingName);
                                String idBuilding = jsonBuildingObject.getString("id");
                                buildingIdList.add(Integer.valueOf(idBuilding));
                                indexB = buildingIdList.indexOf(idBuildingCurrent);
                                buildingSpinner.setAdapter(buildingSpinnerAdapter);
                            }
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "The error message is : " + e.getMessage());
                    }

                    JSONArray divisionListArray = new JSONArray(post.divisions);
                    String divisionName = "";
                    for (int i = 0; i < divisionListArray.length(); i++) {
                        try {
                            JSONObject divisionObject = divisionListArray.getJSONObject(i);
                            divisionName = divisionObject.getString("name");

                            if (!divisionName.equals(divisionString)) {
                                divisionList.add(divisionName);
                                int divisionIdCurrent = divisionObject.getInt("id");
                                divisionIdList.add(divisionIdCurrent);
                                divisionSpinner.setAdapter(divisionSpinnerAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray costCenterArray = new JSONArray(post.costCenterList);
                    for (int i = 0; i < jsonCostCenterArray.length(); i++) {
                        try {
                            JSONObject costCenterObject = costCenterArray.getJSONObject(i);
                            String costCenterName = costCenterObject.getString("name");

                            if (!costCenterName.equals(costCenterString)) {
                                costCenterList.add(costCenterName);
                                String costCenterIdString = costCenterObject.getString("id");
                                costCenterIdList.add(Integer.valueOf(costCenterIdString));
                                costCenterSpinner.setAdapter(costCenterSpinnerAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray faultCategoryArray = new JSONArray(post.faultCodeList);
                    for (int i = 0; i < faultCategoryArray.length(); i++) {
                        try {
                            JSONObject faultCategoryArrayJSONObject = faultCategoryArray.getJSONObject(i);
                            String faultCategoryName = faultCategoryArrayJSONObject.getString("name");
                            if (!faultCategoryName.equals(faultString)) {
                                String faultCategoryId = faultCategoryArrayJSONObject.getString("id");
                                faultList.add(faultCategoryName);
                                faultCategorySpinner.setAdapter(faultCategorySpinnerAdapter);
                                faultCodeIdList.add(Integer.valueOf(faultCategoryId));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray deptListArray = new JSONArray(post.deptList);
                    String deptListName = "";
                    for (int i = 0; i < deptListArray.length(); i++) {
                        try {
                            JSONObject deptListObject = deptListArray.getJSONObject(i);
                            deptListName = deptListObject.getString("name");

                            if (!deptListName.equals(departmentString)) {
                                deptList.add(deptListName);
                                String deptIdName = deptListObject.getString("id");
                                deptIdList.add(Integer.valueOf(deptIdName));
                                deptNumberSpinner.setAdapter(deptSpinnerAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray priorityListArray = new JSONArray(post.priorityList);
                    String priorityListName = "";
                    for (int i = 0; i < priorityListArray.length(); i++) {
                        try {
                            JSONObject priorityObject = priorityListArray.getJSONObject(i);
                            priorityListName = priorityObject.getString("name");

                            if (!priorityListName.equals(priorityString)) {
                                String priorId = priorityObject.getString("id");
                                priorityIdList.add(Integer.valueOf(priorId));
                                priorityList.add(priorityListName);
                                priorityNumberSpinner.setAdapter(prioritySpinnerAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray maintGrpListArray = new JSONArray(post.maintGrpList);
                    String mainGrpName = "";
                    for (int i = 0; i < maintGrpListArray.length(); i++) {
                        try {
                            JSONObject mainGrpObject = maintGrpListArray.getJSONObject(i);
                            mainGrpName = mainGrpObject.getString("name");

                            if (!mainGrpName.equals(mainGrpString)) {
                                String mainGrp = mainGrpObject.getString("id");
                                mainGrpIdList.add(Integer.valueOf(mainGrp));
                                mainGrpList.add(mainGrpName);
                                mainGrpSpinner.setAdapter(mainGrpSpinnerAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //Technician List
                    technicianAllGet();
                }
            }

            @Override
            public void onFailure(Call<FaultReportResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteRemarks(View view) {
        if (!editTextList.isEmpty()) {
            if (mlayout.getChildCount() > 1) {
                if (statusSpinner.getSelectedItem().toString().equals("Closed")) {
                    if (mlayout.getChildCount() > 2) {
                        mlayout.removeViewAt(mlayout.getChildCount() - 1);
                        int index = editTextList.size() - 1;
                        editTextList.remove(index);
                    }
                } else {
                    mlayout.removeViewAt(mlayout.getChildCount() - 1);
                    int index = editTextList.size() - 1;
                    editTextList.remove(index);
                }
            }
        }
    }

    @NotNull
    private TextView createNewEditText(String remarksString, int remarksId) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText = new EditText(this);
        editText.setId(remarksId);
        editText.setText(remarksString);
        editText.setLayoutParams(lparams);
        editText.setHint("add remarks");
        editText.setMinHeight(50);
        editText.setMaxWidth(mlayout.getWidth());
        editTextList.add(editText);
        String remarkSt = editText.getText().toString();
        editText.addTextChangedListener(textWatcher);
        return editText;
    }

    private void technicianAllGet() {

        Call<List<TechnicianListResponse>> callTechAllGet = APIClient.getUserServices().technicianCall(tokenGen, workspaceString);
        callTechAllGet.enqueue(new Callback<List<TechnicianListResponse>>() {
            @Override
            public void onResponse(Call<List<TechnicianListResponse>> call, Response<List<TechnicianListResponse>> response) {

                List<TechnicianListResponse> technicianListResponse = response.body();

                for (int i = 0; i < technicianListResponse.size(); i++) {
                    String techName = technicianListResponse.get(i).getName();
                    int techId = technicianListResponse.get(i).getId();
                    technicianList.add(techName);
                    technicianListId.add(techId);
                }
                technicianSpinner.setAdapter(technicianSpinnerAdapter);
                //this method generates all the field and spinner values that are placed in the respective fields whenever the activity starts
                getEditSearchBody(frId);

            }

            @Override
            public void onFailure(Call<List<TechnicianListResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectEquipmentMethod() {
        selectEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ActivityTwoRequestCode = "1";

                Intent intent = new Intent(EditFaultReportActivity.this, EquipmentSearchActivity.class);
                intent.putExtra("token", tokenGen);
                intent.putExtra("workspace", workspaceString);
                startActivityForResult(intent, Integer.parseInt(ActivityTwoRequestCode));
            }
        });
    }

    private void spinnerDataAlocation() {

        buildingList.add(" ");
        buildingIdList.add(1);
        priorityIdList.add(1);
        deptIdList.add(1);
        unitIdList.add(1);
        divisionIdList.add(1);
        faultCodeIdList.add(1);
        mainGrpIdList.add(1);
        costCenterIdList.add(1);
        technicianListId.add(1);
        priorityList.add(" ");
        deptList.add(" ");
        unitList.add(" ");
        divisionList.add("Select Division");
        faultList.add(" ");
        mainGrpList.add(" ");
        costCenterList.add("Select Cost Center");
        technicianList.add("Select Technician");
        statusList.add(" ");

        statusList.add("open");
        statusList.add("Closed");
        statusList.add("KIV");
        statusList.add("In Progress");

        deptSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, deptList);
        prioritySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorityList);
        buildingSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, buildingList);
        unitSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, unitList);
        divisionSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, divisionList);
        faultCategorySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, faultList);
        mainGrpSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mainGrpList);
        costCenterSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, costCenterList);
        technicianSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, technicianList);
        statusSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statusList);

        deptNumberSpinner.setAdapter(deptSpinnerAdapter);
        priorityNumberSpinner.setAdapter(prioritySpinnerAdapter);
        buildingSpinner.setAdapter(buildingSpinnerAdapter);
        unitSpinner.setAdapter(unitSpinnerAdapter);
        divisionSpinner.setAdapter(divisionSpinnerAdapter);
        faultCategorySpinner.setAdapter(faultCategorySpinnerAdapter);
        mainGrpSpinner.setAdapter(mainGrpSpinnerAdapter);
        costCenterSpinner.setAdapter(costCenterSpinnerAdapter);
        technicianSpinner.setAdapter(technicianSpinnerAdapter);
        statusSpinner.setAdapter(statusSpinnerAdapter);

    }

    private void datePicker() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        View.OnClickListener showDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditFaultReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String cdate = formatter.format(date);

                        if (v.getId() == R.id.dateStart) //id of your StartDate button
                        {
                            datePickerStart.setText(cdate);
                        }
                        if (v.getId() == R.id.dateEnd) {
                            datePickerEnd.setText(cdate);
                        }
                        if (v.getId() == R.id.dateResponse) {
                            datePickerResponse.setText(cdate);
                        }

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        };

        datePickerStart.setOnClickListener(showDatePicker);
        datePickerResponse.setOnClickListener(showDatePicker);
        datePickerEnd.setOnClickListener(showDatePicker);
    }

    private void getEditSearchBody(String frId) {
        Call<EditFaultReportResponse> call = APIClient.getUserServices().getReport(frId, tokenGen, workspaceString);
        call.enqueue(new Callback<EditFaultReportResponse>() {
            @Override
            public void onResponse(Call<EditFaultReportResponse> call, Response<EditFaultReportResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    EditFaultReportResponse editFaultReportResponse = response.body();

                    deptNumberSpinner.setAdapter(deptSpinnerAdapter);
                    requestorName = editFaultReportResponse.reqtorName;
                    locDesc = editFaultReportResponse.locOtherDesc;

                    faultDetailString = editFaultReportResponse.faultDtl;
                    frIdEditText.setText(frId);
                    reqNameEditText.setText(requestorName);
                    locDescEditText.setText(locDesc);

                    faultDetailsEditText.setText(faultDetailString);

                    if (editFaultReportResponse.reqtorContactNo != null) {
                        requestorNumberEditText.setText(editFaultReportResponse.reqtorContactNo);
                    }
                    startDateLong = editFaultReportResponse.workspace.start_date;
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(startDateLong);
                    String report = (String) DateFormat.format("mm-dd-yyyy", cal);

                    if (editFaultReportResponse.startTime != null) {
                        if (!editFaultReportResponse.startTime.equals("null")) {
                            startTimeString = String.valueOf(editFaultReportResponse.startTime);
                            startTimeString = startTimeString.substring(0, 5);
                            timePickerStart.setText(startTimeString);
                        }
                    }

                    if (editFaultReportResponse.responseTime != null) {
                        if (!editFaultReportResponse.responseTime.equals("null")) {
                            String responseTimeString = editFaultReportResponse.responseTime;
                            responseTimeString = responseTimeString.substring(0, 5);
                            timePickerResponse.setText(responseTimeString);
                        }
                    }
                    if (editFaultReportResponse.reportedTime != null) {
                        if (!editFaultReportResponse.reportedTime.equals("null")) {
                            if (!editFaultReportResponse.reportedTime.equals("0")) {
                                String reportedTimeString = editFaultReportResponse.reportedTime;
                                reportedTimeString = reportedTimeString.substring(0, 5);
                                timeAddedTextView.setText(reportedTimeString);
                            }
                        }
                    }
                    if (editFaultReportResponse.endTime != null) {
                        if (!editFaultReportResponse.endTime.equals("null")) {
                            String endTimeString = String.valueOf(editFaultReportResponse.endTime);
                            endTimeString = endTimeString.substring(0, 5);
                            timePickerEnd.setText(endTimeString);
                        }
                    }

                    if (editFaultReportResponse.responseDate != 0) {
                        getDate(editFaultReportResponse.responseDate);
                        datePickerResponse.setText(date);
                    }

                    if (editFaultReportResponse.endDate != 0) {
                        getDate(editFaultReportResponse.endDate);
                        datePickerEnd.setText(date);
                    }
                    if (editFaultReportResponse.startDate != 0) {
                        getDate(editFaultReportResponse.startDate);
                        datePickerStart.setText(date);
                    }
                    if (editFaultReportResponse.createdDate != 0) {
                        getDate(editFaultReportResponse.createdDate);
                        dateAddedTextView.setText(date);
                    }


                    if (!editFaultReportResponse.bldgId.build_name.isEmpty()) {

                        buildingNameString = String.valueOf(editFaultReportResponse.bldgId.build_name).trim();

                        if (!buildingList.contains(buildingNameString)) {
                            buildingList.add(String.valueOf(editFaultReportResponse.bldgId.build_name).trim());
                            idBuildingCurrent = editFaultReportResponse.bldgId.id;
                            buildingIdList.add(idBuildingCurrent);
                            buildingSpinner.setAdapter(buildingSpinnerAdapter);
                        }
                        buildingSpinner.setSelection(buildingList.indexOf(buildingNameString));
                    }

                    if (editFaultReportResponse.costCenter != null) {
                        costCenterString = editFaultReportResponse.costCenter.costCenterID;

                        if (!costCenterList.contains(costCenterString)) {
                            costCenterList.add(costCenterString);
                            idCostCurrent = editFaultReportResponse.costCenter.id;
                            costCenterIdList.add(idCostCurrent);
                            costCenterSpinner.setAdapter(costCenterSpinnerAdapter);
                        }
                        costCenterSpinner.setSelection(costCenterList.indexOf(costCenterString));
                    }

                    if (editFaultReportResponse.division != null) {
                        divisionString = editFaultReportResponse.division.division_id;
                        if (!divisionList.contains(divisionString)) {
                            divisionList.add(divisionString);
                            intDivisionCurrent = editFaultReportResponse.division.id;
                            divisionIdList.add(intDivisionCurrent);
                            divisionSpinner.setAdapter(divisionSpinnerAdapter);
                        }
                        divisionSpinner.setSelection(divisionList.indexOf(divisionString));
                    }

                    if (editFaultReportResponse.faultCodeId != null) {
                        faultString = editFaultReportResponse.faultCodeId.faultCodeDesc;
                        if (!faultList.contains(faultString)) {
                            faultList.add(faultString);
                            intFaultCurrent = editFaultReportResponse.faultCodeId.id;
                            faultCodeIdList.add(intFaultCurrent);
                            faultCategorySpinner.setAdapter(faultCategorySpinnerAdapter);
                        }
                        faultCategorySpinner.setSelection(faultList.indexOf(faultString));
                    }

                    if (editFaultReportResponse.deptId != null) {
                        departmentString = editFaultReportResponse.deptId.dept_desc;
                        if (!deptList.contains(departmentString)) {
                            deptList.add(departmentString);
                            intDepartmentCurrent = editFaultReportResponse.deptId.id;
                            deptIdList.add(intDepartmentCurrent);
                            deptNumberSpinner.setAdapter(deptSpinnerAdapter);
                        }
                        deptNumberSpinner.setSelection(deptList.indexOf(departmentString));
                    }

                    if (editFaultReportResponse.priorityId != null) {
                        priorityString = editFaultReportResponse.priorityId.priDesc;
                        if (!priorityList.contains(priorityString)) {
                            priorityList.add(priorityString);
                            int intPriorityCurrent = editFaultReportResponse.priorityId.id;
                            priorityIdList.add(intPriorityCurrent);
                            priorityNumberSpinner.setAdapter(prioritySpinnerAdapter);
                        }
                        priorityNumberSpinner.setSelection(priorityList.indexOf(priorityString));
                    }

                    if (editFaultReportResponse.maintGrpId != null) {
                        mainGrpString = editFaultReportResponse.maintGrpId.mGrpDesc;
                        if (!mainGrpList.contains(mainGrpString)) {
                            mainGrpList.add(mainGrpString);
                            int intMainGrpCurrent = editFaultReportResponse.maintGrpId.id;
                            mainGrpIdList.add(intMainGrpCurrent);
                            mainGrpSpinner.setAdapter(mainGrpSpinnerAdapter);
                        }
                        mainGrpSpinner.setSelection(mainGrpList.indexOf(mainGrpString));
                    }

                    if (editFaultReportResponse.status != null) {
                        statusString = editFaultReportResponse.status;
                        if (!statusList.contains(statusString)) {
                            statusList.add(statusString);
                            statusSpinner.setAdapter(statusSpinnerAdapter);
                        }
                        statusSpinner.setSelection(statusList.indexOf(statusString));
                    }

                    if (editFaultReportResponse.locId.loc_desc != null) {
                        locationString = editFaultReportResponse.locId.loc_desc;
                        if (!unitList.contains(locationString)) {
                            unitList.add(locationString);
                            unitIdList.add(editFaultReportResponse.locId.id);
                            unitSpinner.setAdapter(unitSpinnerAdapter);
                        }
                        unitSpinner.setSelection(unitList.indexOf(locationString));
                    }

                    if (editFaultReportResponse.observe != null) {
                        observationEditText.setText(editFaultReportResponse.observe.toString());
                    }
                    if (editFaultReportResponse.actionTaken != null) {
                        actionTakenEditText.setText(editFaultReportResponse.actionTaken);
                    }
                    if (editFaultReportResponse.diagnosis != null) {
                        diagnosisEditText.setText(editFaultReportResponse.diagnosis.toString());
                    }
                    if (editFaultReportResponse.labourHrs != null) {
                        labourHoursEditText.setText(editFaultReportResponse.labourHrs.toString());
                    }
                    if (editFaultReportResponse.equipment != null) {
                        equipmentTextView.setText(editFaultReportResponse.equipment.equipmentCode);
                    }

                    if (editFaultReportResponse.attendedBy != null) {
                        technicianString = editFaultReportResponse.attendedBy.name;
                        if (!technicianList.contains(technicianString)) {
                            technicianList.add(technicianString);
                            technicianListId.add(editFaultReportResponse.attendedBy.id);
                            technicianSpinner.setAdapter(technicianSpinnerAdapter);
                        }
                        technicianSpinner.setSelection(technicianList.indexOf(technicianString));
                    }

                    if (editFaultReportResponse.remarks != null) {
                        for (int ik = 0; ik < editFaultReportResponse.remarks.size(); ik++) {
                            if (!editFaultReportResponse.remarks.get(ik).remarks.isEmpty()) {
                                String remString = editFaultReportResponse.remarks.get(ik).remarks;
                                int remId = editFaultReportResponse.remarks.get(ik).id;
                                mlayout.addView(createNewEditText(remString, remId));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EditFaultReportResponse> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String eqip = data.getStringExtra("equipment_code");
                int equipId = data.getIntExtra("equipmentId", 0);
                Log.d(TAG, "onActivityResult: " + equipId);
                equipmentTextView.setText(eqip);
            }
        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        cal.add(Calendar.DATE, 1);
        date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle("Hello: " + user);
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
            logoutService.callForLogout(EditFaultReportActivity.this,user);
        }
        return true;
    }


    public void timePickerMethod() {

        View.OnClickListener showTimePicker = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditFaultReportActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tHour = i;
                                tMinute = i1;

                                String hour = pad(tHour);
                                String minute = pad(tMinute);

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, Integer.parseInt(hour), Integer.parseInt(minute));
                                timeS = String.valueOf(DateFormat.format("HH:mm", calendar));

                                if (view.getId() == R.id.timeStart) {
                                    timePickerStart.setText(timeS);
                                }
                                if (view.getId() == R.id.timeEnd) {
                                    timePickerEnd.setText(timeS);
                                }
                                if (view.getId() == R.id.timeResponse) {
                                    timePickerResponse.setText(timeS);
                                }


                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();

            }
        };
        timePickerEnd.setOnClickListener(showTimePicker);
        timePickerStart.setOnClickListener(showTimePicker);
        timePickerResponse.setOnClickListener(showTimePicker);
    }
}