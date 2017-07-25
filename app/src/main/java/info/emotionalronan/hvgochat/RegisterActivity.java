package info.emotionalronan.hvgochat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.result.AuthResult;
import com.wilddog.wilddogauth.model.WilddogUser;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    private Toolbar mToolbar;

    //
    private ProgressDialog mRegProgress;
    private WilddogAuth mAuth ;

    //创建数据库引用
    private SyncReference ref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegProgress = new ProgressDialog(this);
        mAuth = WilddogAuth.getInstance();
        //获取数据库引用实例
        ref = WilddogSync.getInstance().getReference();


        //设置状态栏
        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.login_email);
        mPassword = (TextInputLayout) findViewById(R.id.login_password);
        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){

                    if(password.length()<6){

                        Toast.makeText(RegisterActivity.this, "密码要大于6位数", Toast.LENGTH_SHORT).show();

                    }else{
                        mRegProgress.setTitle("Register User");

                        mRegProgress.setCanceledOnTouchOutside(false);
                        mRegProgress.show();
                        regiser_user(display_name,email,password);
                    }

                }

            }
        });
    }


    //点击返回按钮 销毁当前界面
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //注册用户
    private void regiser_user(final String display_name, String email, String password) {

        Log.d("3333333","email:"+email +"pass :"+password);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(Task task) {
                        //注册成功
                        if (task.isSuccessful()) {

                            WilddogUser currentUser = WilddogAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            //HashMap 用来存储用户信息
                            HashMap<String,String> userNap = new HashMap<>();

                            userNap.put("name",display_name);
                            userNap.put("status","Hi There I'm using HvgoChat App");
                            userNap.put("image","default");
                            userNap.put("thumb_image","default");

                            ref.child("Users").child(uid).setValue(userNap);

                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();

                            //注册成功后。
                            mRegProgress.dismiss();
                            Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else { //注册失败

                            mRegProgress.hide();
                            Toast.makeText(RegisterActivity.this, "Create User failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("result","reason:"+task.getException().toString());

                        }
                    }
                });

    }
}
