package in.ifarms.com.singpost;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.ifarms.com.General.CardAdapter;
import in.ifarms.com.General.CardDetails;
import in.ifarms.com.General.Dashboard;
import in.ifarms.com.R;
import in.ifarms.com.Search.EquipmentSearchResponse;
import in.ifarms.com.TaskSearch.TasksRecyclerAdapter;

public class SingpostAdapter extends RecyclerView.Adapter<SingpostAdapter.SingpostViewHolder> {

    private List<EquipmentSearchResponse> listData;
    private  String token,workspace;

    public SingpostAdapter(List<EquipmentSearchResponse> listData,String token,String workspace) {
        this.listData = listData;
        this.token= token;
        this.workspace=workspace;
    }

    @NonNull
    @Override
    public SingpostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_fors_singpost, parent, false);
        SingpostViewHolder singpostViewHolder =new SingpostViewHolder(v);
        return singpostViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingpostViewHolder holder, int position) {

        final EquipmentSearchResponse currentItem = listData.get(position);
        holder.textViewId.setText(String.valueOf(currentItem.getId()));
        holder.textViewName.setText(currentItem.getName());
        holder.textViewEquipment.setText(currentItem.getEquipmentCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SingpostActivity.class);
                intent.putExtra("equipId", currentItem.getId());
                intent.putExtra("token",token);
                intent.putExtra("Worklist",workspace);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class SingpostViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId, textViewEquipment, textViewName;

        public SingpostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.equpi_sing_id);
            textViewName = itemView.findViewById(R.id.equpi_sing_name);
            textViewEquipment = itemView.findViewById(R.id.equpi_sing_code);

        }
    }
}
