package in.ifarms.com.TaskSearch;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class PmTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String taskNumber, user, token, workspace;
    private TextView taskNumberTextView, scheduleNumberTextView, buildingNameTextView,
            locationNameTextView, equipmentNameTextView, briefDescTextView,
            scheduleDateTextView;
    private EditText remarksTextView, nameTextView;
    private Spinner statusSpinner;
    private Button buttonUpdate;
    private TextView datePickerEdit, timePickerEdit;
    private int tHour, tMinute;
    private ProgressDialog mProgress;
    private ArrayAdapter<String> statusSpinnerAdapter;
    private List<String> statusList = new ArrayList<>();
    String timeString, dateString, completedByString, taskNumberString, statusString, remarksString = "";
    private static final String TAG = "PmTaskActivity";
    private ProgressDialog updateProgress;
    private Toolbar toolbar;
    private Button checkListButton;
    private long scheduleDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_task);

        toolbar = findViewById(R.id.pmtool);
        setSupportActionBar(toolbar);

        taskNumberTextView = findViewById(R.id.textViewTaskNumberPm);
        scheduleNumberTextView = findViewById(R.id.textViewScheduleNumberPm);
        scheduleNumberTextView.setMovementMethod(new ScrollingMovementMethod());
        nameTextView = findViewById(R.id.namePmTasks);
        remarksTextView = findViewById(R.id.remarks_pmTasks);
        buildingNameTextView = findViewById(R.id.textViewBuildingNumberPm);
        locationNameTextView = findViewById(R.id.textViewLocationNumberPm);
        equipmentNameTextView = findViewById(R.id.textViewEquipmentPm);
        briefDescTextView = findViewById(R.id.textViewBriefDescriptionPm);
        scheduleDateTextView = findViewById(R.id.textViewScheduleDatePm);
        datePickerEdit = findViewById(R.id.date_picker_pmtasksPm);
        datePickerEdit.setEnabled(false);
        timePickerEdit = findViewById(R.id.time_picker_pmtasks);
        timePickerEdit.setEnabled(false);
        buttonUpdate = findViewById(R.id.buttonUpdateTaskPm);
        statusSpinner = findViewById(R.id.spinner_status_pmtasks);
        checkListButton = findViewById(R.id.buttonCheckList);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateTimeString = sdf.format(d);
        timePickerEdit.setText(currentDateTimeString);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        datePickerEdit.setText(sdf1.format(new Date()));


        mProgress = new ProgressDialog(PmTaskActivity.this);
        mProgress.setTitle("Retrieving data...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        updateProgress = new ProgressDialog(this);
        updateProgress.setTitle("Updating...");
        updateProgress.setCancelable(false);
        updateProgress.setIndeterminate(true);

        statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusList);
        statusSpinner.setAdapter(statusSpinnerAdapter);

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        taskNumber = intent.getStringExtra("taskNumber");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("workspace");


        getPmTaskItems();

        if (workspace.equals("PA-Bedok-Heartbeat-072019-001")) {
            datePickerEdit.setEnabled(true);
            timePickerEdit.setEnabled(true);
            getTimeAndDatePicker();

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date newDate = null;
                    try {
                        newDate = format.parse(datePickerEdit.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    format = new SimpleDateFormat("yyyy-MM-dd");
                    String date = format.format(newDate);

                    GetUpdatePmTaskRequest request = new GetUpdatePmTaskRequest(taskNumber,
                            statusSpinner.getSelectedItem().toString(),
                            date,
                            timePickerEdit.getText().toString(),
                            nameTextView.getText().toString(),
                            remarksTextView.getText().toString());
                    if (!nameTextView.getText().toString().isEmpty() && !remarksTextView.getText().toString().isEmpty()) {
                        updatePmTaskService(request);
                    } else
                        Toast.makeText(PmTaskActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateTaskMethod();
        }

        checkListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CheckListActivity.class);
                intent.putExtra("taskNumber", taskNumber);
                intent.putExtra("token", token);
                intent.putExtra("username", user);
                intent.putExtra("workspace", workspace);
                startActivity(intent);

            }
        });

    }

    private void updateTaskMethod() {


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                timeString = timePickerEdit.getText().toString();
                dateString = datePickerEdit.getText().toString();
                completedByString = nameTextView.getText().toString();
                statusString = statusSpinner.getSelectedItem().toString();
                remarksString = remarksTextView.getText().toString();

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date newDate = null;
                try {
                    newDate = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                format = new SimpleDateFormat("yyyy-MM-dd");
                String date = format.format(newDate);
                completedByString = nameTextView.getText().toString();
                statusString = statusSpinner.getSelectedItem().toString();

                GetUpdatePmTaskRequest getUpdatePmTaskRequest = new GetUpdatePmTaskRequest(taskNumber, statusString,
                        date, timeString, completedByString, remarksString);

                if (!nameTextView.getText().toString().isEmpty() && !remarksTextView.getText().toString().isEmpty()) {
                    LocalDate now = Instant.ofEpochMilli((long) scheduleDate).atZone(ZoneId.systemDefault()).toLocalDate();
                    if (!now.isBefore(LocalDate.now())) {
                        updatePmTaskService(getUpdatePmTaskRequest);
                    } else
                        Toast.makeText(PmTaskActivity.this, "Overdue tasks cannot be updated.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PmTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePmTaskService(GetUpdatePmTaskRequest getUpdatePmTaskRequest) {
        updateProgress.show();

        Call<GetUpdatePmTaskResponse> callTaskUpdate = APIClient.getUserServices().postPmTaskUpdate(token, workspace, getUpdatePmTaskRequest);

        callTaskUpdate.enqueue(new Callback<GetUpdatePmTaskResponse>() {
            @Override
            public void onResponse(Call<GetUpdatePmTaskResponse> call, Response<GetUpdatePmTaskResponse> response) {
                updateProgress.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(PmTaskActivity.this, "Task Updated", Toast.LENGTH_LONG).show();
                    finish();
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetUpdatePmTaskResponse> call, Throwable t) {
                Toast.makeText(PmTaskActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                updateProgress.dismiss();
            }
        });
    }


    private void getTimeAndDatePicker() {
        timePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        PmTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tHour = i;
                                tMinute = i1;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, tHour, tMinute);

                                timePickerEdit.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();
            }
        });

        datePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDailog();
            }
        });

    }

    private void getPmTaskItems() {

        mProgress.show();
        Call<GetPmTaskItemsResponse> callGetPmTasks = APIClient.getUserServices().getPmItemsTasks(taskNumber, token, workspace);

        callGetPmTasks.enqueue(new Callback<GetPmTaskItemsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<GetPmTaskItemsResponse> call, Response<GetPmTaskItemsResponse> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    GetPmTaskItemsResponse getPmTaskItemsResponse = response.body();
                    if (getPmTaskItemsResponse.getTaskNumber() != null) {
                        taskNumberTextView.setText(getPmTaskItemsResponse.getTaskNumber());
                    }
                    if (getPmTaskItemsResponse.getPmScheduleNo() != null) {
                        scheduleNumberTextView.setText(getPmTaskItemsResponse.getPmScheduleNo());
                    }
                    if (getPmTaskItemsResponse.getBriefDescription() != null) {
                        briefDescTextView.setText(getPmTaskItemsResponse.getBriefDescription());
                    }
                    if (getPmTaskItemsResponse.getEquipmentBuilding() != null) {
                        buildingNameTextView.setText(getPmTaskItemsResponse.getEquipmentBuilding());
                    }
                    if (getPmTaskItemsResponse.getEquipmentCode() != null) {
                        equipmentNameTextView.setText(getPmTaskItemsResponse.getEquipmentCode());
                    }
                    if (getPmTaskItemsResponse.getEquipmentLocation() != null) {
                        locationNameTextView.setText(getPmTaskItemsResponse.getEquipmentLocation());
                    }
                    if (getPmTaskItemsResponse.getScheduleDate() != 0) {
                        scheduleDate = (long) getPmTaskItemsResponse.getScheduleDate();
                        LocalDateTime date = Instant.ofEpochMilli((long) getPmTaskItemsResponse.getScheduleDate()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                        String dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        scheduleDateTextView.setText(dateStr);
                    }

                    if (getPmTaskItemsResponse.getCompDate() != 0) {
                        long compDate = (long) getPmTaskItemsResponse.getCompDate();
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(compDate);
                        String date = String.valueOf(DateFormat.format("dd-MM-yyyy ", cal));
                        datePickerEdit.setText(date);
                    }
                    if (getPmTaskItemsResponse.getCompTime() != 0) {

                    }
                    if (getPmTaskItemsResponse.getStatus() != null) {
                        statusList.add(getPmTaskItemsResponse.getStatus());
                        if (getPmTaskItemsResponse.getStatus().equals("OPEN")) {
                            statusList.add("CLOSED");
                        } else statusList.add("OPEN");
                        statusSpinner.setAdapter(statusSpinnerAdapter);
                    } else {
                        statusList.add("Status of Pm");
                        statusList.add("OPEN");
                        statusList.add("CLOSED");
                        statusSpinner.setAdapter(statusSpinnerAdapter);
                    }

                    if (getPmTaskItemsResponse.getRemarks() != null) {
                        remarksTextView.setText(getPmTaskItemsResponse.getRemarks());
                    }

                    if (getPmTaskItemsResponse.getCompletedBy() != null) {
                        nameTextView.setText(getPmTaskItemsResponse.getCompletedBy());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPmTaskItemsResponse> call, Throwable t) {
                Toast.makeText(PmTaskActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
                checkListButton.setEnabled(false);
            }
        });
    }

    private void showDatePickerDailog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        String date = i2 + "-" + (i1 + 1) + "-" + i;
        datePickerEdit.setText(date);
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
            logoutService.callForLogout(PmTaskActivity.this,user);
        }
        return true;
    }
}