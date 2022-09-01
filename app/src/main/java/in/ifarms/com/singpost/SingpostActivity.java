package in.ifarms.com.singpost;

import static android.content.Intent.ACTION_GET_CONTENT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import in.ifarms.com.APIClient;
import in.ifarms.com.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingpostActivity extends AppCompatActivity {
    private StringBuilder encodedImageString;
    private String encodedPdfString;
    private Intent uploadFileIntent;
    private PDFView pdfView;
    private TextView nameTv, addresstv, lockertv, serialTv, simCardTv;
    private String token, workspace;
    private long equipCode;

    private ImageView imageView;

    private Button chosePDF, choseImageBtn, updateSingpostbtn;
    private String stringFromWebservice;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singpost);

        initViews();
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("Worklist");
        equipCode = intent.getIntExtra("equipId", 0);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");


        chosePDF = findViewById(R.id.chosepdf);
        choseImageBtn = findViewById(R.id.takeimage);
        updateSingpostbtn = findViewById(R.id.updateBtn);


        chosePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFileIntent = new Intent(ACTION_GET_CONTENT);
                uploadFileIntent.setType("application/pdf");
//                openActivityForResult.launch(uploadFileIntent);
                startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 10);
            }
        });

        choseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                openActivityForResult.launch(takePicture);
                startActivityForResult(takePicture, 1);
            }
        });

        updateSingpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String encodedFile = null;
                if (encodedImageString != null) {
                    encodedFile = "IMAGE/" + encodedImageString.toString();
                } else if (encodedPdfString != null) {
                    encodedFile = "PDF/" + encodedPdfString;
                }
                if (encodedFile == null) {
                    Toast.makeText(SingpostActivity.this, "Please Upload Image/Pdf.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setTitle("Uloading file..");
                    progressDialog.show();
                    SingpostModel EquipmentSingPostDTO = new SingpostModel(equipCode, nameTv.getText().toString(),
                            addresstv.getText().toString(), serialTv.getText().toString(), simCardTv.getText().toString(),
                            lockertv.getText().toString(), encodedFile);

                    Call<Void> call = APIClient.getUserServices().createSingPost(EquipmentSingPostDTO, token, workspace);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                Toast.makeText(SingpostActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (response.code() == 400) {
                                Toast.makeText(SingpostActivity.this, "error " + response.code(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SingpostActivity.this, "error " + response.code(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(SingpostActivity.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
        getSingPost(equipCode);


    }

    private void initViews() {

//                Log card
        nameTv = findViewById(R.id.nameofcondo);
        addresstv = findViewById(R.id.condoaddress);
        lockertv = findViewById(R.id.lockersize);
        simCardTv = findViewById(R.id.simcardrrgno);
        serialTv = findViewById(R.id.serialnomain);
        imageView = findViewById(R.id.chosedimage);
        pdfView = findViewById(R.id.pdfView);
    }


    private ActivityResultLauncher<Intent> openActivityForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {

                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                                ImageView imageView = findViewById(R.id.chosedimage);
                                imageView.setVisibility(View.VISIBLE);
                                imageView.setImageBitmap(photo);

                                try {
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    photo.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                                    encodedImageString = bmpToString(photo);
                                } catch (Exception e) {
                                    Log.d("TAG", "onActivityResult: " + e.getLocalizedMessage());
                                }
                            }


                        }
                    });

    private void getSingPost(long equipCode) {
        progressDialog.setTitle("Loading data..");
        progressDialog.show();
        Call<SingpostModel> call = APIClient.getUserServices().getSingPost(equipCode, token, workspace);
        call.enqueue(new Callback<SingpostModel>() {
            @Override
            public void onResponse(Call<SingpostModel> call, Response<SingpostModel> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
//                    equipIdfromweb = response.body().equipId;
                    nameTv.setText(response.body().nameOfCondo);
                    addresstv.setText(response.body().condoAddress);
                    lockertv.setText(response.body().lockerSize);
                    serialTv.setText(response.body().serialNumberMain);
                    simCardTv.setText(response.body().simCardRegNo);

                    if (response.body().getLogCard() != null) {
                        stringFromWebservice = response.body().getLogCard();
                        String extension = response.body().getLogCard().substring(response.body().getLogCard().lastIndexOf(".") + 1);
//                        if (extension.equals("jpeg") || extension.equals("png") || extension.equals("jpg")) {
                            getBase64Cal(stringFromWebservice, extension);
//                        }else if(extension.equals("pdf")){
//                            getpdfString(stringFromWebservice);
//                        }
//                        setPresentImgOrPdf(stringFromWebservice);
                    }

                } else if (response.code() == 400) {
                    Toast.makeText(SingpostActivity.this, "error " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SingpostActivity.this, "error " + response.code(), Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<SingpostModel> call, Throwable t) {
                Toast.makeText(SingpostActivity.this, "Failed to load data" + t.getCause(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void getpdfString(String stringFromWebservice) {

        SingpostModel singpostModel = new SingpostModel(stringFromWebservice);
        Call<ResponseBody> call = APIClient.getUserServices().getbase64String(singpostModel, token, workspace);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    try {
                        pdfView.fromBytes(response.body().bytes()).load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pdfView.setVisibility(View.VISIBLE);
                    pdfView.recycle();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getBase64Cal(String stringFromWebservice, String extension) {
        SingpostModel singpostModel = new SingpostModel(stringFromWebservice);
        Call<ResponseBody> call = APIClient.getUserServices().getbase64String(singpostModel, token, workspace);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("TAG", "onResponse: " + response.body().byteStream());
                    try {
                        setPresentImgOrPdf(response.body().byteStream(), extension,response.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setPresentImgOrPdf(InputStream stringEndcodedpdfImg, String extension, byte[] bytes) {


        if (extension.equals("pdf")) {
            pdfView.setVisibility(View.VISIBLE);
            pdfView.recycle();
            pdfView.fromBytes(bytes).load();

        } else
       if (extension.equals("jpeg") || extension.equals("png") || extension.equals("jpg")) {
//            Bitmap bmp = BitmapFactory.decodeStream(stringEndcodedpdfImg);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(photo);
                chosePDF.setVisibility(View.GONE);
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    encodedImageString = bmpToString(photo);
                } catch (Exception e) {
                    Log.d("TAG", "onActivityResult: " + e.getLocalizedMessage());
                }
            }

            if (requestCode == 10 && resultCode == RESULT_OK) {

                Uri uri = data.getData();

                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(uri);
                    byte[] pdfInBytes = new byte[inputStream.available()];
                    choseImageBtn.setVisibility(View.GONE);
                    pdfView.setVisibility(View.VISIBLE);
                    pdfView.recycle();
                    pdfView.fromBytes(pdfInBytes).load();
                    inputStream.read(pdfInBytes);
                    encodedPdfString = android.util.Base64.encodeToString(pdfInBytes, android.util.Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public StringBuilder bmpToString(Bitmap photoBmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoBmp.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new StringBuilder().append(Base64.encodeToString(byteArray, Base64.DEFAULT));
    }
}