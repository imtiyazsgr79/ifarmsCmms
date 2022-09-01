package in.ifarms.com.QrFaultReportScan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

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

public class QrFaultRepotCreate extends AppCompatActivity {
    private TextView tvResquestorname, tvRD, tvRT, tvEC;
    private Button create;
    private Spinner spinnerB, spinnerL, spinnerP, spinnerD, spinnerF, spinnerM;
    private EditText etFaultD;
    private String user, b, l, code, token, workspace;
    private ArrayList<String> depList = new ArrayList<>();
    private ArrayList<PriorityPojo> priorityList = new ArrayList<>();
    private ArrayList<String> faultList = new ArrayList<>();
    private ArrayList<String> maintList = new ArrayList<>();
    private int priorityId;
    private String TAG;
    private Toolbar toolbar;
    private ArrayAdapter<String> faultAdapter, depAdapter, mainAdapter;
    private ArrayAdapter<PriorityPojo> priAdapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_fault_repot_create);
        toolbar = findViewById(R.id.qr_fault_tool);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        init();
        onSpinerListeners();
        getBuildingList();

        priorityList.add(new PriorityPojo(1, "Select Priority"));

        spinnerP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PriorityPojo p = (PriorityPojo) adapterView.getItemAtPosition(i);
                priorityId = p.id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });
    }

    private void createPost() {

        String equipcode = tvEC.getText().toString();
        String faultDescriptionName = etFaultD.getText().toString();

        if (priorityId != 1) {


            if (!(tvEC.getText().toString().isEmpty() || etFaultD.getText().toString().isEmpty())) {
                progressDialog.show();

                CreateFaultQrReport createFaultQrReport = new CreateFaultQrReport(
                        equipcode, priorityId, faultDescriptionName);

                Call<JsonObject> listCreateButton = APIClient.getUserServices().qRcreateFaultRequest(createFaultQrReport, token, workspace);
                listCreateButton.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 201) {
                            Toast.makeText(QrFaultRepotCreate.this, "Fault Report Created Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            JsonObject jsonObject = response.body();
                            assert jsonObject != null;
                            String frid = jsonObject.get("frId").getAsString();
                            frid = frid.replace("\"", "");
                            Intent uploadPictureIntent = new Intent(QrFaultRepotCreate.this, BeforePictureActivity.class);
                            uploadPictureIntent.putExtra("token", token);
                            uploadPictureIntent.putExtra("workspace", workspace);
                            uploadPictureIntent.putExtra("frId", frid);
                            uploadPictureIntent.putExtra("value", "Before");
                            uploadPictureIntent.putExtra("user", user);
                            startActivity(uploadPictureIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(QrFaultRepotCreate.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } else
            Toast.makeText(this, "Please fill fields", Toast.LENGTH_SHORT).show();
    }

    private void onSpinerListeners() {

        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Collections.singletonList(b));
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerB.setAdapter(buildingAdapter);
        spinnerB.setEnabled(false);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Collections.singletonList(l));
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerL.setAdapter(locationAdapter);
        spinnerL.setEnabled(false);

        depAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, depList);
        depAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerD.setAdapter(depAdapter);

        faultAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, faultList);
        depAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerF.setAdapter(faultAdapter);

        mainAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maintList);
        depAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerM.setAdapter(mainAdapter);

        priAdapter = new ArrayAdapter<PriorityPojo>(this, android.R.layout.simple_spinner_dropdown_item, priorityList);
        depAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerP.setAdapter(priAdapter);

    }

    private void getBuildingList() {

        Call<JsonObject> listCall = APIClient.getUserServices().getretrieveInfoInFaultReport(token, workspace);

        listCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    JsonObject object = response.body();

                    JsonArray jsonArray = object.getAsJsonArray("deptList");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String depname = jsonArray.get(i).getAsJsonObject().get("name").getAsString();
                        depList.add(depname);
                        spinnerD.setAdapter(depAdapter);
                    }
                    JsonArray jsonArraypriorty = object.getAsJsonArray("priorityList");
                    for (int i = 0; i < jsonArraypriorty.size(); i++) {
                        String priname = jsonArraypriorty.get(i).getAsJsonObject().get("name").getAsString();
                        Integer priId = jsonArraypriorty.get(i).getAsJsonObject().get("id").getAsInt();
                        priorityList.add(new PriorityPojo(priId, priname));
                        spinnerP.setAdapter(priAdapter);
                    }
                    JsonArray jsonArrayfault = object.getAsJsonArray("faultCodeList");
                    for (int i = 0; i < jsonArrayfault.size(); i++) {
                        String priname = jsonArrayfault.get(i).getAsJsonObject().get("name").getAsString();
                        faultList.add(priname);
                        spinnerF.setAdapter(faultAdapter);
                    }
                    JsonArray jsonArraymaint = object.getAsJsonArray("maintGrpList");
                    for (int i = 0; i < jsonArraymaint.size(); i++) {
                        String priname = jsonArraymaint.get(i).getAsJsonObject().get("name").getAsString();
                        maintList.add(priname);
                        spinnerM.setAdapter(mainAdapter);
                    }
                } else
                    Toast.makeText(QrFaultRepotCreate.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(QrFaultRepotCreate.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Report...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        tvResquestorname = findViewById(R.id.requestor_name);
        tvEC = findViewById(R.id.tv_code);
        tvRD = findViewById(R.id.tv_date);
        tvRT = findViewById(R.id.tv_time);
        create = findViewById(R.id.creat_btn_qr);
        spinnerB = findViewById(R.id.s_building);
        spinnerD = findViewById(R.id.s_dept);
        spinnerF = findViewById(R.id.s_fault);
        spinnerL = findViewById(R.id.s_location);
        spinnerM = findViewById(R.id.s_maint);
        spinnerP = findViewById(R.id.s_priority);
        etFaultD = findViewById(R.id.et_fault);


        depList.add("please select");
        maintList.add("please select");
        faultList.add("please select");

        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        l = intent.getStringExtra("location");
        code = intent.getStringExtra("equipment");
        b = intent.getStringExtra("building");
        user = intent.getStringExtra("username");
        workspace = intent.getStringExtra("workspace");

        tvResquestorname.setText(user);
        tvEC.setText(code);


        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        tvRD.setText(dateString);

        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm");
        String dateString1 = sdf1.format(date);
        tvRT.setText(dateString1);


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
            logoutService.callForLogout(QrFaultRepotCreate.this,user);
        }
        return true;
    }
}