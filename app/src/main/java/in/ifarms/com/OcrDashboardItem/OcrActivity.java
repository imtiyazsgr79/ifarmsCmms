package in.ifarms.com.OcrDashboardItem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.General.Dashboard;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class OcrActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Spinner selectMeterSpinner;
    private List<String> spinnerOcrList = new ArrayList<>();
    private List<Integer> idOcrList = new ArrayList<>();
    private Map<String, Integer> meterMap = new HashMap<String, Integer>();
    private ArrayAdapter<String> spinnerArrayAdapter;
    private String token, workspace;
    private String previousReadingString, previousDateString;
    private Integer id;
    private String ocrImage;
    private ProgressDialog progressDialog1;
    private String imageText = "";
    private String meterName;
    private ImageView uploadDetailsManualyButton;
    private ImageView uploadImageView;
    private LinearLayout uploadImageLinear, uploadManuallyLinear;
    private TextView previousReadingTextView, previousDateTextView, dateReadingTextView;
    private EditText remarksEditText;
    private ImageView uploadedImageOcrImageView;
    private static final String TAG = "UploadPicture";
    private Bitmap photo;
    private Uri lastUri;
    private String encodedStringBuilder, user;
    private String baseString;
    private Button uploadButton, resetButton;
    private Button createReadingButton, modifyButton, verifyValueButton;
    private EditText currentReadingEditText;
    private ProgressDialog progressDialog, mProgress;
    private ProgressDialog saveProgressDialog;
    private ImageView verifyImageView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        toolbar = findViewById(R.id.toolbar_ocr);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("worklist");
        user = intent.getStringExtra("username");

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.camera_icon);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Getting values...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        saveProgressDialog = new ProgressDialog(this);
        saveProgressDialog.setTitle("Creating...");
        saveProgressDialog.setCancelable(false);
        saveProgressDialog.setIndeterminate(true);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Verifying...");
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        currentReadingEditText = findViewById(R.id.editTextOcr);
        selectMeterSpinner = findViewById(R.id.selectMeterSpinnerOcr);
        uploadDetailsManualyButton = findViewById(R.id.buttonUploadManually);
        uploadImageView = findViewById(R.id.imageViewOcr);
        uploadImageLinear = findViewById(R.id.linearOcrUploadImage);
        uploadManuallyLinear = findViewById(R.id.linearLayoutOcrManual);
        resetButton = findViewById(R.id.resetButtonOcr);
        uploadButton = findViewById(R.id.getValuesButtonOcr);
        uploadButton.setEnabled(false);
        createReadingButton = findViewById(R.id.readingButtonOcr);
        modifyButton = findViewById(R.id.modifyButtonOcr);
        verifyValueButton = findViewById(R.id.verifyButtonOcr);
        verifyImageView = findViewById(R.id.imageViewOcrDynamic);
        verifyImageView.setImageResource(R.drawable.camera_ocr);
        createReadingButton.setEnabled(false);
        idOcrList.add(1);

        uploadedImageOcrImageView = findViewById(R.id.uploadedImageOcr);
        uploadedImageOcrImageView.setImageBitmap(icon);
        previousDateTextView = findViewById(R.id.previousReadingDateTextViewOcr);
        previousReadingTextView = findViewById(R.id.previousReadingTextView);
        dateReadingTextView = findViewById(R.id.DateReadingTextViewOcr);
        remarksEditText = findViewById(R.id.remarksTextViewOcr);
        spinnerOcrList.add("Select the meter");
        currentReadingEditText.setEnabled(false);

        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerOcrList);
        selectMeterSpinner.setAdapter(spinnerArrayAdapter);

        uploadedImageOcrImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(OcrActivity.this);

            }
        });

        buttonClickListeners();

        createNewMeterReadingMethod();
    }

    private void uploadImageMethod() {
        encodedStringBuilder = "," + encodedStringBuilder;
        UploadImageOcrRequest uploadImageOcrRequest = new UploadImageOcrRequest(encodedStringBuilder);

        Call<PostImageResponse> callPostImageOcr = APIClient.getUserServices().postImageUploadOcr(uploadImageOcrRequest, token, workspace);
        callPostImageOcr.enqueue(new Callback<PostImageResponse>() {
            @Override
            public void onResponse(Call<PostImageResponse> call, Response<PostImageResponse> response) {
                if (response.code() == 200) {
                    currentReadingEditText.setText(imageText);
                    currentReadingEditText.setEnabled(false);
                    uploadManuallyLinear.setVisibility(View.VISIBLE);
                    uploadImageLinear.setVisibility(View.GONE);

                    PostImageResponse postImageResponse = response.body();
                    ocrImage = postImageResponse.getOcrImage();
                } else
                    Toast.makeText(OcrActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostImageResponse> call, Throwable t) {
                Toast.makeText(OcrActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void uploadImageAndGetValues() {

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        Frame imageFrame = new Frame.Builder()
                .setBitmap(photo)                 // your image bitmap
                .build();

        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            imageText = textBlock.getValue();  // return string
        }
        if (!imageText.equals("")) {
            getMeterList();
            compressImage();
            uploadImageMethod();
        } else {
            Toast.makeText(this, "Please select a valid image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void meterSelectedMethod() {

        String selectedSpinnerName = selectMeterSpinner.getSelectedItem().toString();
        int mapIdIndex = spinnerOcrList.indexOf(selectedSpinnerName);
        String mapIdSelected = String.valueOf(idOcrList.get(mapIdIndex));

        if (!spinnerOcrList.isEmpty()) {
            if (mapIdIndex != 0) {
                spinnerOcrList.add("Select the meter");

                Call<MeterSelectedResponse> callMeterSelected = APIClient.getUserServices().getMeterSelected(mapIdSelected, token, workspace);

                callMeterSelected.enqueue(new Callback<MeterSelectedResponse>() {
                    @Override
                    public void onResponse(Call<MeterSelectedResponse> call, Response<MeterSelectedResponse> response) {
                        if (response.code() == 200) {

                            MeterSelectedResponse meterSelectedResponse = response.body();
                            previousDateString = meterSelectedResponse.getPreviousDate();
                            previousReadingString = meterSelectedResponse.getPrevious();

                            previousDateTextView.setText(previousDateString);
                            previousReadingTextView.setText(previousReadingString);

                        } else
                            Toast.makeText(OcrActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MeterSelectedResponse> call, Throwable t) {
                        Toast.makeText(OcrActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void getMeterList() {


        if (idOcrList.contains(1)) {
            idOcrList.clear();
            spinnerOcrList.clear();
            idOcrList.add(1);
            spinnerOcrList.add("Select the meter");

            progressDialog1 = new ProgressDialog(OcrActivity.this);
            progressDialog1.setTitle("Loading Building List...");
            progressDialog1.setMessage("Please wait...");
            progressDialog1.setCancelable(false);
            progressDialog1.setIndeterminate(true);
            progressDialog1.show();

            Call<List<GetMeterListResponse>> callMeter = APIClient.getUserServices().getMeterList(token, workspace);

            callMeter.enqueue(new Callback<List<GetMeterListResponse>>() {
                @Override
                public void onResponse(Call<List<GetMeterListResponse>> call, Response<List<GetMeterListResponse>> response) {
                    if (response.code() == 200) {
                        List<GetMeterListResponse> responseList = response.body();

                        for (int i = 0; i < responseList.size(); i++) {
                            id = responseList.get(i).getId();
                            meterName = responseList.get(i).getMeterName();
                            id = responseList.get(i).getId();
                            idOcrList.add(id);

                            meterMap.put(meterName, id);
                            spinnerOcrList.add(meterName);
                            selectMeterSpinner.setAdapter(spinnerArrayAdapter);
                        }
                    } else
                        Toast.makeText(OcrActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    progressDialog1.dismiss();
                }

                @Override
                public void onFailure(Call<List<GetMeterListResponse>> call, Throwable t) {
                    Toast.makeText(OcrActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog1.dismiss();
                }
            });
        } else {

        }
        selectMeterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meterSelectedMethod();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showDatePickerDailog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        String date = i + "-" + (i1 + 1) + "-" + i2;
        dateReadingTextView.setText(date);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                uploadedImageOcrImageView.setImageURI(resultUri);
                uploadButton.setEnabled(true);

                try {
                    if (resultUri != null) {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    }
                } catch (Exception e) {
                    //handle exception
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void compressImage() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 50, baos); // bm is the bitmap object
            byte[] b = baos.toByteArray();

            encodedStringBuilder = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonClickListeners() {
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadButton.setEnabled(false);
                progressDialog.show();
                uploadImageAndGetValues();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadedImageOcrImageView.setImageResource(R.drawable.camera_icon);
                uploadButton.setEnabled(false);
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentReadingEditText.setEnabled(true);
            }
        });

        dateReadingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDailog();
            }
        });

        verifyValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyMethod();
                mProgress.show();

            }
        });
    }

    private void createNewMeterReadingMethod() {

        selectMeterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textWatcherMethod();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        remarksEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textWatcherMethod();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dateReadingTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textWatcherMethod();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void saveUtilityReading() {

        String meterName = selectMeterSpinner.getSelectedItem().toString();
        int index = spinnerOcrList.indexOf(meterName);
        int meterId = idOcrList.get(index);

        String currentReading = currentReadingEditText.getText().toString();
        String previousReading = previousReadingTextView.getText().toString();
        String readingDate = dateReadingTextView.getText().toString();
        String remarksString = remarksEditText.getText().toString();

        double currRead = Double.parseDouble(currentReadingEditText.getText().toString());
        double previousRead = Double.parseDouble(previousReadingTextView.getText().toString());

        if (currRead > previousRead) {
            saveProgressDialog.show();
            SaveUtilityRequest saveUtilityRequest = new SaveUtilityRequest(meterId, currentReading, previousReading, readingDate, remarksString, ocrImage);

            Call<Void> callSaveUtility = APIClient.getUserServices().postSaveUtility(saveUtilityRequest, token, workspace);

            callSaveUtility.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    saveProgressDialog.dismiss();
                    if (response.code() == 200) {
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("variable", workspace);
                        intent.putExtra("token", token);
                        intent.putExtra("username", user);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(OcrActivity.this, "Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(OcrActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    saveProgressDialog.dismiss();
                }
            });
        }else
            Toast.makeText(this, "Current Reading cannot be less than previous reading", Toast.LENGTH_SHORT).show();
    }

    private void textWatcherMethod() {

        if (!remarksEditText.getText().toString().isEmpty() &&
                !selectMeterSpinner.getSelectedItem().toString().equals("Select the meter") &&
                !dateReadingTextView.getText().toString().equals("Date of Reading")) {

            createReadingButton.setEnabled(true);
            createReadingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveUtilityReading();
                }
            });
        }
    }

    private void verifyMethod() {

        Call<ResponseBody> callVerifyOcr = APIClient.getUserServices().getVerifyOcr();
        callVerifyOcr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    verifyImageView.setImageBitmap(null);
                    verifyImageView.setImageBitmap(bmp);
                } else
                    Toast.makeText(OcrActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OcrActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            logoutService.callForLogout(OcrActivity.this,user);
        }
        return true;
    }


}