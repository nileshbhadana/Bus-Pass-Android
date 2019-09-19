package com.example.datepicker3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datepicker3.models.Checksum;
import com.example.datepicker3.models.Paytm;
import com.example.datepicker3.utils.WebServiceCaller;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class paytmgateway extends AppCompatActivity {
    String custid="",orderId="",finalamount="",start_date="",last_date="";
    String rescode="";
    Button showqr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytmgateway);
        //showqr=findViewById(R.id.showqr);
        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");
        finalamount= intent.getExtras().getString("finalamount");

        start_date= intent.getExtras().getString("fdate");
        last_date= intent.getExtras().getString("ldate");
        processPaytm();
    }

    private void processPaytm() {

        String custID = custid;
        String orderID = orderId;
        String callBackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderID;
//        amountText.getText().toString().trim(),

        final Paytm paytm = new Paytm(
                "djQsaO72690935904285",
                "WAP",
                finalamount,
                "WEBSTAGING",
                callBackurl,
                "Retail",
                orderID,
                custID
        );

        WebServiceCaller.getClient().getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId()
        ).enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {
                if (response.isSuccessful()) {
                    processToPay(response.body().getChecksumHash(),paytm);
                }
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });


    }

    private void processToPay(String checksumHash, Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , paytm.getmId());
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , paytm.getOrderId());
        paramMap.put( "CUST_ID" , paytm.getCustId());
        paramMap.put( "CHANNEL_ID" , paytm.getChannelId());
        paramMap.put( "TXN_AMOUNT" , paytm.getTxnAmount());
        paramMap.put( "WEBSITE" , paytm.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , paytm.getIndustryTypeId());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put( "CHECKSUMHASH" , checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/


            public void someUIErrorOccurred(String inErrorMessage) {}

            @Override
            public void onTransactionResponse(Bundle inResponse) {
                //Toast.makeText(getApplicationContext(), inResponse.toString(), Toast.LENGTH_LONG).show();
               rescode=inResponse.getString("RESPCODE").toString();
               if(rescode.equals("01"))
                {
                    Toast.makeText(getApplicationContext(), "TRANSACTION SUCCESSFUL!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(paytmgateway.this, qrgenrator.class);
                    intent.putExtra("fdate", start_date);
                    intent.putExtra("ldate", last_date);
                    intent.putExtra("custid", custid);
                    startActivity(intent);
                }
                else if(rescode.equals("235"))
                {
                    Toast.makeText(getApplicationContext(), "TRANSACTION FAILED DUE TO INSUFFICIENT BALANCE!!", Toast.LENGTH_LONG).show();

                }
                else if(rescode.equals("400"))
                {
                    Toast.makeText(getApplicationContext(), "Transaction Pending!!", Toast.LENGTH_LONG).show();
                }
                else{
                   Toast.makeText(getApplicationContext(), "TRANSACTION UNSUCCESSFUL!!", Toast.LENGTH_LONG).show();
               }

            }
            public void networkNotAvailable() {}
            public void clientAuthenticationFailed(String inErrorMessage) {}
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
            public void onBackPressedCancelTransaction() {}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}


        });
    }

}
