package info.emotionalronan.hvgochat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;

public class MainActivity extends AppCompatActivity {

    private WilddogAuth wilddogAuth ;
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private TabLayout mTabLyout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WilddogOptions options = new WilddogOptions.Builder().setSyncUrl("https://hvgochat.wilddogio.com").build();
        WilddogApp.initializeApp(this, options);

        //添加数据存储 创建 Sync 实例
        SyncReference ref = WilddogSync.getInstance().getReference();


        //获取 WilddogAuth 实例
        wilddogAuth = WilddogAuth.getInstance();

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
         getSupportActionBar().setTitle("HvgoChat");


         //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tab_pager);

        // 创建ViewPager  并添加适配器
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLyout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLyout.setupWithViewPager(mViewPager);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        WilddogUser currentUser = wilddogAuth.getCurrentUser();

        if(currentUser == null){
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //加载menu
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_btn){

            //退出登录
            WilddogAuth.getInstance().signOut();
            //刷新UI
            sendToStart();
        }

        return true;
    }
}
