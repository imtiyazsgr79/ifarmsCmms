package in.ifarms.com.TaskSearch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadSignature extends AppCompatActivity {

    private Spinner spinnerStatus;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> spinnerList = new ArrayList<>();
    private SignatureView signatureView;
    private Button saveButton, clearButton;
    private String encodedStringBuilder;
    private String taskNumber, user, workspace, token;
    private ProgressDialog progressDialog;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_signature);
        toolbar = findViewById(R.id.toolup);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        taskNumber = intent.getStringExtra("taskNumber");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("workspace");

        progressDialog = new ProgressDialog(UploadSignature.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        spinnerStatus = findViewById(R.id.spinnerStatusSignature);
        signatureView = findViewById(R.id.signatureEdit);
        clearButton = findViewById(R.id.clearSign);
        saveButton = findViewById(R.id.saveSign);
        saveButton.setEnabled(false);
        spinnerList.add("Select the status");
        spinnerList.add("open");
        spinnerList.add("closed");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinnerStatus.setAdapter(arrayAdapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spinnerStatus.getSelectedItem().toString().equals("Select the status")) {
                    saveButton.setEnabled(true);
                } else
                    saveButton.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                saveButtonMethod();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
            }
        });

    }

    private void saveButtonMethod() {

        if (signatureView.isBitmapEmpty()) {
            AlertDialog.Builder emptyDailog = new AlertDialog.Builder(UploadSignature.this);
            emptyDailog.setTitle("Add signature");
            emptyDailog.setIcon(R.drawable.ic_error);
            emptyDailog.setPositiveButton("Ok", null);
            emptyDailog.show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UploadSignature.this);
            alertDialogBuilder.setTitle("Upload Signature");
            alertDialogBuilder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    progressDialog.show();

                    compressImageMethod();
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", null);
            alertDialogBuilder.show();
        }
    }

    private void compressImageMethod() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            signatureView.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG, 50, baos); // bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            encodedStringBuilder = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            encodedStringBuilder = "data:image/jpeg;base64," + encodedStringBuilder;
            uploadSignatureMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadSignatureMethod() {

        String spinnerStatusString = spinnerStatus.getSelectedItem().toString();
        UploadSignatureRequest uploadSignatureRequest = new UploadSignatureRequest(taskNumber, encodedStringBuilder,
                "attendedby", spinnerStatusString);

        Call<Void> callUploadSignature = APIClient.getUserServices().getSignatureCall(uploadSignatureRequest, token, workspace);
        callUploadSignature.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(UploadSignature.this, "Signature Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(UploadSignature.this, "Response code: " + response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UploadSignature.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle("Hello: " + user);
        return true;
    }
}