package dominika.launcher.AllAppsGrid;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;

import dominika.launcher.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Domi on 25.10.2016.
 *
 * Model of an application object which stores all needed information about an app retrieved from system.
 */

public class AppModel extends Object {
    private final Context mContext;
    private final ApplicationInfo mAppInfo;

    private String mAppLabel;
    private Drawable mAppIcon;

    private boolean mMounted;
    private final File mApkFile;

    private String mCategory;

    public AppModel(Context context, ApplicationInfo info) {
        mContext = context;
        mAppInfo = info;

        //Directory of an app
        mApkFile = new File(info.sourceDir);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate("package", mAppLabel);
            jsonObject.accumulate("category", mCategory);
        }catch (Exception ignored){
        }
        return jsonObject;
    }

    // Get info about app
    public ApplicationInfo getAppInfo() {
        return mAppInfo;
    }

    // Get package name
    public String getApplicationPackageName() {
        return getAppInfo().packageName;
    }

    // Get label (name) of an app
    public String getLabel() {
        return mAppLabel;
    }


    // Get icon of an app
    public Drawable getIcon() {
        if (mAppIcon == null) {
            if (mApkFile.exists()) {
                mAppIcon = mAppInfo.loadIcon(mContext.getPackageManager());
                return mAppIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            // If the app wasn't mounted but is now mounted, reload
            // its icon.
            if (mApkFile.exists()) {
                mMounted = true;
                mAppIcon = mAppInfo.loadIcon(mContext.getPackageManager());
                return mAppIcon;
            }
        } else {
            return mAppIcon;
        }

        return mContext.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }

    public void loadLabel(Context context) {
        if (mAppLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mAppLabel = mAppInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mAppInfo.loadLabel(context.getPackageManager());
                mAppLabel = label != null ? label.toString() : mAppInfo.packageName;
            }
        }
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }
}
