package a4tay.com.orderreadyemail;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import a4tay.com.orderreadyemail.Adapter.OrderRecyclerAdapter;

import static a4tay.com.orderreadyemail.MainActivity.bodies;
import static a4tay.com.orderreadyemail.MainActivity.carrier;
import static a4tay.com.orderreadyemail.MainActivity.custCarrierList;
import static a4tay.com.orderreadyemail.MainActivity.custNameList;
import static a4tay.com.orderreadyemail.MainActivity.numberList;
import static a4tay.com.orderreadyemail.MainActivity.selectedEmail;
import static a4tay.com.orderreadyemail.MainActivity.subjects;
import static a4tay.com.orderreadyemail.MainActivity.transList;

/**
 * Created by johnkonderla on 2/10/17.
 */

public class OrderSection extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        custNameList = new ArrayList<>();
        numberList = new ArrayList<>();
        transList = new ArrayList<>();
        custCarrierList = new ArrayList<>();

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


        final View rootView = inflater.inflate(R.layout.order_list, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        final RecyclerView orderRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_order_list);

        final OrderRecyclerAdapter orderRecyclerAdapter = new OrderRecyclerAdapter() {
            @Override
            public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(MyHolder holder, int position) {
                holder.custName.setText(custNameList.get(position));
                holder.number.setText(numberList.get(position));
                holder.trans.setText(transList.get(position));

            }

            @Override
            public int getItemCount() {
                if (custNameList != null && custNameList.size() > 0) {
                    return custNameList.size();
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

                        new SendMailTask(getActivity()).execute(fromEmail,
                                fromPassword, (numberList.get(position) + custCarrierList.get(position)), subjects.get(selectedEmail), bodies.get(selectedEmail) + transList.get(position));

                        Log.d("LOG", fromEmail + " " + fromPassword + " " + (numberList.get(position) + custCarrierList.get(position)) + " " + subjects.get(selectedEmail) + " " + bodies.get(selectedEmail) + transList.get(position));


                        custNameList.remove(position);
                        numberList.remove(position);
                        transList.remove(position);
                        custCarrierList.remove(position);

                        orderRecyclerAdapter.notifyItemRemoved(position);
                        orderRecyclerAdapter.notifyItemRangeChanged(position, custNameList.size());
                    }

                    @Override
                    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        if (viewHolder instanceof OrderRecyclerAdapter.MyHolder) {
                            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                            Log.d("LOG", "Recording swipe....");
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
                            Log.d("LOG", "Removing????");
                        }
                    }

                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        getDefaultUIUtil().onDraw(c, recyclerView, ((OrderRecyclerAdapter.MyHolder) viewHolder).getSwipableView(), dX, dY, actionState, isCurrentlyActive);
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
                        //new GetOrdersTaskSide().execute("");



                        orderRecyclerAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


        return rootView;
    }

}
