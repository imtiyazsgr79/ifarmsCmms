package in.ifarms.com.EquipmentQrCodeSearch;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.JsonObject;
import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class QrEquipmentSearchActivity extends AppCompatActivity {
    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;
    private TextView scanTextView;
    private Button btn;
    private String token, workspace;
    private String TAG, user;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_equipment_search);

        Toolbar toolbar = findViewById(R.id.toolbar_qr);
        setSupportActionBar(toolbar);

        mProgress = new ProgressDialog(QrEquipmentSearchActivity.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("worklist");
        user = intent.getStringExtra("username");

        btn = findViewById(R.id.qr_btn_click);
        scanTextView = findViewById(R.id.scan_tv);
        codeScannerView = findViewById(R.id.qr_btn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn.setVisibility(View.INVISIBLE);
                    codeScannerView.setVisibility(View.VISIBLE);
                    codeScanner = new CodeScanner(QrEquipmentSearchActivity.this, codeScannerView);
                    codeScanner.startPreview();
                    codeScanner.setDecodeCallback(new DecodeCallback() {
                        @Override
                        public void onDecoded(@NonNull Result result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    scanTextView.setText(result.getText());
                                    codeScannerView.setVisibility(View.INVISIBLE);
                                    callQrCodeSearch(result.getText());
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void callQrCodeSearch(String result) {
        mProgress.show();

        Call<JsonObject> call = APIClient.getUserServices().getQrSeachList(result, token, workspace);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        mProgress.dismiss();
                        JsonObject jsonObject = response.body();

                        String code = String.valueOf(jsonObject.get("equipmentCode"));
                        String type = String.valueOf(jsonObject.get("type"));
                        String name = String.valueOf(jsonObject.get("name"));
                        String asset = String.valueOf(jsonObject.get("assetNo"));
                        JsonObject build = jsonObject.getAsJsonObject("building");
                        JsonObject loc = jsonObject.getAsJsonObject("location");

                        String building = String.valueOf(build.get("build_name"));
                        String location = String.valueOf(loc.get("loc_id"));
                        Intent intent = new Intent(QrEquipmentSearchActivity.this, QrDetails.class);

                        code = code.replace("\"", "");
                        type = type.replace("\"", "");
                        name = name.replace("\"", "");
                        asset = asset.replace("\"", "");
                        location = location.replace("\"", "");
                        building = building.replace("\"", "");

                        intent.putExtra("code", code);
                        intent.putExtra("type", type);
                        intent.putExtra("name", name);
                        intent.putExtra("building", building);
                        intent.putExtra("location", location);
                        intent.putExtra("asset", asset);
                        intent.putExtra("workspace", workspace);
                        intent.putExtra("token", token);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(QrEquipmentSearchActivity.this, "Equipment not found", Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(QrEquipmentSearchActivity.this, "Equipment not found", Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(QrEquipmentSearchActivity.this, "Equipment not found", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutmenu) {
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            LogoutService logoutService=new LogoutService();
            logoutService.callForLogout(QrEquipmentSearchActivity.this,user);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}