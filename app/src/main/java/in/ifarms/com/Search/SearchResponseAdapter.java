package in.ifarms.com.Search;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.ifarms.com.R;

public class SearchResponseAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<SearchResponse> contacts; //data source of the list adapter
    private static final String TAG = "SearchResponseAdapter";
    private String wk = "";
    private String tokenGen;
    private String workspaceSearchString;
    private String user;

    public SearchResponseAdapter(Context context, ArrayList<SearchResponse> items, String user) {
        this.context = context;
        this.contacts = items;
        this.user = user;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.search_row, parent, false);
        }
        SearchResponse currentItem = (SearchResponse) getItem(position);

        TextView frIdTextView = (TextView) convertView.findViewById(R.id.textView_frid);
        TextView re = (TextView) convertView.findViewById(R.id.textView_reporteddatete);
        TextView cr = (TextView) convertView.findViewById(R.id.textView_createddate);
        TextView st = (TextView) convertView.findViewById(R.id.textView_status);
        TextView bu = (TextView) convertView.findViewById(R.id.textView_building);
        TextView lo = (TextView) convertView.findViewById(R.id.textView_location);
        TextView to = convertView.findViewById(R.id.tokenGen);
        TextView workspaceSearchTextView = convertView.findViewById(R.id.workspace_search);


        Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
        cal1.setTimeInMillis(currentItem.getReportedDate());
        cal1.add(Calendar.DATE, 1);

        Calendar cal2 = Calendar.getInstance(Locale.ENGLISH);
        cal2.setTimeInMillis(currentItem.getCreatedDate());
        cal2.add(Calendar.DATE, 1);

        String date = DateFormat.format("dd-MM-yyyy", cal2).toString();
        String report = DateFormat.format("dd-MM-yyyy", cal1).toString();

        frIdTextView.setText(currentItem.getFrId());
        re.setText(report);
        cr.setText(date);
        st.setText(currentItem.getStatus());
        bu.setText(currentItem.getBuilding());
        lo.setText(currentItem.getLocation());
        tokenGen = (currentItem.getTokenGen());

        user = currentItem.getUser();

        workspaceSearchString = currentItem.getWorkspaceSearch();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String frid = ((TextView) view.findViewById(R.id.textView_frid)).getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), EditFaultReportActivity.class);
                intent.putExtra("workspace", workspaceSearchString);
                intent.putExtra("frId", frid);
                intent.putExtra("token", tokenGen);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
