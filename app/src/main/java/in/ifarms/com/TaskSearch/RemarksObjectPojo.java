package in.ifarms.com.TaskSearch;

import android.widget.EditText;
import android.widget.Spinner;

public class RemarksObjectPojo {
    Spinner spinner;
    EditText remEditText;

    public RemarksObjectPojo(Spinner spinner, EditText remEditText) {
        this.spinner = spinner;
        this.remEditText = remEditText;
    }

    public RemarksObjectPojo() {
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    public EditText getRemEditText() {
        return remEditText;
    }

    public void setRemEditText(EditText remEditText) {
        this.remEditText = remEditText;
    }
}
