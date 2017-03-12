package a4tay.com.orderreadyemail;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import a4tay.com.orderreadyemail.DB.DatabaseContract;

import static a4tay.com.orderreadyemail.MainActivity.carrier;
import static a4tay.com.orderreadyemail.MainActivity.transactionArrayList;
import static a4tay.com.orderreadyemail.OrderSection.staticIP;
import static a4tay.com.orderreadyemail.OrderSection.staticPort;

/**
 * Created by johnkonderla on 2/10/17.
 */

public class InputSection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.input_screen, container, false);


        final Spinner carrierSpinner;
        final TextView toTextView;
        final TextView nameTextView;
        final TextView transTextView;
        final Button saveOrderToList;

        ArrayList<String> carrierSelector = new ArrayList<>();
        carrierSelector.add("Att");
        carrierSelector.add("T-Mobile");
        carrierSelector.add("Verizon");
        carrierSelector.add("Sprint");
        carrierSelector.add("Virgin Mobile");
        carrierSelector.add("Tracfone");
        carrierSelector.add("Metro PCS");
        carrierSelector.add("Boost Mobile");
        carrierSelector.add("Cricket");

        carrierSpinner = (Spinner) rootView.findViewById(R.id.sp_carrier_spinner);

        final ArrayAdapter<String> carrierAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                carrierSelector
        );
        carrierSpinner.setAdapter(carrierAdapter);

        toTextView = (TextView) rootView.findViewById(R.id.editText3);

        nameTextView = ((TextView) rootView.findViewById(R.id.editText4));

        transTextView = (TextView) rootView.findViewById(R.id.editText5);

        saveOrderToList = (Button) rootView.findViewById(R.id.button1);

        saveOrderToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = toTextView.getText().toString().trim();
                String name = nameTextView.getText().toString().trim();
                String transNumb = transTextView.getText().toString().trim();
                if(number.length() <= 9 || number.equals("") || number.length() > 10) {
                    toTextView.setError("A valid phone number is required!");
                } else if (name.length() == 0 || name.equals("")) {
                    nameTextView.setError("A name is required!");
                } else if (transNumb.length() == 0 || transNumb.equals("")) {
                    transTextView.setError("A transaction number is required");
                } else {

                    //transactionArrayList.add(new Transaction(name,number,carrierSpinner.getSelectedItem().toString(),transNumb));

                    toTextView.setText("");
                    nameTextView.setText("");
                    transTextView.setText("");
                    carrierSpinner.setId(0);

                    new PushNewOrder().execute(new Transaction(name,number,carrierSpinner.getSelectedItem().toString(),transNumb));
                }


            }
        });

        return rootView;
    }
    public class PushNewOrder extends AsyncTask<Transaction,String,TCPClient> {

        protected TCPClient doInBackground(Transaction... trans) {
            Cursor prefCursor = getContext().getContentResolver().query(DatabaseContract.PREFERENCES_URI,
                    new String[]{DatabaseContract.TablePreferences.COL_PREF_VALUE},
                    "CAST (" + DatabaseContract.TablePreferences.COL_PREF_TYPE + " AS TEXT) = ?",
                    new String[] {"1"},
                    null
            );

            //Log.d("Log", Integer.toString(prefCursor.getCount()));

            if(prefCursor != null && prefCursor.moveToLast()) {
                staticIP = prefCursor.getString(0);
                prefCursor.close();
            } else {
                Log.d(OrderSection.class.getSimpleName(),"prefCursor was nullllllll");
            }

            TCPClient client = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                public void messageReceived(int type, String message) {

                    String jsonResponse = "";
                    publishProgress(jsonResponse);
                    jsonResponse = message;
                    Log.d("TCPClient", jsonResponse);

                    publishProgress(jsonResponse);



                }
            },staticIP,staticPort);


            client.requestToken("new," + JSONParsing.makeJSONTrans(trans[0]));

            return null;


        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Log.d("Dropping off a trans", "It did it!!!");

            if(!values[0].contains("removed")) {

                transactionArrayList = JSONParsing.getTransactionsFromJSON(values[0]);

                Integer something = transactionArrayList.size();
                Log.d("The JSON WORKED!", something.toString());
            }

        }

        @Override
        protected void onPostExecute(TCPClient result) {
            super.onPostExecute(result);

            Log.d("onPostExecute", "dropped off a new trans");


        }

    }
}