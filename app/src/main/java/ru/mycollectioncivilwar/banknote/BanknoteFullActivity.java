package ru.mycollectioncivilwar.banknote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.mycollectioncivilwar.R;
import ru.mycollectioncivilwar.list.ListActivity;
import ru.mycollectioncivilwar.utils.DBHelper;

import static ru.mycollectioncivilwar.App.LOG_TAG;
import static ru.mycollectioncivilwar.utils.DBHelper.COLUMN_CIRCULATION;
import static ru.mycollectioncivilwar.utils.DBHelper.COLUMN_COUNTRY;
import static ru.mycollectioncivilwar.utils.DBHelper.COLUMN_DESCRIPTION;
import static ru.mycollectioncivilwar.utils.DBHelper.COLUMN_ID;
import static ru.mycollectioncivilwar.utils.DBHelper.COLUMN_NAME;
import static ru.mycollectioncivilwar.utils.DBHelper.COLUMN_OBVERSE;
import static ru.mycollectioncivilwar.utils.DBHelper.COLUMN_REVERSE;
import static ru.mycollectioncivilwar.utils.DBHelper.TABLE_BANKNOTES;

public class BanknoteFullActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private String name, circulationTime, obversePath, reversePath, description, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "BanknoteFullActivity is created");
        database = Objects.requireNonNull(DBHelper.getInstance(this)).getDatabase();
        setContentView(R.layout.activity_banknote_full);
        getData();
        setToolbar();
        setData();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void getData() {
        Log.i(LOG_TAG, "Getting data...");
        int banknoteID = getIntent().getIntExtra("id", 1);
        Cursor c = database.query(TABLE_BANKNOTES, null, COLUMN_ID + " = " + banknoteID, null, null, null, null);
        c.moveToFirst();
        name = c.getString(c.getColumnIndex(COLUMN_NAME));
        circulationTime = c.getString(c.getColumnIndex(COLUMN_CIRCULATION));
        obversePath = c.getString(c.getColumnIndex(COLUMN_OBVERSE));
        reversePath = c.getString(c.getColumnIndex(COLUMN_REVERSE));
        description = c.getString(c.getColumnIndex(COLUMN_DESCRIPTION));
        country = c.getString(c.getColumnIndex(COLUMN_COUNTRY));
        c.close();
        Log.i(LOG_TAG, "Data is got");
    }

    private void setData() {
        Log.i(LOG_TAG, "Setting data");
        // выделение жирным названия пункта списка
        SpannableStringBuilder country = new SpannableStringBuilder(String.format(getString(R.string.country_s), this.country));
        country.setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.country_s).length() - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.countryText)).setText(country);
        SpannableStringBuilder circulation = new SpannableStringBuilder(String.format(getString(R.string.circulation_time_s), this.circulationTime));
        circulation.setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.circulation_time_s).length() - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.circulationText)).setText(circulation);
        SpannableStringBuilder description = new SpannableStringBuilder(String.format(getString(R.string.description_s), this.description));
        description.setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.description_s).length() - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) findViewById(R.id.descriptionText)).setText(description);
        if (!obversePath.equals("nothing")) {
            Picasso.get().load("file:///android_asset/images/" + obversePath.replace("jpg", "webp")).into((ImageView) findViewById(R.id.obverseImage));
        } else
            Picasso.get().load(R.drawable.example_banknote).into((ImageView) findViewById(R.id.obverseImage));
        if (!reversePath.equals("nothing")) {
            Picasso.get().load("file:///android_asset/images/" + reversePath.replace("jpg", "webp")).into((ImageView) findViewById(R.id.reverseImage));
        } else
            Picasso.get().load(R.drawable.example_banknote).into((ImageView) findViewById(R.id.reverseImage));
        Log.i(LOG_TAG, "Data is set");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("update", false);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Log.i(LOG_TAG, "Back button on toolbar selected, finishing");
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // открытие увеличенного изображения
    public void openImage(View view) {
        Intent intent = new Intent(this, BanknoteImageActivity.class);
        boolean start = true;
        switch (view.getId()) {
            case R.id.reverseImage:
                if (reversePath.equals("nothing"))
                    start = false;
                else
                    intent.putExtra("image", reversePath);
                break;
            case R.id.obverseImage:
                if (obversePath.equals("nothing"))
                    start = false;
                else
                    intent.putExtra("image", obversePath);
                break;
        }
        intent.putExtra("name", name);
        if (start)
            startActivity(intent);
    }
}
