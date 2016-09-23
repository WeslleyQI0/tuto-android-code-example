package com.otr.www.databackup;

import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewShow;
    private EditText key,value;
    private Button restore, backup;
    private boolean restoreFlag;
    private String restoreKey = "restoreKey";

    SharedPreferences preferences;
    BackupManager manager;
    RestoreObserver observer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewShow = (TextView) findViewById(R.id.textViewShow);
        key = (EditText) findViewById(R.id.editTextKey);
        value = (EditText) findViewById(R.id.editTextValue);
        restore = (Button) findViewById(R.id.restore);
        backup = (Button) findViewById(R.id.backup);
        restore.setOnClickListener(this);
        backup.setOnClickListener(this);


        preferences = getSharedPreferences("myPrefrences",Context.MODE_PRIVATE);


        manager = new BackupManager(this);
        observer = new RestoreObserver() {
            @Override
            public void restoreStarting(int numPackages) {
                super.restoreStarting(numPackages);
            }

            @Override
            public void onUpdate(int nowBeingRestored, String currentPackage) {
                super.onUpdate(nowBeingRestored, currentPackage);
                showToast("backuping");

            }

            @Override
            public void restoreFinished(int error) {
                super.restoreFinished(error);
                showToast("restore finish");
                getData();

        }
        };

        if (preferences.contains(restoreKey)) {

        }else {
            preferences.edit().putBoolean(restoreKey, true).commit();
            manager.dataChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.restore:
                manager.requestRestore(observer);

                break;
            case R.id.backup:
                preferences.edit().putString(key.getText().toString(), value.getText().toString()).commit();
                manager.dataChanged();

                break;

        }
    }

    public void getData() {
        textViewShow.setText(null);
        Map<String, ?> map = new HashMap<String, Object>();
        map = preferences.getAll();
        int size = map.size();
        Set<String> stringSet = map.keySet();
        String[] args = stringSet.toArray(new String[0]);
        while (size > 0) {
            String key = args[size - 1];
            String value = map.get(key).toString();
            textViewShow.append(key+":"+value+"\n");
            size--;

        }
    }
}
