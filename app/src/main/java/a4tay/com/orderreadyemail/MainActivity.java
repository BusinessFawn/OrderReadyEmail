package a4tay.com.orderreadyemail;
import java.util.ArrayList;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.support.v4.app.FragmentPagerAdapter;


public class MainActivity extends FragmentActivity implements android.app.ActionBar.TabListener {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;



    static ArrayList<String> custNameList;
    static ArrayList<String> numberList;
    static ArrayList<String> transList;
    static ArrayList<String> custCarrierList;
    static ArrayList<String> carrier;

    static ArrayList<String> subjects;
    static ArrayList<String> bodies;
    static int selectedEmail;

    private static String[] tabs = {"Order List", "Add Order", "Settings"};


    @Override
    public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction
            fragmentTransaction) {
    }

    @Override
    public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());


        final android.app.ActionBar actionBar = getActionBar();


        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // Here is where the meat of the app lives. Create and Send to the server.
                    return new OrderSection();


                case 1:
                    //add orders here.
                    return new InputSection();
                default:

                    // Set IP and Port here
                    return new SettingsSection();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }


    public class GetOrdersTaskSide extends AsyncTask {

        //private ProgressDialog statusDialog;
        //private Activity getOrdersActivity;

        private ArrayList<Transaction> transactions;

        public GetOrdersTaskSide() {

        }

        protected void onPreExecute() {
            /*statusDialog = new ProgressDialog(getOrdersActivity);
            statusDialog.setMessage("Getting ready...");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();*/
        }

        @Override
        protected Object doInBackground(Object... args) {
                    /*try {
                        Log.i(LOG_TAG, "about to connect to the server");
                        publishProgress("Processing input....");

                        TCPClient tcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                            @Override
                            public void messageReceived(int type, String message) {
                                String jsonResponse = message;
                            }
                        }, args[0].toString(),
                                Integer.getInteger(args[1].toString()));

                        publishProgress("Got all orders");
                        Log.i(LOG_TAG, "Mail Sent.");
                    } catch (Exception e) {
                        publishProgress(e.getMessage());
                        Log.e(LOG_TAG, e.getMessage(), e);
                    }*/

            transactions = JSONParsing.getTransactionsFromJSON("{\"transactions\":[{\"trans\":{\"transID\":1,\"custName\":\"DanielMontilla\",\"custNumb\":9194379712,\"custCarrier\":\"Verizon\"}},{\"trans\":{\"transID\":2,\"custName\":\"JohnKonderla\",\"custNumb\":83233033767,\"custCarrier\":\"ATT\"}},{\"trans\":{\"transID\":3,\"custName\":\"ZackBrown\",\"custNumb\":9198559494,\"custCarrier\":\"Sprint\"}},{\"trans\":{\"transID\":4,\"custName\":\"LindseyCross\",\"custNumb\":9194177592,\"custCarrier\":\"T-mobile\"}}]}");
            Integer something = transactions.size();
            Log.d("The JSON WORKED! ", something.toString());


            return null;
        }

        @Override
        public void onProgressUpdate(Object... values) {
            //statusDialog.setMessage(values[0].toString());

        }

        @Override
        public void onPostExecute(Object result) {
            /*statusDialog.dismiss();*/
        }
    }
}
