package ru.mycollectioncivilwar.list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.mycollectioncivilwar.R;
import ru.mycollectioncivilwar.settings.SettingsActivity;
import ru.mycollectioncivilwar.utils.DBHelper;

import static ru.mycollectioncivilwar.App.LOG_TAG;

public class ListActivity extends AppCompatActivity implements CategoryRVAdapter.OnAddListener {

    private int currID;
    private int parentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // изменение темы splashscreen на обычную
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "ListActivity is created");
        setContentView(R.layout.activity_list);
        // запуск идёт с главной категории, поэтому ID = 1
        currID = 1;
        setSupportActionBar(findViewById(R.id.toolbar));
        // поиск RecyclerView и установка слушателей
        RecyclerView main = findViewById(R.id.main);
        main.setLayoutManager(new LinearLayoutManager(this));
        DBHelper.getInstance(this);
        updateList();
        initializeAd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void initializeAd() {
        AdView adView = findViewById(R.id.ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("update", false)) updateList();
    }

    private void updateList() {
        // обновление списка
        Log.i(LOG_TAG, "Getting data from database...");
        ListUpdater updater = new ListUpdater(currID, this);
        updater.setOnLoadListener((newType, parent, newAdapter) -> parentID = parent);
        updater.execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.settings:
                Log.i(LOG_TAG, "Settings button clicked");
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case android.R.id.home:
                Log.i(LOG_TAG, "Back button on toolbar selected");
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void goBack() {
        if (currID == 1) finish();
        else {
            currID = parentID;
            updateList();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public void loadNewCategory(int id) {
        currID = id;
        updateList();
    }
}
