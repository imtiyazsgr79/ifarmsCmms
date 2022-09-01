package in.ifarms.com.General;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import in.ifarms.com.R;

public class UserAccount extends AppCompatActivity {

    TextView receivedTextView, arrayWorkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        receivedTextView = findViewById(R.id.textViewResponse);
        arrayWorkList = findViewById(R.id.textViewArray);
        receivedTextView.setText("str");

        String receivedValue;

        Intent recivedIntent = getIntent();
        Bundle bundle = recivedIntent.getExtras();
        receivedValue = (String) bundle.get("variable");
        Toast.makeText(this, receivedValue, Toast.LENGTH_SHORT).show();
    }
}
