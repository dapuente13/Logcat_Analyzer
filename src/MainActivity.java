package com.app.netcat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    final SendData[] s = new SendData[1];

    protected TextView status;
    protected EditText ip, port, filter;

    protected ArrayList<PackageInfoStruct> userApps;
    protected ArrayList<PackageInfoStruct> systemApps;

    protected Button stop;

    class PackageInfoStruct {
        private String appname;
        private String pname;
        private Drawable icon;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        ip = findViewById(R.id.ip);
        port = findViewById(R.id.port);
        filter = findViewById(R.id.filter);

        userApps = new ArrayList<>();
        systemApps = new ArrayList<>();
        getInstalledApps(false);

        final Button userAppsFilter = findViewById(R.id.button5);
        userAppsFilter.setOnClickListener(v -> filterApps(true));

        final Button systemAppsFilter = findViewById(R.id.button6);
        systemAppsFilter.setOnClickListener(v -> filterApps(false));

        final Button start = findViewById(R.id.button);
        start.setOnClickListener(v -> start());

        stop = findViewById(R.id.button2);
        stop.setOnClickListener(v -> stop());

    }

    protected void start(){
        if(ip.getText().toString() != "" && port.getText().toString() != ""){
            status.setText("Running");
            stop.setText("Stop");
            s[0] = (SendData) new SendData(this, false).execute(ip.getText().toString(), port.getText().toString(), filter.getText().toString());
        }
        else{
            status.setText("Ip/Port empty");
        }
    }

    protected void stop(){
        if(!s[0].getStop()){
            status.setText("Stopped");
            s[0].setStop(true);
            stop.setText("Get Results");
        }
        else{
            s[0] = (SendData) new SendData(this, true).execute(ip.getText().toString(), port.getText().toString(), filter.getText().toString());
        }
    }

    private void filterApps(boolean user) {
        List<CharSequence> charSequences = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int max;

        if (user) {
            max = userApps.size();
            for (int i = 0; i < max; i++) {
                charSequences.add(new String(userApps.get(i).pname));
            }
        }
        else {
            max = systemApps.size();
            for (int i = 0; i < max; i++) {
                charSequences.add(new String(systemApps.get(i).pname));
            }
        }

        CharSequence[] apps = charSequences.toArray(new CharSequence[charSequences.size()]);

        builder.setTitle("Select a package to filter");
        builder.setItems(apps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstString = filter.getText().toString();
                filter.setText(firstString + apps[which]+ ",");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getInstalledApps(boolean getSysPackages) {
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for(int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            PackageInfoStruct newInfo = new PackageInfoStruct();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                userApps.add(newInfo);
            else
                systemApps.add(newInfo);
        }
    }
}