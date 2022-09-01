package in.ifarms.com.General;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.ifarms.com.APIClient;
import in.ifarms.com.EquipmentQrCodeSearch.QrEquipmentSearchActivity;
import in.ifarms.com.FaultReportActivity.FaultReportActivity;
import in.ifarms.com.FaultReportSearch.FaultReportSearchActivity;
import in.ifarms.com.GIS.GlobeActivity;
import in.ifarms.com.LogoutClass;
import in.ifarms.com.LogoutService;
import in.ifarms.com.OcrDashboardItem.OcrActivity;
import in.ifarms.com.QrFaultReportScan.FrQrScan;
import in.ifarms.com.R;
import in.ifarms.com.Search.SearchActivity;
import in.ifarms.com.Setting.SettingActivity;
import in.ifarms.com.TaskSearch.TaskSearchActivity;
import in.ifarms.com.singpost.SingpostSeachActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class Dashboard extends AppCompatActivity {
    private String TAG;
    private String user;
    private String receivedValue;
    private String receivedTokenfromRecycler;
    private String deviceGCM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        LinearLayout l9FRQRCodeScan = findViewById(R.id.linear9);
        LinearLayout l1CreateFaultReport = findViewById(R.id.linear1);
        LinearLayout l2Search = findViewById(R.id.linear2);
        LinearLayout l3Globe = findViewById(R.id.linear3);
        LinearLayout l4TaskSearch = findViewById(R.id.linear4);
        LinearLayout l5FaultSearch = findViewById(R.id.linear5);
        LinearLayout l6EquipScan = findViewById(R.id.linear6);
        LinearLayout l7Settings = findViewById(R.id.linear7);
        LinearLayout l8 = findViewById(R.id.linear8);
        LinearLayout l10 = findViewById(R.id.linear10);
        LinearLayout linearLayoutDashboard = findViewById(R.id.linearLayoutDashboard);
        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);

        deviceGCM = getToken(this);

        Intent receivedIntent = getIntent();
        user = receivedIntent.getStringExtra("username");
        receivedValue = receivedIntent.getStringExtra("variable");
        receivedTokenfromRecycler = receivedIntent.getStringExtra("token");
        String role = receivedIntent.getStringExtra("role");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        user = sharedPreferences.getString("name", "");

        // for ifarms only
        if (!receivedValue.equals("CMMS-SINGPOST-102021-001")){
            l10.setVisibility(View.GONE);
        }

        if (role != null) {
            if (receivedValue.equals("STIE-EnterpriseSingapore-102019-001") && role.equals("Technician")) {
                l1CreateFaultReport.setVisibility(View.GONE);
                l2Search.setVisibility(View.GONE);
                l3Globe.setVisibility(View.GONE);
                l4TaskSearch.setVisibility(View.GONE);
                l5FaultSearch.setVisibility(View.GONE);
                l7Settings.setVisibility(View.GONE);
                l8.setVisibility(View.GONE);
                l9FRQRCodeScan.setVisibility(View.GONE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                l6EquipScan.setLayoutParams(params);
            }

            if (receivedValue.equals("STIE-EnterpriseSingapore-102019-001") && role.equals("Client")) {
                l1CreateFaultReport.setVisibility(View.GONE);
                l3Globe.setVisibility(View.GONE);
                l6EquipScan.setVisibility(View.GONE);
                l4TaskSearch.setVisibility(View.GONE);
                l8.setVisibility(View.GONE);
                l7Settings.setVisibility(View.GONE);

                LinearLayout d1 = findViewById(R.id.din1);
                LinearLayout d2 = findViewById(R.id.din5);
                linearLayoutDashboard.removeView(d1);
                linearLayoutDashboard.removeView(d2);
                d1.removeAllViews();
                d2.removeAllViews();

                LinearLayout a = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3);
                params.setMargins(16, 16, 16, 16);
                params.gravity = Gravity.CENTER_VERTICAL;
                a.setLayoutParams(params);
                a.setOrientation(LinearLayout.HORIZONTAL);
                a.addView(l9FRQRCodeScan);
                a.addView(l5FaultSearch);
                a.addView(l2Search);
                linearLayoutDashboard.addView(a);
            }


        }

        l1CreateFaultReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FaultReportActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("Workspace", receivedValue);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
        l2Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, SearchActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("worklist", receivedValue);
                intent.putExtra("username", user);

                startActivity(intent);

            }
        });
        l3Globe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, GlobeActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("worklist", receivedValue);
                intent.putExtra("username", user);

                startActivity(intent);

            }
        });

        l4TaskSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, TaskSearchActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("worklist", receivedValue);
                intent.putExtra("username", user);
                startActivity(intent);

            }
        });

        l5FaultSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FaultReportSearchActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("worklist", receivedValue);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });

        l6EquipScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, QrEquipmentSearchActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("worklist", receivedValue);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
        l7Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, SettingActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("worklist", receivedValue);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
        l8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, OcrActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("worklist", receivedValue);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
        l9FRQRCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FrQrScan.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("Worklist", receivedValue);
                intent.putExtra("deviceToken", deviceGCM);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
        l10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, SingpostSeachActivity.class);
                intent.putExtra("token", receivedTokenfromRecycler);
                intent.putExtra("Worklist", receivedValue);
                intent.putExtra("deviceToken", deviceGCM);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
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
            LogoutService logoutService = new LogoutService();
            logoutService.callForLogout(Dashboard.this, user);
        }
        return true;
    }

    private void callforlogout() {
        LogoutClass logoutClass = new LogoutClass(user);
        Call<Void> call = APIClient.getUserServices().logoutUser(logoutClass);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(Dashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Dashboard.this, MainActivityLogin.class);
                    startActivity(in);
                    finishAffinity();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Failed to logout :" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }
}
