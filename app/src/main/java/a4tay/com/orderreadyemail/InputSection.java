package a4tay.com.orderreadyemail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static a4tay.com.orderreadyemail.MainActivity.carrier;
import static a4tay.com.orderreadyemail.MainActivity.custCarrierList;
import static a4tay.com.orderreadyemail.MainActivity.custNameList;
import static a4tay.com.orderreadyemail.MainActivity.numberList;
import static a4tay.com.orderreadyemail.MainActivity.transList;

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
        carrierSelector.add("Spring");
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
                    custNameList.add(name);
                    numberList.add(number);
                    transList.add(transNumb);
                    custCarrierList.add(carrier.get(carrierSpinner.getSelectedItemPosition()));

                    toTextView.setText("");
                    nameTextView.setText("");
                    transTextView.setText("");
                    carrierSpinner.setId(0);
                }


            }
        });


        return rootView;
    }
}