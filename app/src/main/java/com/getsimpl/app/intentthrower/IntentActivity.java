package com.getsimpl.app.intentthrower;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

    }

    public void onStartClicked(View view) {
        startIntent();
    }

    public void startIntent() {
        String intentAction = ((TextView) findViewById(R.id.intentAction)).getText().toString();
        Intent intent = new Intent(intentAction);
        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            ((TextView) findViewById(R.id.exceptionText)).setText(e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((TextView) findViewById(R.id.activityResultCode)).setText(Html.fromHtml(getResultCode(resultCode)));
        ((TextView) findViewById(R.id.activityResultData)).setText(Html.fromHtml(getResultData(data)));
    }

    // TODO: Display logic needs to be cleaner
    private String getResultCode(int resultCode) {
        return String.format("<b>Result Code:</b> %s</br>", resultCode);
    }

    @NonNull
    private String getResultData(Intent data) {
        String resultData;
        if (data == null) {
            resultData = "<b>Result Intent:</b> NULL";
        } else {
            resultData = "<b>Result Intent:</b><br>\n";
            resultData = resultData.concat(String.format("<b>Type</b>: %s <br>\n", data.getType()));
            resultData = resultData.concat(String.format("<b>Action</b>: %s <br>\n", data.getAction()));
            resultData = resultData.concat(getExtrasDisplayData(data));
        }
        return resultData;
    }

    private String getExtrasDisplayData(Intent data) {
        Bundle extras = data.getExtras();
        String resultData = "";
        if (extras != null) {
            resultData = resultData.concat(String.format("<b>Extras:</b> %s<br>\n", extras.keySet().size()));
            for (String key: extras.keySet()) {
                Object extraValue = extras.get(key);
                String displayValue = "NULL";
                String extraClass = "?";
                if (extraValue != null) {
                    extraClass = extraValue.getClass().getCanonicalName();
                    if (extraValue.getClass().isArray()) {
                        List<Object> objects = buildListFromArrayObject(extraValue);
                        if (objects.size() >= 5) {
                            objects = objects.subList(0, 4);
                            objects.add("...");
                        }
                        displayValue = Arrays.toString(objects.toArray());
                    } else {
                        displayValue = extraValue.toString();
                    }
                }
                resultData = resultData.concat(String.format("\u00A0\u00A0\u00A0<b>%s (%s):</b> %s<br>\n", key, extraClass, displayValue));
            }
        } else {
            resultData = resultData.concat("<b>Extras: NULL</b>");
        }
        return resultData;
    }

    private ArrayList<Object> buildListFromArrayObject(Object arrayAsObject) {
        Array.getLength(arrayAsObject);
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < Array.getLength(arrayAsObject); i++) { //  No Java 8 FML!
            list.add(Array.get(arrayAsObject, i));
        }
        return list;
    }
}
