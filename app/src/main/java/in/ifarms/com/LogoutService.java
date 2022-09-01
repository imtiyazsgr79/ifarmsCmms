package in.ifarms.com;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import in.ifarms.com.General.MainActivityLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LogoutService {
    public void callForLogout(Activity activity, String user) {

        final String SHARED_PREFS = "sharedPrefs";
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Logging out!");
        progressDialog.setMessage("Please wait..");
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("TAG", "callforlogout: " + sharedPreferences.getString("name", ""));
//        String user = sharedPreferences.getString("name", "");
        progressDialog.show();
        LogoutClass logoutClass = new LogoutClass(user);
        Call<Void> call = APIClient.getUserServices().logoutUser(logoutClass);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(activity, MainActivityLogin.class);
                    activity.startActivity(in);
                    activity.finishAffinity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity, "Failed to logout :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
