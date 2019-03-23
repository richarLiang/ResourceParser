package com.resourceparser.resourceparser;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText packageName;
    EditText stringName;
    Spinner spinner;
    TextView textView;
    Button button;
    PackageManager pm;
    Resources otherRes;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        pm = getPackageManager();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pack = packageName.getText().toString().trim();
                String string = stringName.getText().toString().trim();
                String type = spinner.getSelectedItem().toString();
                showParseResult(pack, type,string);
            }
        });

    }

    private void showParseResult(String pack, String type, String string) {
        try {
            otherRes = pm.getResourcesForApplication(pack);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could not find package " + pack, Toast.LENGTH_LONG).show();
        }
        if(otherRes != null) {
            int resid = otherRes.getIdentifier(string, type, pack);
            if(resid != 0) {
                if("mipmap".equals(type)||"drawable".equals(type)){
                    Drawable drawable = otherRes.getDrawable(resid, null);
                    imageView.setImageDrawable(drawable);
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageDrawable(null);
                                }
                            });
                        }
                    }.start();
                }else {
                    String result = getParseResult(resid, type);
                    textView.setText("The resid=" + resid + "/0x" + Integer.toHexString(resid)
                            + " type=" + type
                            + " name=" + string
                            + " result=" + result);
                }
            }else{
                Toast.makeText(MainActivity.this, "resid=0", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getParseResult(int resid, String type) {
        switch (type){
            case "bool":
                return String.valueOf(otherRes.getBoolean(resid));
            case "string":
                return otherRes.getString(resid);
            case "dimen":
                return String.valueOf(otherRes.getDimension(resid));
            case "integer":
                return String.valueOf(otherRes.getInteger(resid));
            case "color":
                return String.valueOf(otherRes.getColor(resid));
            default:
                return "---------type error!----------";

        }
    }

    private void findView(){
        packageName = findViewById(R.id.editText);
        stringName = findViewById(R.id.editText2);
        spinner = findViewById(R.id.spinner);
        textView = findViewById(R.id.textView4);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
    }
}
