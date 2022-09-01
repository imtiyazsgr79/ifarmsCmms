package in.ifarms.com.singpost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.ifarms.com.APIClient;
import in.ifarms.com.R;
import in.ifarms.com.Search.EquipmentSearchActivity;
import in.ifarms.com.Search.EquipmentSearchAdapter;
import in.ifarms.com.Search.EquipmentSearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingpostSeachActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    ArrayList<EquipmentSearchResponse> equipDetails = new ArrayList<>();
    SingpostAdapter singpostAdapter;

    String token;
    String workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singpost_seach);
        Intent intent = getIntent();
        token=intent.getStringExtra("token");
        workspace = intent.getStringExtra("Worklist");

        recyclerView = findViewById(R.id.equipoment_recycler_singpost);
        searchView = findViewById(R.id.equipmentsearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    equipDetails.clear();

                } else {
                    equipDetails.clear();
                    getEquipment(newText, token, workspace);
                }
                return false;
            }
        });
    }

    private void getEquipment(String queryParam, String token, String workspace) {
        equipDetails.clear();

        Call<List<EquipmentSearchResponse>> call = APIClient.getUserServices().getEquipment(queryParam, token, workspace);

        call.enqueue(new Callback<List<EquipmentSearchResponse>>() {
            @Override
            public void onResponse(Call<List<EquipmentSearchResponse>> call, Response<List<EquipmentSearchResponse>> response) {
                if (response.code() == 200) {
                    List<EquipmentSearchResponse> equipmentSearchResponse = response.body();
                    for (EquipmentSearchResponse equipment : equipmentSearchResponse) {
                        // contacts.clear();
                        EquipmentSearchResponse s = new EquipmentSearchResponse();
                        int id = equipment.getId();
                        String name = equipment.getName();
                        String eq_code = equipment.getEquipmentCode();

                        s.setId(id);
                        s.setName(name);
                        s.setEquipmentCode(eq_code);
                        equipDetails.add(s);
                    }
                    singpostAdapter = new SingpostAdapter(equipDetails,token,workspace);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SingpostSeachActivity.this));
                    recyclerView.setAdapter(singpostAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<EquipmentSearchResponse>> call, Throwable t) {
                Toast.makeText(SingpostSeachActivity.this, "Failed to SearchActivity Equipment", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure get message of fail:  " + t.getMessage());
                Log.d("TAG", "onFailure: cause" + t.getCause());
            }
        });
    }

}