package a4tay.com.orderreadyemail;
import java.util.ArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    Spinner carrierSpinner;
    TextView toTextView;
    ArrayList<String> carrier;
    String emailBody = "Please come see the counter for your pickup!";
    TextView bodyTextView;
    String emailSubject = "Your order is ready!";
    TextView subjectTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton send = (FloatingActionButton) this.findViewById(R.id.button1);

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


        carrier = new ArrayList<>();
        carrier.add("@txt.att.net");
        carrier.add("@tmomail.net");
        carrier.add("@vtext.com");
        carrier.add("@pm.sprint.com");
        carrier.add("@vmobl.com");
        carrier.add("@mmst5.tracfone.com");
        carrier.add("@mymetropcs.com");
        carrier.add("@myboostmobile.com");
        carrier.add("@mms.cricketwireless.net");

        carrierSpinner = (Spinner) findViewById(R.id.sp_carrier_spinner);

        ArrayAdapter<String> carrierAdapter = new ArrayAdapter<>(
                getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                carrierSelector
        );
        carrierSpinner.setAdapter(carrierAdapter);

        toTextView = (TextView) findViewById(R.id.editText3);

        bodyTextView = (TextView) findViewById(R.id.editText5);
        bodyTextView.setText(emailBody);

        subjectTextView = ((TextView) findViewById(R.id.editText4));
        subjectTextView.setText(emailSubject);

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i("SendMailActivity", "Send Button Clicked.");



                String toEmail = toTextView.getText().toString().trim();
                emailSubject = subjectTextView.getText().toString().trim();


                String fromEmail = "4tay.studio@gmail.com";
                String fromPassword = "h4ll0W##N";

                emailBody = bodyTextView.getText().toString().trim();
                new SendMailTask(MainActivity.this).execute(fromEmail,
                        fromPassword, (toEmail + carrier.get(carrierSpinner.getSelectedItemPosition())), emailSubject, emailBody);
            }
        });
    }
}
