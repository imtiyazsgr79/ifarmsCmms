package in.ifarms.com.FaultReportSearch;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import in.ifarms.com.R;
import in.ifarms.com.Search.EditFaultReportActivity;

public class FaultSearchRecyclerAdapter extends RecyclerView.Adapter<FaultSearchRecyclerAdapter.RecyclerAdapterViewholder> {

    private static final String TAG = null;
    ArrayList<RowList> rowdata;
    Context mContext;


    public FaultSearchRecyclerAdapter(ArrayList<RowList> rowdata, Context mContext) {
        this.mContext = mContext;
        this.rowdata = rowdata;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public RecyclerAdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_for_faultsearch, parent, false);
        return new RecyclerAdapterViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterViewholder holder, int position) {
        String frId = rowdata.get(position).getFrId();
        String status = rowdata.get(position).getStatus();
        long report = rowdata.get(position).getReporteddate();
        long create = rowdata.get(position).getCreatedDate();
        String workspace = rowdata.get(position).getWorkspace();
        String token = rowdata.get(position).getToken();
        String user = rowdata.get(position).getUser();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(report);
        String rdate = DateFormat.format("dd-MM-yyyy", cal).toString();

        cal.setTimeInMillis(create);
        String cdate = DateFormat.format("dd-MM-yyyy", cal).toString();


        holder.textView_frid.setText("FrId: " + frId);
        holder.textView_status.setText("Status: " + status);
        holder.textView_reportdate.setText("Reported Date: " + rdate);
        holder.textView_createdate.setText("Created Date: " + cdate);
        holder.tv_workspace.setText(workspace);
        holder.tv_token.setText(token);
        holder.userName = user;
    }

    @Override
    public int getItemCount() {
        return rowdata.size();
    }


    public class RecyclerAdapterViewholder extends RecyclerView.ViewHolder {
        TextView textView_frid, textView_reportdate, textView_createdate, textView_status, tv_token, tv_workspace;
        String userName;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public RecyclerAdapterViewholder(@NonNull View itemView) {
            super(itemView);
            textView_frid = itemView.findViewById(R.id.textView_frid);
            textView_reportdate = itemView.findViewById(R.id.textView_reportdate);
            textView_createdate = itemView.findViewById(R.id.textView_create_date);
            textView_status = itemView.findViewById(R.id.textView_status);
            tv_token = itemView.findViewById(R.id.tv_token);
            tv_workspace = itemView.findViewById(R.id.tv_workspace);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        RowList clickedDataItem = rowdata.get(pos);

                        Intent intent = new Intent(itemView.getContext(), EditFaultReportActivity.class);
                        intent.putExtra("frId", clickedDataItem.getFrId());
                        intent.putExtra("token", clickedDataItem.getToken());
                        intent.putExtra("workspace", clickedDataItem.getWorkspace());
                        intent.putExtra("user", userName);
                        itemView.getContext().startActivity(intent);

                    }
                }
            });


        }
    }
}
