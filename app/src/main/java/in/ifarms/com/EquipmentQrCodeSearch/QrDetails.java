package in.ifarms.com.EquipmentQrCodeSearch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import in.ifarms.com.Search.EditFaultReportActivity;
import in.ifarms.com.TaskSearch.PmTaskActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class QrDetails extends AppCompatActivity {

    private static final String TAG = "";
    private TextView t1, t2, t3, t4, t5, t6;
    private TextView faultListButton, taskListButton;
    private ProgressDialog mProgress;
    private ArrayList<String> frIdList = new ArrayList();
    private String token, workspace;
    private ListView listView, listView_qr;
    private String code;
    private ArrayList<String> tno_list = new ArrayList<>();
    private ArrayAdapter<String> taskListAdapter;
    private ArrayAdapter<String> frIdAdapter;
    private String user;
    private Toolbar toolbar;
    private String type;
    private String name;
    private String building;
    private String location;
    private String asset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_details);


        mProgress = new ProgressDialog(QrDetails.this);
        mProgress.setTitle("searching equipment...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        initViews();
        listView = findViewById(R.id.list_view_equip);
        faultListButton = findViewById(R.id.first_btn);
        taskListButton = findViewById(R.id.second_btn);
        toolbar = findViewById(R.id.toolbar_QRDetails);
        setSupportActionBar(toolbar);

        listView_qr = (ListView) findViewById(R.id.list_qr_view);
        listView.setAdapter(null);
        tno_list.clear();
        frIdList.clear();

        faultListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFaultReportEquip();
            }
        });
        taskListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();

            }
        });

    }

    private void dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(QrDetails.this).create(); //Read Update
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View radioLayoutView = layoutInflater.inflate(R.layout.custom_dialog_radio_layout_qr, null);
        RadioGroup radioGroup = radioLayoutView.findViewById(R.id.radio_grp_id);

        alertDialog.setView(radioLayoutView);
        alertDialog.setTitle("Select Type of Tasks");

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioSelectedButton = radioGroup.findViewById(selectedId);

                String status = radioSelectedButton.getText().toString();
                status = status.substring(0, status.length() - 6);
                loadTaskOnEq(status);
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void loadFaultReportEquip() {
        mProgress.show();
        mProgress.setTitle("Loading Fault Reports...");

        Call<JsonArray> call = APIClient.getUserServices().getfaultOnQrList(code, token, workspace);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mProgress.dismiss();
                if (response.code() == 200) {
                    mProgress.dismiss();
                    JsonArray jsonArray = response.body();
                    frIdAdapter = new ArrayAdapter<String>(QrDetails.this, android.R.layout.simple_list_item_1, frIdList);
                    listView.setAdapter(frIdAdapter);
                    if (jsonArray == null) {
                        Toast.makeText(QrDetails.this, "No Fault Report Available", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            String frid = String.valueOf(jsonObject.get("frId"));
                            frid = frid.replace("\"", "");

                            frIdList.add(frid);
                            listView.setVisibility(View.VISIBLE);
                        }
                        listView.setAdapter(frIdAdapter);
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String tno_string = frIdList.get(i);
                            Intent intent = new Intent(QrDetails.this, EditFaultReportActivity.class);
                            intent.putExtra("frId", tno_string);
                            intent.putExtra("token", token);
                            intent.putExtra("workspace", workspace);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(QrDetails.this, "failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadTaskOnEq(String s) {
        tno_list.clear();
        listView.setAdapter(taskListAdapter);
        mProgress.setTitle("Loading tasks...");
        mProgress.show();

        Call<JsonArray> call = APIClient.getUserServices().getTaskOnQrList(code, s, token, workspace);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mProgress.dismiss();
                if (response.code() == 200) {
                    mProgress.dismiss();
                    JsonArray jsonArray = response.body();
                    if (jsonArray == null) {
                        Toast.makeText(QrDetails.this, "No Tasks Available", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jo = jsonArray.get(i).getAsJsonObject();
                            String tno = String.valueOf(jo.get("task_number"));
                            tno = tno.replace("\"", "");
                            tno_list.add(tno);
                            listView.setVisibility(View.VISIBLE);

                            taskListAdapter = new ArrayAdapter<String>(QrDetails.this, android.R.layout.simple_list_item_1, tno_list);
                            listView.setAdapter(taskListAdapter);
                        }
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                String taskNumber = tno_list.get(i);

                                Intent intent = new Intent(QrDetails.this, PmTaskActivity.class);
                                intent.putExtra("taskNumber", taskNumber);
                                intent.putExtra("token", token);
                                intent.putExtra("workspace", workspace);
                                intent.putExtra("username", user);
                                startActivity(intent);
                            }
                        });

                    }
                }
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Log.d(TAG, "onFailure: " + t.getCause());
                mProgress.dismiss();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void initViews() {

        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("workspace");
        user = intent.getStringExtra("user");
        code = intent.getStringExtra("code");
        type = intent.getStringExtra("type");
        name = intent.getStringExtra("name");
        building = intent.getStringExtra("building");
        location = intent.getStringExtra("location");
        asset = intent.getStringExtra("asset");

        t1 = findViewById(R.id.eq_code);
        t2 = findViewById(R.id.eq_type);
        t3 = findViewById(R.id.eq_name);
        t4 = findViewById(R.id.eq_building);
        t5 = findViewById(R.id.eq_location);
        t6 = findViewById(R.id.eq_assetn0);

        t1.setText("Equipment code: " + code);
        t2.setText("Equipment type: " + type);
        t3.setText("Equipment name: " + name);
        t4.setText("Equipment building: " + building);
        t5.setText("Equipment location: " + location);
        t6.setText("Asset No: " + asset);

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
            logoutService.callForLogout(QrDetails.this,user);
        }
        return true;
    }
}