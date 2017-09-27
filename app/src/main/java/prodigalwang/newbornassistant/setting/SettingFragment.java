package prodigalwang.newbornassistant.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.text.InputType;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseFragmentActivity;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.SPHelper;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/28.
 */

public class SettingFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private CheckBoxPreference net_mode, update_mode, notice_mode;
    private Preference clear_cache, open_source, help, feed_back, agreement, check_update;

    private static final String key_clear_cache = "key_clear_cache";
    private static final String key_feed_back = "key_feed_back";
    private static final String key_help = "key_help";
    private static final String key_open_source_license = "key_open_source_license";
    private static final String key_agreement = "key_agreement";
    private static final String key_check_update = "key_check_update";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        net_mode = (CheckBoxPreference) findPreference(getString(R.string.key_net_mode));
        update_mode = (CheckBoxPreference) findPreference(getString(R.string.key_update_mode));
        notice_mode = (CheckBoxPreference) findPreference(getString(R.string.key_notice_mode));
        net_mode.setOnPreferenceChangeListener(this);
        update_mode.setOnPreferenceChangeListener(this);
        notice_mode.setOnPreferenceChangeListener(this);

        clear_cache = findPreference(getString(R.string.key_clear_cache));
        open_source = findPreference(getString(R.string.key_open_source_license));
        help = findPreference(getString(R.string.key_help));
        feed_back = findPreference(getString(R.string.key_feed_back));
        agreement = findPreference(getString(R.string.key_agreement));
        check_update = findPreference(getString(R.string.key_check_update));

        clear_cache.setOnPreferenceClickListener(this);
        open_source.setOnPreferenceClickListener(this);
        help.setOnPreferenceClickListener(this);
        feed_back.setOnPreferenceClickListener(this);
        agreement.setOnPreferenceClickListener(this);
        check_update.setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean checked = Boolean.valueOf(newValue.toString());

        SPHelper.getInstance().saveData(preference.getKey(), checked);
        LogUtil.e("Pref " + preference.getKey() + " changed to " + checked);
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case key_clear_cache:
                showConfirmDialog();
                preference.setSummary("已清理缓存");
                break;
            case key_feed_back:
                break;
            case key_check_update:
                ToastUtil.showShort(getActivity(),"点击检查更新");
                break;
            case key_help:
                break;
            case key_open_source_license:
                Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
                intent.putExtra(getString(R.string.fragment_tag), getString(R.string.fragment_open_source));
                getActivity().startActivity(intent);
                break;
            case key_agreement:
                break;
            default:
                break;
        }
        return true;
    }

    private void showConfirmDialog() {

//        new MaterialDialog.Builder(getActivity())
//                .iconRes(R.drawable.ic_importent_hint)
//                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
//                .title("您将删除缓存的文件,该操作不可逆")
//                .content("是否删除?")
//                .positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                ToastUtil.showShort(getActivity(),"点击确定");
//            }
//        })
//                .negativeText("取消")
//                .show();

        new MaterialDialog.Builder(getActivity())
                .content("请稍后")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();

    }
}
