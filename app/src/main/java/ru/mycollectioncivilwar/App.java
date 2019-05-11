package ru.mycollectioncivilwar;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import ru.mycollectioncivilwar.utils.Utils;

public class App extends Application {

    public static final String LOG_TAG = "ru.mycollectioncivilwar";
    public static final int USES_DB_VERSION = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, "ca-app-pub-2853509457699224~1770127572");
        Utils.updateFontScale(this);
    }
}
