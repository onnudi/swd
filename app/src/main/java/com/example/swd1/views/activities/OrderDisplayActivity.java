package com.example.swd1.views.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swd1.R;
import com.example.swd1.models.entities.Order;
import com.example.swd1.models.entities.OrderDetail;
import com.example.swd1.presenters.OrderDisplayPresenter;
import com.example.swd1.utils.CommonConstant;
import com.example.swd1.views.OrderDisplayViewListener;
import com.example.swd1.views.adapters.OrderDisplayaAdapter;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import java.text.DecimalFormat;

import dmax.dialog.SpotsDialog;

public class OrderDisplayActivity extends AppCompatActivity implements
        OrderDisplayViewListener, OrderDisplayaAdapter.OnCallback {

    private Button btnPayment, btnOrderMore;
    private TextView txtTotalPrice;

    private OrderDisplayaAdapter adapter;
    private OrderDisplayPresenter presenter;
    private RecyclerView lvOrder;
    private SharedPreferences preferences;
    private AlertDialog dialog;

//    private HubConnection hubConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_display);

        presenter = new OrderDisplayPresenter(this, this);

//        hubConnection = HubConnectionBuilder.create("http://192.168.57.7:4545/testhub").build();
//        hubConnection.start();
//
//        hubConnection.on("UpdateTableStatus", (id)->{
//
//        }, Integer.class);


        initView();

        preferences = getSharedPreferences(CommonConstant.APP_SHARE_PREFERENCE, MODE_PRIVATE);
        int tableId = preferences.getInt(CommonConstant.CURRENT_TABLE_ID, CommonConstant.INVALID_INT);

        dialog.show();
        presenter.loadListOrderByTable(tableId);

    }

    private void initView() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage(R.string.waiting)
                .build();

        lvOrder = findViewById(R.id.lv_order);
        btnPayment = findViewById(R.id.btn_order_pay);
        btnOrderMore = findViewById(R.id.btn_order_more);
        txtTotalPrice = findViewById(R.id.txt_order_total);
        lvOrder.setHasFixedSize(true);
        lvOrder.setLayoutManager(new LinearLayoutManager(this));

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                presenter.requestPayment(preferences.getInt(CommonConstant.CURRENT_ORDER_ID, CommonConstant.INVALID_INT));
            }
        });

        btnOrderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MasterCategoryActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onGetListOrderSuccess(Order body) {

        double totalPrice = 0;

        for (OrderDetail item : body.getDetails()) {
            totalPrice += item.getTotalAmount();
        }

        txtTotalPrice.setText("Tổng : " + CommonConstant.currencyFormat(totalPrice));

        preferences.edit().putInt(CommonConstant.CURRENT_ORDER_ID, body.getOrderId()).commit();
        adapter = new OrderDisplayaAdapter(body.getDetails(), this);
        lvOrder.setAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onConnectFailed() {
        Toast.makeText(this, R.string.connect_to_server_failed, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onRequestPaymentSuccess() {
        dialog.dismiss();
        Toast.makeText(this, R.string.complete_request_payment, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onItemClick() {

    }
}
