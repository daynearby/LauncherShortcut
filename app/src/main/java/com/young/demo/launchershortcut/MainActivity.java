package com.young.demo.launchershortcut;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Function:ShortcutManager 的简单实用
 * Note:
 * Created by daynearby@hotmail.com on 2017-09-10.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ShortcutManager shortcutManager;
    private static final String SHORTCUT_ID_LIKE = "shortcut_id_like";
    private static final String SHORTCUT_ID_COMMENT = "shortcut_id_comment";
    private static final String SHORTCUT_ID_ADD_AND_REMOVE = "shortcut_id_add_and_remove";
    //离线视频
    private static final String SHORTCUT_ID_DOWNLOAD = "shortcut_id_download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Context.SHORTCUT_SERVICE
        shortcutManager = getSystemService(ShortcutManager.class);
        TextView maxShortcutCountPerActivtyTxt = (TextView) findViewById(R.id.txt_max_shortcut_count_per_activity);
        maxShortcutCountPerActivtyTxt.setText(
                getString(R.string.lable_txt_max_count,shortcutManager.getMaxShortcutCountPerActivity()));
        //Using Dynamic Shortcuts
        findViewById(R.id.btn_dynamic).setOnClickListener(this);
        //添加一个
        findViewById(R.id.btn_dynamic_add_one).setOnClickListener(this);
        //移除刚刚添加的那个
        findViewById(R.id.btn_dynamic_remove_one).setOnClickListener(this);
        //设置离线视频的快捷方式不可用
        findViewById(R.id.btn_dynamic_disable).setOnClickListener(this);
        //设置离线视频的快捷方式可用
        findViewById(R.id.btn_dynamic_enable).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dynamic:
                shortcutManager.setDynamicShortcuts(creatShortcutDynamic());
                break;
            case R.id.btn_dynamic_add_one:
                shortcutManager.addDynamicShortcuts(creatOneShortcutDynamic());
                break;
            case R.id.btn_dynamic_remove_one:
                //通过指定的id移除一个shortcut，底层逻辑跟disableShortcuts一致
                shortcutManager.removeDynamicShortcuts(Collections.singletonList(SHORTCUT_ID_ADD_AND_REMOVE));
                break;
            case R.id.btn_dynamic_disable:
                //通过指定的id disable一个shortcut，底层逻辑跟removeDynamicShortcuts一致，快捷方式会被移除
                shortcutManager.disableShortcuts(Collections.singletonList(SHORTCUT_ID_LIKE),"disable shortcut");
                break;
            case R.id.btn_dynamic_enable:
                //该方法只对创建了图标的有效，并不会将原来移除的重新添加回来
                shortcutManager.enableShortcuts(Collections.singletonList(SHORTCUT_ID_LIKE));//静态添加的快捷方式不能用该方法
                break;
        }
        Toast.makeText(this, R.string.toast_op_finish, Toast.LENGTH_SHORT).show();
    }

    /**
     * create shortcut
     *
     * @return shortcutInfo List
     */
    private List<ShortcutInfo> creatShortcutDynamic() {
        List<ShortcutInfo> shortcutInfoList = new ArrayList<ShortcutInfo>();
       /*Intent intent =new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.mysite.example.com/"));*/

        ShortcutInfo likeShortcut = new ShortcutInfo.Builder(this, SHORTCUT_ID_LIKE)
                .setShortLabel(getString((R.string.lable_shortcut_dynamic_like_short)))
                .setLongLabel(getString(R.string.lable_shortcut_dynamic_like_long))
                .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_like))
                .setIntent(new Intent(Intent.ACTION_VIEW)
                        .setClass(this, DynamicShortcutLike.class))//intent必须设置action
                .build();

        ShortcutInfo commentShortcut = new ShortcutInfo.Builder(this, SHORTCUT_ID_COMMENT)
                .setShortLabel(getString(R.string.lable_shortcut_dynamic_comment_short))
                .setLongLabel(getString(R.string.lable_shortcut_dynamic_comment_long))
                .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_comment))
                .setIntent(new Intent(Intent.ACTION_VIEW)
                        .setClass(this, DynamicShortcutComment.class))//intent必须设置action
                .build();

        shortcutInfoList.add(likeShortcut);
        shortcutInfoList.add(commentShortcut);

        return shortcutInfoList;
    }

    private List<ShortcutInfo> creatOneShortcutDynamic() {
        ShortcutInfo shortcut = new ShortcutInfo.Builder(this, SHORTCUT_ID_ADD_AND_REMOVE)
                .setShortLabel(getString((R.string.lable_shortcut_dynamic_add_and_remocve_short)))
                .setLongLabel(getString(R.string.lable_shortcut_dynamic_add_and_remocve_long))
                .setIcon(Icon.createWithResource(this, R.drawable.ic_share_moments_pressed_live))
                .setIntent(new Intent(Intent.ACTION_VIEW)//intent必须设置action
                        .setClass(this,DynamicShortcutAddAndRemove.class))
                .build();
        return Collections.singletonList(shortcut);
    }

}
