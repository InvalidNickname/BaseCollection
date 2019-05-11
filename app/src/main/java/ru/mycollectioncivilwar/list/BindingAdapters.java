package ru.mycollectioncivilwar.list;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.databinding.BindingAdapter;
import androidx.preference.PreferenceManager;
import ru.mycollectioncivilwar.R;
import ru.mycollectioncivilwar.utils.RoundCornerTransformation;

@SuppressWarnings("WeakerAccess")
public class BindingAdapters {

    // установка иконки категории
    @BindingAdapter("app:bind_icon_path")
    public static void loadImage(@NonNull ImageView view, String iconPath) {
        switch (iconPath) {
            case "nothing":
                Picasso.get().load(R.drawable.example_flag).into(view);
                break;
            case "no icon":
                Picasso.get().load(R.drawable.no_icon).into(view);
                break;
            default:
                Picasso.get().load("file:///android_asset/images/" + iconPath.replace("jpg", "webp")).transform(new RoundCornerTransformation(12)).into(view);
                break;
        }
    }

    // установка слушателей
    @BindingAdapter("app:bind_listener")
    public static void setListeners(ConstraintLayout layout, @NonNull final Category category) {
        final Context context = layout.getContext();
        final CategoryRVAdapter.OnAddListener onAddListener = (CategoryRVAdapter.OnAddListener) context;
        layout.setOnClickListener(v -> onAddListener.loadNewCategory(category.getId()));
    }

    // установка размера иконки сдвигом guideline
    @BindingAdapter("app:guideline_percent")
    public static void setGuideline(Guideline guideline, @NonNull Context context) {
        guideline.setGuidelinePercent(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("icon_size", false) ? 0.25f : 0.2f);
    }
}
