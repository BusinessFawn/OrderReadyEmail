package a4tay.com.orderreadyemail;

import android.content.ContentValues;
import android.database.Cursor;
import android.media.audiofx.BassBoost;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import a4tay.com.orderreadyemail.DB.AddPreferenceService;
import a4tay.com.orderreadyemail.DB.DatabaseContract;

import static a4tay.com.orderreadyemail.MainActivity.bodies;
import static a4tay.com.orderreadyemail.MainActivity.selectedEmail;
import static a4tay.com.orderreadyemail.MainActivity.serverIP;
import static a4tay.com.orderreadyemail.MainActivity.serverPort;
import static a4tay.com.orderreadyemail.MainActivity.subjects;


/**
 * Created by johnkonderla on 2/10/17.
 */

public class SettingsSection extends Fragment {

    private static final String LOG_TAG = SettingsSection.class.getSimpleName();

    AutoCompleteTextView ip;
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

        ip = (AutoCompleteTextView) rootView.findViewById(R.id.et_ip_value);

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

                if(ip.getText() != null && !ip.getText().toString().equals("")) {
                    serverIP = ip.getText().toString().trim();
                    SaveIP(serverIP);
                }
            }
        });


        new PreferenceShower().execute();

        return rootView;

    }

    private void SaveIP(String ip) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseContract.TablePreferences.COL_PREF_TYPE,1);
        contentValues.put(DatabaseContract.TablePreferences.COL_PREF_VALUE,ip);

        AddPreferenceService.insertNewPref(getContext(),contentValues);

        Toast.makeText(getContext(),"IP updated! ",Toast.LENGTH_LONG).show();



    }
    public void ShowIP(ArrayList<String> ips) {

        ArrayAdapter<String> ipAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.select_dialog_item,
                ips
        );
        ip.setThreshold(1);
        ip.setAdapter(ipAdapter);
        if(ips.size() > 0) {
            ip.setText(ips.get(ips.size() -1 ));
            ip.clearFocus();
        }

    }


    private class PreferenceShower extends AsyncTask<Void,Void,ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void...voidOb) {
            ArrayList ipsToSet = new ArrayList<>();
            Cursor prefCursor = getContext().getContentResolver().query(DatabaseContract.PREFERENCES_URI,
                    new String[]{DatabaseContract.TablePreferences.COL_PREF_VALUE},
                    "CAST (" + DatabaseContract.TablePreferences.COL_PREF_TYPE + " AS TEXT) = ?",
                    new String[] {"1"},
                    null
            );
            if(prefCursor != null && prefCursor.moveToFirst()) {
                do{
                    boolean addPref = true;
                    if(prefCursor.getString(0) != null) {
                        Log.d(LOG_TAG,"This is my first cursor result: " + prefCursor.getString(0));
                        if(ipsToSet.size() > 0) {
                            for(int i = 0; i < ipsToSet.size(); i++) {
                                if(ipsToSet.get(i).equals(prefCursor.getString(0))) {
                                    if(prefCursor.getCount() == (prefCursor.getPosition() + 1)) {
                                        ipsToSet.remove(i);
                                        Log.d(LOG_TAG, "Added the most recent IP as the last Pref");
                                    }
                                    else {
                                        addPref = false;
                                    }
                                }
                            }
                        }
                    } else {
                        Log.d(LOG_TAG,"My cursor was empty");
                    }
                    if(addPref) {
                        ipsToSet.add(prefCursor.getString(0));
                        Log.d(LOG_TAG,"Added this IP to my list: " + prefCursor.getString(0));
                    }
                } while (prefCursor.moveToNext());
                prefCursor.close();
            }
            if(ipsToSet.size() == 0) {
                Log.d(LOG_TAG,"no IPS were saved");
            }
            return ipsToSet;
        }
        @Override
        protected void onPostExecute(ArrayList<String> ips) {
            ShowIP(ips);
        }
    }

}