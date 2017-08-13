package samsung.membership.splash;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by KyuYeol on 2017-08-09.
 */

public class WifiCheckActivity extends AppCompatActivity implements ShowWifiMonitor.OnChangeNetworkStatusListener{

    private TextView wifiConnected;
    private ShowWifiMonitor showWifiMonitor;

    private boolean startCheck = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(showWifiMonitor);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        showWifiMonitor = new ShowWifiMonitor(getApplicationContext());

        wifiConnected = (TextView)findViewById(R.id.wifi_connected);

        showWifiMonitor.setOnChangeNetworkStatusListener(this);
        registerReceiver(showWifiMonitor, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(showWifiMonitor, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));

    }

    @Override
    public void OnChanged(int status) {
        if(status == ShowWifiMonitor.WIFI_ENABLED) {
            if(!startCheck) {
                Log.d("CHECKCHECK", "CHECK");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                WifiCheckActivity.this.finish();
                startCheck = !startCheck;
            }
        } else {
            wifiConnected.setText("SETTING PLZ");
        }
    }
}
