package a4tay.com.orderreadyemail;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by johnkonderla on 2/10/17.
 */

public final class JSONParsing {

    private static ArrayList<Transaction> transactions;
    public static final String LOG_TAG = JSONParsing.class.getSimpleName();

    private JSONParsing() {

    }

    public static ArrayList<Transaction> getTransactionsFromJSON(String json) {

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
        ArrayList<String> carrier = new ArrayList<>();

        carrier.add("@txt.att.net");
        carrier.add("@tmomail.net");
        carrier.add("@vtext.com");
        carrier.add("@messaging.sprintpcs.com");
        carrier.add("@vmobl.com");
        carrier.add("@mmst5.tracfone.com");
        carrier.add("@mymetropcs.com");
        carrier.add("@myboostmobile.com");
        carrier.add("@mms.cricketwireless.net");



        transactions = new ArrayList<>();

        try {
            JSONObject fullOb = new JSONObject(json);

            JSONArray allTrans = fullOb.optJSONArray("transactions");

            if(allTrans!= null) {
                for(int i = 0; i < allTrans.length(); i++) {
                    JSONObject firstTrans = allTrans.getJSONObject(i);
                    JSONObject trans = firstTrans.getJSONObject("trans");
                    String transID = trans.getString("transID");
                    String custName = trans.getString("custName");
                    String custNumb = trans.getString("custNumb");
                    String custCarrierName = trans.getString("custCarrier");

                    String custCarrier = "";

                    for(int j = 0; j < carrierSelector.size(); j++ ) {
                        if(custCarrierName.equalsIgnoreCase(carrierSelector.get(j))) {
                            custCarrier = carrier.get(j);
                        }
                    }
                    if(custCarrier.equals("")) {
                        custCarrier = carrier.get(0);
                    }

                    Log.d("JSONParsing",custCarrier);

                    Transaction addTrans = new Transaction(custName,custNumb,custCarrier,transID);

                    transactions.add(addTrans);
                }
            }


        }
        catch (JSONException e) {
            Log.d(LOG_TAG + " Error parsing JSON", e.getMessage());
        }

        return transactions;
    }

    public static String makeJSONTrans(Transaction trans) {
        JSONObject fulllOb = new JSONObject();

        try {
            fulllOb.put("transID",trans.getOrder());
            fulllOb.put("custName",trans.getName());
            fulllOb.put("custNumb",trans.getPhone());
            fulllOb.put("custCarrier",trans.getCarrier());
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Error!!! " + e.getMessage());
        }

        return fulllOb.toString();
    }
}




