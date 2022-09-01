package in.ifarms.com.General;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import in.ifarms.com.APIClient;
import in.ifarms.com.R;
import in.ifarms.com.Search.EditFaultReportActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityLogin extends AppCompatActivity {

    private ProgressDialog mProgress;
    private int STORAGE_PERMISSION_CODE = 1;
    private TextInputEditText editTextName, passwordEdit;
    private TextInputLayout editLayout, passwordLayout;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "text";
    public static final String PASSWORD1 = "password";
    private MaterialButton buttonLogin;
    private String nameString, passwordString, userName;
    private ArrayList<String> workSpacelistReceived = new ArrayList<String>();
    private static final String TAG = "Tag";
    private static final String CHANNEL_ID = "channel Id";
    private static final String CHANNEL_NAME = "channel Name";
    private static final String CHANNEL_DESC = "channel Desc";
    private String deviceGCM;
    public static Context context;

    boolean isUserNameValid, isPasswordValid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        MainActivityLogin.context = getApplicationContext();
        buttonLogin = findViewById(R.id.btn_login);
        editTextName = findViewById(R.id.editTextUsername);
        passwordEdit = findViewById(R.id.editTextPassword);
        editLayout = findViewById(R.id.login_username_layout);
        passwordLayout = findViewById(R.id.login_password_layout);
        mProgress = new ProgressDialog(MainActivityLogin.this);
        mProgress.setTitle("Authenticating...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        deviceGCM = getToken(this);

        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
            buttonLogin.setEnabled(false);
        } else {
            buttonLogin.setEnabled(true);
            loadData();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = editTextName.getText().toString();
                passwordString = passwordEdit.getText().toString();
                if (TextUtils.isEmpty(editTextName.getText().toString().trim())) {
                    editLayout.setError("Please enter username");
                    isUserNameValid = false;
                } else {
                    editLayout.setError(null);
                    isUserNameValid = true;
                }
                if (TextUtils.isEmpty(passwordEdit.getText().toString().trim())) {
                    passwordLayout.setError("Please enter password");
                    isPasswordValid = false;
                } else {
                    passwordLayout.setError(null);
                    isPasswordValid = true;
                }
                if (isUserNameValid && isPasswordValid) {
                    editLayout.setError(null);
                    passwordLayout.setError(null);
                    mProgress.show();
                    UserRequest userRequest = new UserRequest(nameString, passwordString, deviceGCM);
                    userLogin(userRequest);
                    //saveData(userTokenReceived); 
                }


            }
        });
    }

    public static Context getAppContext() {
        return MainActivityLogin.context;
    }

    public void saveData(String userTokenReceived, String name, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("password", password);
        editor.putString("token", userTokenReceived);
        //  editor.putString("username", userName);
        editor.apply();
    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        nameString = sharedPreferences.getString("name", "");
        passwordString = sharedPreferences.getString("password", "");
        String userToken = sharedPreferences.getString("token", "");


        if (userToken != null) {
            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            Log.d(TAG, "loadData: " + bundle);
            if (bundle != null) {
                if (bundle.get("id") != null) {
                    String workspace = bundle.get("workspace").toString();
                    String click_action = bundle.get("click_action").toString();
                    String id = bundle.get("id").toString();
                    String equipCode = "";
                    String taskNumber = "";
                    String afterImage = "";
                    String beforeImage = "";
                    String source = "search";
                    Intent intent = null;

                    sharedPreferences.edit().putString("workspace", workspace).apply();
//                    startActivity(new Intent(MainActivityLogin.this, WorkspaceActivity.class));
//                    finish();
                    if (click_action.equals("EditFaultReportActivity")) {
                        intent = new Intent(getApplicationContext(), EditFaultReportActivity.class);
                        intent.putExtra("frId", id);
                        intent.putExtra("workspace", workspace);
                        startActivity(intent);
                        bundle.clear();
                        finish();

                    }

                }
            }
        } else if (!nameString.equals("")) {
            UserRequest request = new UserRequest(nameString, passwordString, deviceGCM);
            userLogin(request);
        }
    }

    private void requestStoragePermission() {

        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed for the application to run properly")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivityLogin.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.ACCESS_NOTIFICATION_POLICY}, STORAGE_PERMISSION_CODE);
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                buttonLogin.setEnabled(true);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setMessage("Please enable the permissions in settings")
                        .setPositiveButton("ok", null)
                        .create().show();
            }
        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }

    public void userLogin(UserRequest userRequest) {
        Call<UserResponse> call = APIClient.getUserServices().saveUser(userRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                mProgress.dismiss();
                if (response.code() == 200) {

                    workSpacelistReceived = response.body().workspacelist;
                    String userTokenReceived = response.body().token;
                    userName = response.body().getUsername();
                    String name = userRequest.getUsername();
                    String password = userRequest.getPassword();
                    saveData(userTokenReceived, name, password);

                    String role = response.body().role;
                    Intent intent = new Intent(getApplicationContext(), WorkspaceActivity.class);
                    intent.putExtra("token", userTokenReceived);
                    intent.putExtra("workList", workSpacelistReceived);
                    intent.putExtra("username", userName);
                    intent.putExtra("role", role);
                    startActivity(intent);
                } else if (response.code() == 202) {
                    Toast.makeText(MainActivityLogin.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivityLogin.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(MainActivityLogin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
