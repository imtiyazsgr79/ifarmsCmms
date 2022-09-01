package in.ifarms.com.FaultReportSearch;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ifarms.com.APIClient;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class FaultReportSearchActivity extends AppCompatActivity {
    private static final String TAG = "";
    private Spinner spinner;
    private TextView fromdate, todate, count_tv;
    private Button search_btn;
    private String token, workspace;
    private int currentitem, totalitems, srolleditems;
    private DatePickerDialog.OnDateSetListener fromDateSetListener, toDateSetListener;
    private RecyclerView recyclerView;
    private FaultSearchRecyclerAdapter mAdapter;
    private LinearLayoutManager manager;
    private ArrayList<RowList> rowdata;
    private ProgressDialog mProgress;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrolledOutItems;
    private int countScroll = 1;
    private String user, setCount = "1";
    private FaultReportBodyRequest faultReportBodyRequest;
    private Toolbar toolbar;

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
            logoutService.callForLogout(FaultReportSearchActivity.this,user);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fault_report_search_activity);
        toolbar = findViewById(R.id.toolbar_faultresearch);
        setSupportActionBar(toolbar);

        mProgress = new ProgressDialog(FaultReportSearchActivity.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        rowdata = new ArrayList<RowList>();

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("worklist");


        count_tv = findViewById(R.id.count_tv);
        spinner = findViewById(R.id.spinner);
        fromdate = findViewById(R.id.fromdatepicker);
        todate = findViewById(R.id.todatepicker);
        search_btn = findViewById(R.id.search_fault_report__btn);
        manager = new LinearLayoutManager(this);
        mAdapter = new FaultSearchRecyclerAdapter(rowdata, this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        fromdate.setText(dateFormat.format(new Date()));
        todate.setText(dateFormat.format(new Date()));


        recyclerView = findViewById(R.id.recycler_view_fault_search);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentitem = manager.getChildCount();
                totalitems = manager.getItemCount();
                srolleditems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentitem + srolleditems == totalitems)) {
                    isScrolling = false;
                    countScroll++;
                    mProgress.show();
                }
            }
        });


        fromdate.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(FaultReportSearchActivity.this
                        //, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , fromDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(FaultReportSearchActivity.this
                        , toDateSetListener, year, month, day);

                datePickerDialog.show();

            }
        });

        fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int months, int day) {
                int month = months + 1;
                String date = day + "-" + month + "-" + year;
                fromdate.setText(date);
            }//jkj
        };
        toDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int months, int day) {
                int month = months + 1;
                String date = day + "-" + month + "-" + year;
                todate.setText(date);
            }
        };


        List<String> list = new ArrayList<String>();

        list.add("Select Status");
        list.add("Open");
        list.add("Closed");
        list.add("KIV");
        list.add("Inprogress");
        list.add("Follow");


        ArrayAdapter<List> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(null);
                rowdata.clear();
                if (!spinner.getSelectedItem().toString().equals("Select Status")) {
                    loadFaultSearchList(request());
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrolledOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    isScrolling = false;
                    countScroll++;
                    loadFaultSearchList(faultReportBodyRequest);
                }
            }
        });
    }


    public FaultReportBodyRequest request() {
        faultReportBodyRequest = new FaultReportBodyRequest();

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String from = fromdate.getText().toString().trim();
        String to = todate.getText().toString().trim();
        Date fdate = null;
        Date tdate = null;
        try {
            fdate = inputFormat.parse(from);
            tdate = inputFormat.parse(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert fdate != null;
        String ff = outputFormat.format(fdate);
        assert tdate != null;
        String tt = outputFormat.format(tdate);

        faultReportBodyRequest.setFrom(ff);
        faultReportBodyRequest.setTo(tt);
        faultReportBodyRequest.setStatus(spinner.getSelectedItem().toString());

        return faultReportBodyRequest;

    }

    private void loadFaultSearchList(FaultReportBodyRequest faultReportBodyRequest) {
        mProgress.show();

        Call<JsonObject> call = APIClient.getUserServices().getfaultList(countScroll, faultReportBodyRequest, token, workspace);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mProgress.dismiss();
                if (response.code() == 200) {
                    JsonObject jo = response.body();

                    if (response.body() != null) {
                        JsonElement jsonCount = jo.get("count");
                        setCount = jsonCount.toString();
                        count_tv.setText(setCount);

                        JsonArray ja = jo.getAsJsonArray("list");

                        JsonObject object = null;
                        for (int i = 0; i < ja.size(); i++) {
                            object = (JsonObject) ja.get(i);

                            JsonElement fr = object.getAsJsonPrimitive("frId");
                            JsonElement status = object.get("status");
                            JsonElement building = object.get("building");
                            JsonElement location = object.get("location");
                            JsonElement repot = object.get("reportedDate");
                            JsonElement create = object.get("createdDate");

                            String frr = String.valueOf(fr);
                            String ss = String.valueOf(status);
                            String ll = String.valueOf(location);
                            String bb = String.valueOf(building);
                            long rr = Long.parseLong(String.valueOf(repot));
                            long cc = Long.parseLong(String.valueOf(create));

                            ss = ss.replace("\"", "");

                            frr = frr.replace("\"", "");
                            rowdata.add(new RowList(frr, ss, ll, bb, rr, cc, token, workspace, user));
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                } else {
                    Toast.makeText(FaultReportSearchActivity.this, "Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(FaultReportSearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }

        });

    }
}