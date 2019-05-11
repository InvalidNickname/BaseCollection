package ru.mycollectioncivilwar.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.Objects;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.mycollectioncivilwar.R;

public class Utils {

    public static void updateFontScale(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.fontScale = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("text_size", false) ? 1.15f : 1;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        context.getResources().updateConfiguration(configuration, metrics);
    }

    public static void runLayoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.list_animation);
        recyclerView.setLayoutAnimation(controller);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
