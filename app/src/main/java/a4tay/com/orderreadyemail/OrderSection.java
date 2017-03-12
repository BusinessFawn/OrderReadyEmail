package a4tay.com.orderreadyemail;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.Runnable;

import java.util.ArrayList;

import a4tay.com.orderreadyemail.Adapter.OrderRecyclerAdapter;
import a4tay.com.orderreadyemail.DB.DatabaseContract;

import static a4tay.com.orderreadyemail.MainActivity.bodies;
import static a4tay.com.orderreadyemail.MainActivity.carrier;
import static a4tay.com.orderreadyemail.MainActivity.selectedEmail;
import static a4tay.com.orderreadyemail.MainActivity.serverIP;
import static a4tay.com.orderreadyemail.MainActivity.serverPort;
import static a4tay.com.orderreadyemail.MainActivity.subjects;
import static a4tay.com.orderreadyemail.MainActivity.transactionArrayList;

/**
 * Created by johnkonderla on 2/10/17.
 */

public class OrderSection extends Fragment {

    View orderView;
    ItemTouchHelper itemTouchHelper;
    public OrderRecyclerAdapter orderRecyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView orderRecyclerView;
    public static int staticPort = 4060;
    public static String staticIP = "dev.4tay.xyz";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        carrier.add("@txt.att.net");
        carrier.add("@tmomail.net");
        carrier.add("@vtext.com");
        carrier.add("@messaging.sprintpcs.com");
        carrier.add("@vmobl.com");
        carrier.add("@mmst5.tracfone.com");
        carrier.add("@mymetropcs.com");
        carrier.add("@myboostmobile.com");
        carrier.add("@mms.cricketwireless.net");


        orderView = inflater.inflate(R.layout.order_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) orderView.findViewById(R.id.swipe_refresh_layout);

        orderRecyclerView = (RecyclerView) orderView.findViewById(R.id.rv_order_list);




