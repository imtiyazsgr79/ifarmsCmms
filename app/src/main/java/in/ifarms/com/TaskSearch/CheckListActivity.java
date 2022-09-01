package in.ifarms.com.TaskSearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
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

public class CheckListActivity extends AppCompatActivity {
    private String taskNumber, token, workspace;
    private ProgressDialog mProgress, progressDialog;
    private List<String> statusCheckList = new ArrayList<>();
    private ArrayAdapter<String> statusCheckListAdapter;
    private static final String TAG = "CheckListActivity";
    private LinearLayout linearLayout;
    private Button saveCheckListButton;
    private String descString, descTypeString, statusString;
    private List<String> descStringList = new ArrayList<>();
    private List<String> descTypeStringList = new ArrayList<>();
    private LinearLayout.LayoutParams lparams;
    private String spinnerString, user, editTextString, textViewString;
    private JsonArray jsonArrayChecklist;
    private List<TextView> textViewEditList = new ArrayList<>();
    private List<TextView> textViewSpinnerList = new ArrayList<>();
    private Map<Object, String> textViewMap = new HashMap<>();
    private List<String> textViewRemarksList = new ArrayList<String>();
    private List<String> textViewList = new ArrayList<String>();
    private List<EditText> editTextList = new ArrayList<>();
    private List<EditText> editViewRemarksList = new ArrayList<>();
    private List<Spinner> spinnerList = new ArrayList<>();
    private CheckListAddRequest checkListAddRequest;
    private List<CheckListAddRequest> checkListActivities = new ArrayList<>();
    private TextView textViewCheckList;
    private int j = 0;
    private EditText editText;
    private Toolbar toolbar;
    private Spinner statusCheckListSpinner;
    private EditText remarksEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        toolbar = findViewById(R.id.checlist);
        setSupportActionBar(toolbar);

        linearLayout = findViewById(R.id.linearLayoutCheckList);
        linearLayout.setBackgroundColor(Color.parseColor("#c4c9d6"));
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Getting checklist....");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        taskNumber = intent.getStringExtra("taskNumber");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("workspace");
        saveCheckListButton = findViewById(R.id.savechecklistButton);
        saveCheckListButton.setEnabled(false);

        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        statusCheckList.add("yes");
        statusCheckList.add("no");
        statusCheckList.add("NA");

        editTextList.clear();
        spinnerList.clear();
        textViewEditList.clear();
        textViewSpinnerList.clear();
        editViewRemarksList.clear();

        //method to generate Fill in the blanks or Question Answers
        getCheckListId();

        saveCheckListButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //method to update checklist
                checkListUpdateMethod();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkListUpdateMethod() {
        progressDialog.show();
        int i = 0;
        String textViewS;
        if (!editTextList.isEmpty()) {
            for (i = 0; i < editTextList.size(); i++) {
                checkListAddRequest = new CheckListAddRequest("", "", taskNumber);
                checkListActivities.add(checkListAddRequest);
            }
        }
        if (!spinnerList.isEmpty()) {
            for (int k = i; k < spinnerList.size(); k++) {
                String spinnerTextS = spinnerList.get(k).getSelectedItem().toString();
                textViewS = textViewList.get(k);
                String textViewRemarks = textViewRemarksList.get(k);
                String remarksString = editViewRemarksList.get(k).getText().toString();
                checkListAddRequest = new CheckListAddRequest(spinnerTextS, textViewS, taskNumber);
                CheckListAddRequest checkListAddRemarks = new CheckListAddRequest(remarksString, textViewRemarks, taskNumber);
                checkListActivities.add(checkListAddRequest);
                checkListActivities.add(checkListAddRemarks);
            }
        }

       /* for (int j = 0; j<textViewList.size();j++){
            if (!editTextList.isEmpty() && editTextList.size()>j){
                CheckListAddRequest checkListAddRequest = new CheckListAddRequest("", "", taskNumber);
                checkListActivities.add(checkListAddRequest);
            }
            else {
                String textString = textViewList.keySet().toString();
                String spinnerText = textViewList.values().toString();
                CheckListAddRequest checkListAddRequest = new CheckListAddRequest(spinnerText, textString, taskNumber);
                CheckListAddRequest checkListAddRequestRemarks = new CheckListAddRequest()
            }
        }*/


      /*  int j=0;
        for (Map.Entry<Object, String> entry: textViewMap.entrySet()) {
            String temp  = "taskedit.checkLists[" + j + "].status";
            String remTemp  = "taskedit.checkLists[" + j + "].remarks";
            if (entry.getValue().equals("FILLTHEBLANKS")){
                CheckListAddRequest checkListAddRequest = new CheckListAddRequest("", "", taskNumber);
                checkListActivities.add(checkListAddRequest);
            }
            if (entry.getValue().equals("QUESTIONANSWER")) {
                String textString = entry.getValue();
                Spinner spinner = (Spinner) entry.getKey();

                Object object = entry.getKey();

                String spinnerText = spinner.getSelectedItem().toString();
                EditText remarkEd = editViewRemarksList.get(j);
                String remark = remarkEd.getText().toString();
                CheckListAddRequest checkListAddRequest = new CheckListAddRequest(spinnerText, temp, taskNumber);
                CheckListAddRequest checkListAddRequestRemarks = new CheckListAddRequest(remark, remTemp, taskNumber);
                checkListActivities.add(checkListAddRequest);
                checkListActivities.add(checkListAddRequestRemarks);
            }

            Log.d(TAG, "checkListUpdateMethod: works once");

            j++;
        }*/

        Call<Void> callCheckListAdd = APIClient.getUserServices().getCheckListAdd(checkListActivities, token, workspace);

        callCheckListAdd.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(CheckListActivity.this, "Checklist saved successfully!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), UploadSignature.class);
                    intent.putExtra("taskNumber", taskNumber);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("token", token);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CheckListActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CheckListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCheckListId() {
        mProgress.show();

        Call<JsonObject> callGetChecklist = APIClient.getUserServices().getCheckListResponse(taskNumber, token, workspace);
        callGetChecklist.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    JsonObject jsonBody = response.body();
                    jsonArrayChecklist = jsonBody.getAsJsonArray("checkLists");
                    for (int i = 0; i < jsonArrayChecklist.size(); i++) {
                        JsonObject jsonObject1 = jsonArrayChecklist.get(i).getAsJsonObject();
                        JsonObject jsonObject2 = jsonObject1.get("checklistProperty1").getAsJsonObject();
                        JsonElement jsonObject3 = jsonObject1.get("remarks");
                        String remarkStringReceived = String.valueOf(jsonObject3);
                        JsonElement jsonElementStatus = jsonObject1.get("status");
                        JsonElement jsonElementDesc = jsonObject2.get("description");
                        JsonElement jsonElementDescType = jsonObject2.get("descriptionType");

                        descString = String.valueOf(jsonElementDesc);
                        descTypeString = String.valueOf(jsonElementDescType);
                        statusString = String.valueOf(jsonElementStatus);

                        descTypeString = descTypeString.replace("\"", "");
                        descString = descString.replace("\"", "");
                        statusString = statusString.replace("\"", "");
                        remarkStringReceived = remarkStringReceived.replace("\"", "");

                        if (remarkStringReceived.equals("null")){
                            remarkStringReceived = "";
                        }

                        RemarksObjectPojo remarksObjectPojo = new RemarksObjectPojo();

                        descStringList.add(descString);
                        descTypeStringList.add(descTypeString);
                        if (!statusCheckList.contains(statusString)) {
                            statusCheckList.add(statusString);
                        }

                        textViewString = "taskedit.checkLists[" + i + "].status";

                        if (descTypeString.equals("FILLTHEBLANKS")) {
                            descString = descString.replace("___", "..");
                            createEditText(descString);
                            editTextList.add(editText);
                            textViewMap.put(editText, descTypeString);
                            Log.d(TAG, "onResponse: Success");
                            textViewEditList.add(textViewCheckList);
                        }
                        if (descTypeString.equals("QUESTIONANSWER")) {
                            descString = descString + "?";
                            createSpinner(descString, remarkStringReceived);
                            spinnerList.add(statusCheckListSpinner);
                            editViewRemarksList.add(remarksEditText);
                            remarksObjectPojo.setRemEditText(remarksEditText);
                            remarksObjectPojo.setSpinner(statusCheckListSpinner);
                            textViewMap.put(remarksObjectPojo, descTypeString);
                            Log.d(TAG, "onResponse: Success Spinner");
                            textViewSpinnerList.add(textViewCheckList);
                        }
                        String textRemarks = "taskedit.checkLists[" + i + "].remarks";
                        textViewRemarksList.add(textRemarks);
                        textViewList.add(textViewString);
                    }
                }
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgress.dismiss();
            }
        });

    }

    private String createSpinner(String descString, String remarksReceived) {

        textViewCheckList = new TextView(this);
        textViewCheckList.setId(View.generateViewId());
        lparams.setMargins(20, 20, 20, 20);
        textViewCheckList.setLayoutParams(lparams);
        textViewCheckList.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewCheckList.setText(descString);
        linearLayout.addView(textViewCheckList);
        saveCheckListButton.setEnabled(true);

        statusCheckListSpinner = new Spinner(this);
        statusCheckListSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        statusCheckListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusCheckList);
        statusCheckListSpinner.setAdapter(statusCheckListAdapter);
        statusCheckListSpinner.setId(View.generateViewId());
        lparams.setMargins(20, 20, 20, 20);
        statusCheckListSpinner.setLayoutParams(lparams);
        linearLayout.addView(statusCheckListSpinner);
        spinnerString = statusCheckListSpinner.getSelectedItem().toString();


        remarksEditText = new EditText(this);
        remarksEditText.setHeight(60);
        remarksEditText.setWidth(300);
            remarksEditText.setText(remarksReceived);
        remarksEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        remarksEditText.setHint("Remarks");
        remarksEditText.setId(View.generateViewId());
        editTextString = remarksEditText.getText().toString();
        lparams.setMargins(20, 20, 20, 40);
        remarksEditText.setLayoutParams(lparams);
        linearLayout.addView(remarksEditText, lparams);

        return spinnerString;
    }

    private void createEditText(String descString) {

        textViewCheckList = new TextView(this);
        textViewCheckList.setId(View.generateViewId());
        lparams.setMargins(20, 20, 20, 20);
        textViewCheckList.setLayoutParams(lparams);
        textViewCheckList.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewCheckList.setText(descString);
        linearLayout.addView(textViewCheckList);
        saveCheckListButton.setEnabled(true);

        editText = new EditText(this);
        editText.setHeight(60);
        editText.setWidth(300);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setId(View.generateViewId());
        editTextString = editText.getText().toString();
        lparams.setMargins(20, 20, 20, 40);
        editText.setLayoutParams(lparams);
        linearLayout.addView(editText, lparams);
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
            logoutService.callForLogout(CheckListActivity.this,user);
        }
        return true;
    }
}
