package com.example.lalaecomerce.order_history;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.lalaecomerce.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderItem> orderItems;

    public OrderHistoryAdapter(Context context, ArrayList<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return orderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        }

        OrderItem item = orderItems.get(position);

        TextView orderID = convertView.findViewById(R.id.orderID);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemQuantity = convertView.findViewById(R.id.itemQuantity);
        TextView itemPrice = convertView.findViewById(R.id.itemPrice);

        orderID.setText("Order ID: " + item.getOrderID());
        itemName.setText("Item: " + item.getItemName());
        itemQuantity.setText("Qty: " + item.getQuantity());
        itemPrice.setText("Total: $" + String.format("%.2f", item.getPrice()));

        return convertView;
    }
}
