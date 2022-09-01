package in.ifarms.com.General;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.ifarms.com.LogoutService;
import in.ifarms.com.R;

public class WorkspaceActivity extends AppCompatActivity {
    private static final String TAG = "workspace";
    public static final String SHARED_PREFS = "sharedPrefs";
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Workspace List");

        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        String tokenReceived = receivedIntent.getStringExtra("token");
        user = receivedIntent.getStringExtra("username");
        String role = receivedIntent.getStringExtra("role");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        user = sharedPreferences.getString("name", "");



        ArrayList receivedWorkList = receivedIntent.getStringArrayListExtra("workList");

        ArrayList<CardDetails> cardDetailsArrayList = new ArrayList<>();

        for (int i = 0; i < receivedWorkList.size(); i++) {
            String value = receivedWorkList.get(i).toString();
            cardDetailsArrayList.add(new CardDetails(value, R.drawable.buildingsimage, tokenReceived, user, role));
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new CardAdapter(cardDetailsArrayList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

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
            logoutService.callForLogout(WorkspaceActivity.this,user);

        }
        return true;
    }

}