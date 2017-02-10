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

    private JSONParsing() {

    }

    public static ArrayList<Transaction> getTransactionsFromJSON(String json) {


        transactions = new ArrayList<>();

        try {
            JSONObject fullOb = new JSONObject(json);

            JSONArray allTrans = fullOb.optJSONArray("transactions");

            if(allTrans!= null) {
                for(int i = 0; i < allTrans.length(); i++) {
                    JSONObject firstTrans = allTrans.getJSONObject(i);
                    JSONObject trans = firstTrans.getJSONObject("trans");
                    Integer transID = trans.getInt("transID");
                    String custName = trans.getString("custName");
                    Integer custNumb = trans.getInt("custNumb");
                    String custCarrier = trans.getString("custCarrier");

                    Transaction addTrans = new Transaction(custName,custNumb.toString(),custCarrier,transID.toString());

                    transactions.add(addTrans);
                }
            }


        }
        catch (JSONException e) {
            Log.d("Error parsing JSON", e.getMessage());
        }

        return transactions;
    }
}




