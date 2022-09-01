package in.ifarms.com.EquipmentQrCodeSearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.ifarms.com.R;

public class QrAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<String> tno_list; //data source of the list adapter
    private static final String TAG = "SearchResponseAdapter";
    private String wk = "";
    private String tokenGen;
    private String workspaceSearchString;
    private String frIdCreateString;


    //public constructor
    public QrAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.tno_list = items;
    }

    @Override
    public int getCount() {
        return tno_list.size();
    }

    @Override
    public Object getItem(int position) {
        return tno_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.qr_row_listview, parent, false);
        }
        // get current item to be displayed

        TextView tno = convertView.findViewById(R.id.qr_tno_list_tv);
        for (int i=0;i<tno_list.size();i++){
            tno.setText(tno_list.get(i));
        }
        // get the TextView for item name and item description
        // returns the view for the current row
        return convertView;
    }
}
