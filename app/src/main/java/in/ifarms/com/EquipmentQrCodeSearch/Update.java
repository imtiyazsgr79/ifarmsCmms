package in.ifarms.com.EquipmentQrCodeSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import in.ifarms.com.APIClient;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class Update extends AppCompatActivity {
    private static final String TAG = "";
    TextView t_no, t_frid, t_loc, t_build, t_acmv, t_shedule, t_date;
    EditText t_completedby, t_compdate, t_comptime, t_remarks;
    Spinner t_spiner;
    Button update;
    String tno, token, workspace, user;

    String task_no, remark, shedule_no, equip_code, loc, build, compdate, comptime, compby, status;
    long sheduledate;

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
            logoutService.callForLogout(Update.this,user);


        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent i = getIntent();
        user = i.getStringExtra("username");
        token = i.getStringExtra("token");
        workspace = i.getStringExtra("workspace");
        tno = i.getStringExtra("tno_list");
        tno = tno.replace("\"", "");

        initViews();
        loadToUpdateDetail();


    }

    private void loadToUpdateDetail() {

        Call<JsonObject> call = APIClient.getUserServices().getTaskupdateDetail(tno, token, workspace);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    Toast.makeText(Update.this, "sucess", Toast.LENGTH_SHORT).show();

                    JsonObject jsonObject = response.body();

                    task_no = String.valueOf(jsonObject.get("task_number"));
                    shedule_no = String.valueOf(jsonObject.get("pmScheduleNo"));
                    equip_code = String.valueOf(jsonObject.get("equipmentCode"));
                    build = String.valueOf(jsonObject.get("equipmentBuilding"));
                    loc = String.valueOf(jsonObject.get("equipmentLocation"));
                    status = String.valueOf(jsonObject.get("status"));
                    sheduledate = Long.parseLong(String.valueOf(jsonObject.get("scheduleDate")));
                    compdate = String.valueOf(jsonObject.get("compDate"));
                    comptime = String.valueOf(jsonObject.get("compTime"));
                    compby = String.valueOf(jsonObject.get("completedBy"));
                    shedule_no = String.valueOf(jsonObject.get("pmScheduleNo"));


                    task_no = task_no.replace("\"", "");
                    loc = loc.replace("\"", "");
                    status = status.replace("\"", "");
                    build = build.replace("\"", "");
                    equip_code = equip_code.replace("\"", "");
                    shedule_no = shedule_no.replace("\"", "");

                    t_no.setText(task_no);
                    t_frid.setText(shedule_no);
                    //  t_loc.setText(loc);
                    t_build.setText("Building: " + build);
                    t_loc.setText("Location: " + loc);
                    t_acmv.setText("Equipment code :" + equip_code);

                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(sheduledate);
                    String date = "Report Date: " + DateFormat.format("dd-mm-yyyy", cal).toString();

                    t_shedule.setText(date);
                    t_date.setText(compdate);
                   /* t_compdate.setText("Completed date: "+compdate);
                    t_comptime.setText("Completed Time: "+comptime);
                    t_completedby.setText("CompletedBy: "+compby);*/
                    t_remarks.setText(remark);


                    List<String> list = new ArrayList<String>();
                    list.add(status);

                    if (status.equals("OPEN")) {
                        list.add("CLOSED");
                    } else {
                        list.add("Open");
                    }


                    ArrayAdapter<List> adapter = new ArrayAdapter(Update.this, android.R.layout.simple_spinner_dropdown_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    t_spiner.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getCause());
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initViews() {

        t_no = findViewById(R.id.tno_update);
        t_frid = findViewById(R.id.tno_frid);
        t_loc = findViewById(R.id.tno_loc);
        t_build = findViewById(R.id.tno_build);
        t_acmv = findViewById(R.id.tno_acm);
        t_shedule = findViewById(R.id.tno_shedule);
        t_date = findViewById(R.id.tno_date);

        t_completedby = findViewById(R.id.tno_completedby);
        t_compdate = findViewById(R.id.tno_completeddate);
        t_comptime = findViewById(R.id.tno_completedtime);
        t_remarks = findViewById(R.id.tno_remarks);
        t_spiner = findViewById(R.id.tno_status);
        update = findViewById(R.id.tno_btn_update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BodyOfUpdate bodyOfUpdate = new BodyOfUpdate(task_no, status, compby, compdate, comptime);

                updatefile(bodyOfUpdate);
            }
        });


    }

    private void updatefile(BodyOfUpdate bodyOfUpdate) {

        Call<Void> call = APIClient.getUserServices().updateQrTask(bodyOfUpdate, token, workspace);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {

                    Toast.makeText(Update.this, "Successfully Upodated", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: sucess");
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });


    }
}