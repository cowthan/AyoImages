package com.lzy.imagepickerdemo;

import android.os.Bundle;

import org.ayo.sample.menu.Leaf;
import org.ayo.sample.menu.MainPagerActivity;
import org.ayo.sample.menu.Menu;
import org.ayo.sample.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainnActivity extends MainPagerActivity {

    private List<Menu> menus;

    @Override
    public List<Menu> getMenus() {
        return menus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        menus = new ArrayList<Menu>();

        ///--------------------------菜单1：View
        Menu m1 = new Menu("图片加载", R.drawable.weixin_normal, R.drawable.weixin_pressed);
        menus.add(m1);
        {
            MenuItem menuItem1 = new MenuItem("网络图", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m1.addMenuItem(menuItem1);
            {
                menuItem1.addLeaf(new Leaf("UIL", "", null));
                menuItem1.addLeaf(new Leaf("Picasso", "", null));
                menuItem1.addLeaf(new Leaf("Glide", "", null));
                menuItem1.addLeaf(new Leaf("Fresco", "", null));
                menuItem1.addLeaf(new Leaf("自己写个", "", null));
            }

            menuItem1 = new MenuItem("其他", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m1.addMenuItem(menuItem1);
            {
                menuItem1.addLeaf(new Leaf("下载", "", null));
                menuItem1.addLeaf(new Leaf("保存", "", null));
                menuItem1.addLeaf(new Leaf("缓存", "", null));
                menuItem1.addLeaf(new Leaf("压缩", "", null));
                menuItem1.addLeaf(new Leaf("gif", "", null));
                menuItem1.addLeaf(new Leaf("wepb", "", null));
                menuItem1.addLeaf(new Leaf("svg", "", null));
            }
        }

        ///--------------------------菜单1：开源
        Menu m3 = new Menu("选图", R.drawable.find_normal, R.drawable.find_pressed);
        menus.add(m3);
        {
            MenuItem menuItem = new MenuItem("相册", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("MediaChooser风格", "", PickerMediaChooser.class, 1));
                menuItem.addLeaf(new Leaf("ImagePicker风格", "", ImagePickerActivity.class, 1));
            }

            menuItem = new MenuItem("相机", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("系统相机", "", null));
                menuItem.addLeaf(new Leaf("SquareCamera", "", null));
            }

            menuItem = new MenuItem("裁剪", R.drawable.weixin_normal, R.drawable.weixin_pressed);
            m3.addMenuItem(menuItem);
            {
                menuItem.addLeaf(new Leaf("系统", "", null));
                menuItem.addLeaf(new Leaf("第三方", "", null));
            }
        }

    }
}
