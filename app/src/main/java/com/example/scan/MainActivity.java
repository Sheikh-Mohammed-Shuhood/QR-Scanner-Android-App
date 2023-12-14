package com.example.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Button scan_btn;
    TextView textView, textView2;
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan_btn=findViewById(R.id.scanner);
        textView=findViewById(R.id.text);
        textView2=findViewById(R.id.text2);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray userArray = obj.getJSONArray("teams");

            for (int i = 0; i < userArray.length(); i++) {

                textView2.setText("hello");
                // create a JSONObject for fetching single user data
                JSONObject userDetail = userArray.getJSONObject(i);
                // fetch email and name and store it in arraylist
                list.add(userDetail.getString("UID"));
//                textView2.setText(userDetail.getString("UID"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            String contents = intentResult.getContents();
            if(contents != null){
                textView2.setText("Status");
                textView.setText(intentResult.getContents());
                Iterator<String> itr = list.iterator();
                while (itr.hasNext()) {
                    String element = itr.next();
                    if(element.equalsIgnoreCase(intentResult.getContents())){
                        textView2.setText("Success");
                    }
                }
//                try {
//                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentResult.getContents()));
//                    startActivity(myIntent);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(this, "No application can handle this request."
//                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
            }
            else{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("sample.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}