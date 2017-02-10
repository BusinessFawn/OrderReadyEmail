package a4tay.com.orderreadyemail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import static a4tay.com.orderreadyemail.MainActivity.bodies;
import static a4tay.com.orderreadyemail.MainActivity.selectedEmail;
import static a4tay.com.orderreadyemail.MainActivity.subjects;

/**
 * Created by johnkonderla on 2/10/17.
 */

public class SettingsSection extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.settings_screen, container, false);

        selectedEmail = 0;

        final Spinner responseSpinner = (Spinner) rootView.findViewById(R.id.sp_response_spinner);

        ArrayList<String> responseList = new ArrayList<>();
        responseList.add("Response 1");
        responseList.add("Response 2");
        responseList.add("Response 3");

        final ArrayAdapter<String> responseAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                responseList
        );
        responseSpinner.setAdapter(responseAdapter);

        final EditText subject = (EditText) rootView.findViewById(R.id.et_email_subject);

        final EditText body = (EditText) rootView.findViewById(R.id.et_email_body);

        subjects = new ArrayList<>();

        subjects.add("Your order is ready!");
        subjects.add("Please collect your order!");
        subjects.add("Come and get it!");

        bodies = new ArrayList<>();

        bodies.add("Please present receipt to collect order: ");
        bodies.add("We put in the work, now you can come grab order: ");
        bodies.add("We're all done, the rest is up to you! Order number: ");

        subject.setText(subjects.get(selectedEmail));
        body.setText(bodies.get(selectedEmail));

        responseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          selectedEmail = position;

                                                          subject.setText(subjects.get(selectedEmail));
                                                          body.setText(bodies.get(selectedEmail));
                                                      }

                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {

                                                      }
                                                  }
        );
        final Button saveChanges = (Button) rootView.findViewById(R.id.bt_email_save);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjects.remove(selectedEmail);
                subjects.add(selectedEmail,subject.getText().toString().trim());
                bodies.remove(selectedEmail);
                bodies.add(selectedEmail,body.getText().toString().trim());

                subject.setText(subjects.get(selectedEmail));
                body.setText(bodies.get(selectedEmail));
            }
        });




        return rootView;

    }
}