        orderRecyclerAdapter = new OrderRecyclerAdapter() {
            @Override
            public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(final MyHolder holder, int position) {
                holder.custName.setText(transactionArrayList.get(position).getName());
                holder.number.setText(transactionArrayList.get(position).getPhone());
                holder.trans.setText(transactionArrayList.get(position).getOrder());

                final LinearLayout myWrap = holder.orderItemWrap;

                holder.orderItemWrap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Log.d(OrderSection.class.getSimpleName(),Integer.toString(holder.increaseStatus(holder.getStatus())));

                        int stat = holder.increaseStatus(holder.getStatus());

                        /*if(stat == 1) {
                            myWrap.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                        } else if (stat == 2){
                            myWrap.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent2));
                        } else {
                            myWrap.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));
                        }*/

                        Log.d("OrderSection",Integer.toString(holder.getStatus()));


                    }
                });
            }



            @Override
            public int getItemCount() {
                if (transactionArrayList != null && transactionArrayList.size() > 0) {
                    return transactionArrayList.size();
                } else {
                    return 0;
                }
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT) {
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        // move item in `fromPos` to `toPos` in adapter.
                        return true;// true if moved, false otherwise
                    }

                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        // remove from adapter
                        Log.d("LOG", "Completed!!!");
                        int position = viewHolder.getAdapterPosition();

                        String fromEmail = "4tay.studio@gmail.com";
                        String fromPassword = "h4ll0W##N";
                        String subject;
                        String body;


                        if(subjects == null || subjects.size() < 1 || bodies == null || bodies.size() < 1){
                            subject = "Your order is ready!";
                            body = "Please present receipt to collect order:";
                        }
                        else {
                            subject = subjects.get(selectedEmail);
                            body = bodies.get(selectedEmail);
                        }

                        String whatWay = "";
                        switch (direction) {
                            case 16:
                                whatWay = "left";

                                Toast.makeText(getContext(),"Trans: " + transactionArrayList.get(position).getOrder() + " removed!",Toast.LENGTH_SHORT).show();



                                Log.d("LOG", fromEmail + " " + fromPassword + " " + (transactionArrayList.get(position).getPhone() + transactionArrayList.get(position).getCarrier()) + " " + subject + " " + body + transactionArrayList.get(position).getOrder());
                                break;
                            case 32:
                                whatWay = "right";

                                Log.d("LOG", fromEmail + " " + fromPassword + " " + (transactionArrayList.get(position).getPhone() + transactionArrayList.get(position).getCarrier()) + " " + subject + " " + body + transactionArrayList.get(position).getOrder());

                                new SendMailTask(getActivity()).execute(fromEmail,
                                        fromPassword, (transactionArrayList.get(position).getPhone() + transactionArrayList.get(position).getCarrier()),
                                        subject,
                                        body + transactionArrayList.get(position).getOrder());



                                Toast.makeText(getContext(),"Trans: " + transactionArrayList.get(position).getOrder() + " sent!",Toast.LENGTH_SHORT).show();

                                break;
                        }
                        Log.d("Order Section", whatWay);


                        new GetOrdersTask().execute(transactionArrayList.get(position).getOrder() + ".json");

                        transactionArrayList.remove(position);

                        /*orderRecyclerAdapter.notifyItemRemoved(position);
                        orderRecyclerAdapter.notifyItemRangeChanged(position, transactionArrayList.size());*/

                        refreshOrderList();
                    }

                    @Override
                    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        if (viewHolder instanceof OrderRecyclerAdapter.MyHolder) {
                            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                            Log.d("LOG", "Recording swipe...." + Integer.toString(swipeFlags));
                            return makeMovementFlags(0, swipeFlags);
                        } else
                            return 0;
                    }

                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        getDefaultUIUtil().clearView(((OrderRecyclerAdapter.MyHolder) viewHolder).getSwipableView());
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        if (viewHolder != null) {
                            getDefaultUIUtil().onSelected(((OrderRecyclerAdapter.MyHolder) viewHolder).getSwipableView());
                            Log.d("LOG", "Removing????" + Integer.toString(actionState));
                        }
                    }

                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        getDefaultUIUtil().onDraw(c, recyclerView, ((OrderRecyclerAdapter.MyHolder) viewHolder).getSwipableView(), dX, dY, actionState, isCurrentlyActive);

                        System.out.println("dX: " + String.valueOf(dX));
                        LinearLayout itemBackground = (LinearLayout) viewHolder.itemView.findViewById(R.id.ll_order_item_background);
                        ImageView cancelSymbol = (ImageView) viewHolder.itemView.findViewById(R.id.tv_cancel);
                        ImageView sendSymbol = (ImageView) viewHolder.itemView.findViewById(R.id.tv_send);

                        if(dX < 0) {
                            itemBackground.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.cancelOrder));
                            cancelSymbol.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.order_cancelled));
                            sendSymbol.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.clear));

                        }
                        else if(dX > 0) {
                            itemBackground.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.sendOrder));
                            sendSymbol.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.order_complete));
                            cancelSymbol.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.clear));
                        }

                    }

                    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        getDefaultUIUtil().onDrawOver(c, recyclerView, ((OrderRecyclerAdapter.MyHolder) viewHolder).getSwipableView(), dX, dY, actionState, isCurrentlyActive);
                    }
                });
        itemTouchHelper.attachToRecyclerView(orderRecyclerView);
        orderRecyclerView.setAdapter(orderRecyclerAdapter);
        orderRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        orderRecyclerView.setLayoutManager(layoutManager);
        orderRecyclerView.addItemDecoration(new a4tay.com.orderreadyemail.Adapter.DividerItemDecoration(getContext()));

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        /*final String request = "{\"transactions\":[{\"trans\":{\"transID\":1,\"custName\":\"DanielMontilla\",\"custNumb\":9194379712,\"custCarrier\":\"Verizon\"}},{\"trans\":{\"transID\":2,\"custName\":\"JohnKonderla\",\"custNumb\":83233033767,\"custCarrier\":\"ATT\"}},{\"trans\":{\"transID\":3,\"custName\":\"ZackBrown\",\"custNumb\":9198559494,\"custCarrier\":\"Sprint\"}},{\"trans\":{\"transID\":4,\"custName\":\"LindseyCross\",\"custNumb\":9194177592,\"custCarrier\":\"T-mobile\"}}]}";
                        new GetOrdersTask().execute(request);*/

                        Log.d("OrderSection","onRefresh");

                        new GetOrdersTask().execute("orders");



                        /*orderRecyclerAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);*/
                    }
                }
        );

        Log.d("ORDER SECTION","this");
        /*final String request = "{\"transactions\":[{\"trans\":{\"transID\":1,\"custName\":\"DanielMontilla\",\"custNumb\":9194379712,\"custCarrier\":\"Verizon\"}},{\"trans\":{\"transID\":2,\"custName\":\"JohnKonderla\",\"custNumb\":83233033767,\"custCarrier\":\"ATT\"}},{\"trans\":{\"transID\":3,\"custName\":\"ZackBrown\",\"custNumb\":9198559494,\"custCarrier\":\"Sprint\"}},{\"trans\":{\"transID\":4,\"custName\":\"LindseyCross\",\"custNumb\":9194177592,\"custCarrier\":\"T-mobile\"}}]}";
        new GetOrdersTask().execute(request);*/

        new GetOrdersTask().execute("orders");


        return orderView;
    }

    public void refreshOrderList() {
        orderRecyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public class GetOrdersTask extends AsyncTask<String,String,TCPClient> {


        @Override
        protected TCPClient doInBackground(String... args) {

            //String serverIPRefresh = "192.168.0.21";
            //String serverIPRefresh = "192.168.1.66";

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
                    if (message.contains(".json")) {
                        Log.d("TCPClient", "removed: " + message);
                        new GetOrdersTask().execute("orders");
                    }

                    else {
                        jsonResponse = message;
                        Log.d("TCPClient", jsonResponse);

                        publishProgress(jsonResponse);
                    }



                }
            },staticIP,staticPort);
            client.requestToken(args[0]);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            if(!values[0].contains("removed")) {

                transactionArrayList = JSONParsing.getTransactionsFromJSON(values[0]);

                Integer something = transactionArrayList.size();
                Log.d("The JSON WORKED!", something.toString());
            }

        }

        @Override
        protected void onPostExecute(TCPClient result) {
            super.onPostExecute(result);

            Log.d("onPostExecute", "Got a new list!");

            refreshOrderList();

        }
    }

}
