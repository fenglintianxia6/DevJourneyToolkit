package dev.journey.toolkitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.journey.apptoolkit.update.DataInterceptor;
import dev.journey.apptoolkit.update.UpgradeClient;
import dev.journey.apptoolkit.update.UpgradeInfoProvider;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String url = "http://www.qufenqi.com/api/v2/newversion?version=1.7.0&device=android";
        UpgradeClient upgradeClient = new UpgradeClient(this, "http://dev4.apitest.qufenqi.com/", new DataInterceptor() {
            @Override
            public UpgradeInfoProvider intercept(Object data) {
                return null;
            }
        });
        upgradeClient.checkUpgrade(url, new HashMap<String, String>(1));
    }

    public void test(View v) {
        startActivity(new Intent(this, TestActivity.class));
    }
}
