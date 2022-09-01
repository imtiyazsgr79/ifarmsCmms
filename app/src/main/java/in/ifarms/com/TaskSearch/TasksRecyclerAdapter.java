package in.ifarms.com.TaskSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ifarms.com.R;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.TasksViewHolder> {
    String locationTasksString;
    String tNoTasksString;
    private ArrayList<TaskCardDetails> taskCardDetails;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {
        TextView locTasksTextView, tNoTasksTextView;

        public TasksViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            locTasksTextView = itemView.findViewById(R.id.locationTask);
            tNoTasksTextView = itemView.findViewById(R.id.tnotasks);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    } else
                        Toast.makeText(itemView.getContext(), "this is empty", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public TasksRecyclerAdapter(ArrayList<TaskCardDetails> taskCardDetailsArrayList) {
        taskCardDetails = taskCardDetailsArrayList;
    }


    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_search, parent, false);
        return new TasksViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {

        final TaskCardDetails currentItem = taskCardDetails.get(position);
        holder.tNoTasksTextView.setText( currentItem.gettNoString());
        holder.locTasksTextView.setText(currentItem.getLocationString());

    }

    @Override
    public int getItemCount() {
        return taskCardDetails.size();
    }


}
