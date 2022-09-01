package in.ifarms.com.General;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ifarms.com.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.FarmsViewHolder> {

    private String variable;
    private ArrayList<CardDetails> mCardDetails;
    private static final String TAG = "CardAdapter";
    private String tokenReceivedRecyclerView, user, role;


    @NonNull
    @Override
    public FarmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_card, parent, false);
        FarmsViewHolder farmsViewHolder = new FarmsViewHolder(v);
        return farmsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FarmsViewHolder holder, int position) {

        final CardDetails currentItem = mCardDetails.get(position);
        user = mCardDetails.get(position).getUser();
        role = mCardDetails.get(position).getRole();
        holder.textViewCard.setText(currentItem.getCardName());
        holder.imageViewCard.setImageResource(currentItem.getCardImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                variable = currentItem.getCardName();
                tokenReceivedRecyclerView = currentItem.getToken();
                user = currentItem.getUser();
                role = currentItem.getRole();

                Intent intent = new Intent(view.getContext(), Dashboard.class);
                intent.putExtra("variable", variable);
                intent.putExtra("token", tokenReceivedRecyclerView);
                intent.putExtra("username", user);
                intent.putExtra("role", role);

                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardDetails.size();
    }

    public static class FarmsViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewCard;
        public TextView textViewCard;

        public FarmsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCard = itemView.findViewById(R.id.imageView_card);
            textViewCard = itemView.findViewById(R.id.textView_card);
        }
    }

    public CardAdapter(ArrayList<CardDetails> cardDetailsArrayList) {
        mCardDetails = cardDetailsArrayList;
    }
}
