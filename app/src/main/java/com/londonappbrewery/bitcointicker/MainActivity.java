package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import cz.msebera.android.httpclient.Header;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;



import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private String url;
    // Member Variables:
    TextView mPriceTextView;
    TextView mChangeTextView;
    ImageView mBitcoin;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);
        mChangeTextView = findViewById(R.id.changeLabel);
        mBitcoin = findViewById(R.id.logoImage);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mBitcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter += 1;
                if (counter == 10){
                    Glide.with(view)
                            .load("https://mir-s3-cdn-cf.behance.net/user/276/22f544123434251.5a4d5a04cbf0f.jpg")
                            .apply(RequestOptions.circleCropTransform())
                            .into(mBitcoin);
                } if (counter == 11){
                    counter = 0;
                    mBitcoin.setImageResource(R.drawable.bitcoin_picture);

                }
            }
        });

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                Log.d("bitbit", "" + parent.getItemAtPosition(position));
                url = BASE_URL + parent.getItemAtPosition(position);
                letsDoSomeNetworking(url);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d("bitbit", "Nothing was selected in Spinner");

            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("bitbit", "JSON: " + response.toString());

                String value = null;
                try {
                    value = response.getString("ask");
                    Log.d("bitbit", value);
                    mPriceTextView.setText(value);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                String change = null;
                try {
                    change = response.getJSONObject("changes").getJSONObject("percent").getString("year");
                    Log.d("bitbit", change);
                    mChangeTextView.setText(change);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("bitbit", "Request fail! Status code: " + statusCode);
                Log.d("bitbit", "Fail response: " + response);
                Log.e("ERROR", e.toString());
            }
        });


    }


}
