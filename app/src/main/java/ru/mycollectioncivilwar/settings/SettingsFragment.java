package ru.mycollectioncivilwar.settings;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import ru.mycollectioncivilwar.R;
import ru.mycollectioncivilwar.utils.Utils;

import static ru.mycollectioncivilwar.App.LOG_TAG;

@SuppressWarnings("WeakerAccess")
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    // добавление настроек
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(Objects.requireNonNull(getActivity()), R.xml.preferences, false);
    }

    // убирает отступ под иконку во всем списке
    @NonNull
    @Override
    protected RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen) {
            @SuppressLint("RestrictedApi")
            @Override
            public void onBindViewHolder(@NonNull PreferenceViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (getItem(position) instanceof PreferenceCategory)
                    setZeroPaddingToLayoutChildren(holder.itemView);
                else
                    holder.itemView.findViewById(R.id.icon_frame).setVisibility(getItem(position).getIcon() == null ? View.GONE : View.VISIBLE);
            }
        };
    }

    private void setZeroPaddingToLayoutChildren(View view) {
        if (!(view instanceof ViewGroup)) return;
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            setZeroPaddingToLayoutChildren(viewGroup.getChildAt(i));
            viewGroup.setPaddingRelative(0, viewGroup.getPaddingTop(), viewGroup.getPaddingEnd(), viewGroup.getPaddingBottom());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // установка текста версии
        String versionName = "unknown";
        try {
            versionName = Objects.requireNonNull(getActivity()).getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Preference versionPref = findPreference("version");
        versionPref.setTitle(String.format(getResources().getString(R.string.version), versionName));
        // запрет на overscroll, без него выглядит лучше
        getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @NonNull String key) {
        if (key.equals("text_size")) {
            // изменение размера шрифта
            Log.i(LOG_TAG, "Text size changed");
            Utils.updateFontScale(Objects.requireNonNull(getActivity()));
            // перезапуск активити для изменения размера шрифта в ней
            getActivity().recreate();
        }
    }
}
