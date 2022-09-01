package in.ifarms.com.Search;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.ifarms.com.APIClient;
import in.ifarms.com.LogoutService;
import in.ifarms.com.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ifarms.com.General.MainActivityLogin.SHARED_PREFS;

public class SearchActivity extends AppCompatActivity {
    private String TAG;
    private String frId = "";
    private String token, user;
    private String workList;
    private List<String> frIdList = new ArrayList<>();

    private ListView listView;
    private ArrayList<SearchResponse> contacts = new ArrayList<>();
    private SearchResponseAdapter searchResponseAdapter;
    Toolbar toolbar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.tool_search);
        setSupportActionBar(toolbar);

        ScrollView view = (ScrollView) findViewById(R.id.scrollViewSearch);
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        token = intent.getStringExtra("token");
        workList = intent.getStringExtra("worklist");
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        listView = findViewById(R.id.list_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String onTextChangeQueryCall) {
                if (onTextChangeQueryCall.isEmpty()) {
                    contacts.clear();
//                    searchResponseAdapter.notifyDataSetChanged();
                } else {
                    contacts.clear();
                    loadSearch(token, workList, onTextChangeQueryCall);
                    listView.setFilterText(onTextChangeQueryCall);
                }

                return false;
            }
        });

    }

    private void loadSearch(String tokenString, String worklistString, String callQueryDependent) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        Call<List<SearchResponse>> call = APIClient.getUserServices().getSearchResult(tokenString, worklistString, callQueryDependent);
        call.enqueue(new Callback<List<SearchResponse>>() {
            @Override
            public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    List<SearchResponse> list = response.body();
                    for (SearchResponse searchResponse : list) {

                        SearchResponse searchResp = new SearchResponse();
                        frId = searchResponse.getFrId();
                        long rtdate = searchResponse.getReportedDate();
                        long cdated = searchResponse.getCreatedDate();
                        String status = searchResponse.getStatus();
                        String buildingg = searchResponse.getBuilding();
                        String locationn = searchResponse.getLocation();

                        searchResp.setFrId(frId);
                        searchResp.setReportedDate(rtdate);
                        searchResp.setCreatedDate(cdated);
                        searchResp.setStatus(status);
                        searchResp.setBuilding(buildingg);
                        searchResp.setLocation(locationn);
                        searchResp.setTokenGen(tokenString);
                        searchResp.setWorkspaceSearch(worklistString);
                        searchResp.setUser(user);

                        frIdList.add(frId);
                        contacts.add(searchResp);
                    }
                    Collections.sort(frIdList);
                    searchResponseAdapter = new SearchResponseAdapter(SearchActivity.this, contacts, user);
                    listView.setAdapter(searchResponseAdapter);
                    searchResponseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SearchResponse>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Failed to load Searched Data" + t.getMessage() + "Cause is here" + t.getCause(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to load Searched Data" + t.getMessage());
                progressDialog.dismiss();
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
            logoutService.callForLogout(SearchActivity.this,user);
        }
        return true;
    }

}
