package in.ifarms.com.QrFaultReportScan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class FrQrScan extends AppCompatActivity {

    private CodeScannerView codeScannerView;
    private String token, workspace;
    private String TAG, user;
    private ProgressDialog mProgress;
    private TextView codeBoxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr_qr_scan);

        Toolbar toolbar = findViewById(R.id.toolbar_qr);
        setSupportActionBar(toolbar);
        codeBoxView = findViewById(R.id.qr_code_textview);
        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("Worklist");
        user = intent.getStringExtra("username");
        codeScannerView = findViewById(R.id.qr_create_btn);
        mProgress = new ProgressDialog(FrQrScan.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        scanOpen();

    }

    private void scanOpen() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

        CodeScanner codeScanner = new CodeScanner(FrQrScan.this, codeScannerView);
        codeScanner.startPreview();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myScanCall(result);
                    }
                });
            }
        });


    }

    private void myScanCall(Result result) {

        mProgress.show();

        Call<JsonObject> call = APIClient.getUserServices().getQrFaultReportCreated(result.toString(), token, workspace);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    mProgress.dismiss();
                    JsonObject jsonObject = response.body();
                    assert jsonObject != null;
                    String ee = String.valueOf(jsonObject.get("equipmentCode"));
                    String ll = String.valueOf(jsonObject.get("location").getAsString());
                    String bdg = String.valueOf(jsonObject.get("building"));
                    ee = ee.replace("\"", "");
                    ll = ll.replace("\"", "");
                    bdg = bdg.replace("\"", "");

                    codeBoxView.setText(ee);

                    Intent intent = new Intent(FrQrScan.this, QrFaultRepotCreate.class);
                    intent.putExtra("building", bdg);
                    intent.putExtra("location", ll);
                    intent.putExtra("equipment", ee);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("token", token);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(FrQrScan.this, "Equipment not found", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(FrQrScan.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
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
            LogoutService logoutService=new LogoutService();
            logoutService.callForLogout(FrQrScan.this,user);


        }
        return true;
    }
}