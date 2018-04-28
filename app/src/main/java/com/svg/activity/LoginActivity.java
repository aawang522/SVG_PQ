package com.svg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoshihai.xxdialog.DialogViewHolder;
import com.luoshihai.xxdialog.XXDialog;
import com.svg.R;
import com.svg.utils.CommUtil;
import com.svg.utils.SPUtils;
import com.svg.utils.SysCode;

/**
 * Created by Administrator on 2017/3/21.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText mUsername;
    EditText mUserPhonenum;
    TextView mBtnLogin;
    LinearLayout button_createUser;
    LinearLayout button_changePassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        mUsername = (EditText) findViewById(R.id.username);
        mUserPhonenum = (EditText) findViewById(R.id.user_phonenum);
        mBtnLogin = (TextView) findViewById(R.id.btn_login);
        button_createUser = (LinearLayout) findViewById(R.id.button_createUser);
        button_changePassw = (LinearLayout) findViewById(R.id.button_changePassw);

        mBtnLogin.setOnClickListener(this);
        button_createUser.setOnClickListener(this);
        button_changePassw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                LoglinJiaoYan();
                break;
            // 创建用户
            case R.id.button_createUser:
                showCreateUserDialog();
                break;
            // 修改用户名
            case R.id.button_changePassw:
                showUpdatePassDialog();
                break;
        }
    }

    /**
     * 登录校验
     */
    private void LoglinJiaoYan() {
        // 用户名
        String phoneNum = mUsername.getText().toString().trim();
        // 密码
        String pwd = mUserPhonenum.getText().toString().trim();
        if(TextUtils.isEmpty(SPUtils.getString(LoginActivity.this, SysCode.USER_LOGIN_XM)) &&
                TextUtils.isEmpty(SPUtils.getString(LoginActivity.this, SysCode.USER_LOGIN_MM))) {
            Toast.makeText(this, "请先创建用户", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if(phoneNum.equals(SPUtils.getString(LoginActivity.this, SysCode.USER_LOGIN_XM))
                    && pwd.equals(SPUtils.getString(LoginActivity.this, SysCode.USER_LOGIN_MM))){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "用户名和密码不正确", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示创建用户对话框
     */
    private void showCreateUserDialog(){
        // 引用lsh的github的万能对话框
        final XXDialog dialog = new XXDialog(LoginActivity.this, R.layout.view_dialog_createuser) {
            @Override
            public void convert(DialogViewHolder dialogViewHolder) {
                final EditText createuser_super = (EditText)dialogViewHolder.getView(R.id.createuser_super);
                final EditText createuser_superpass = (EditText)dialogViewHolder.getView(R.id.createuser_superpass);
                final EditText createuser_username = (EditText)dialogViewHolder.getView(R.id.createuser_username);
                final EditText createuser_password = (EditText)dialogViewHolder.getView(R.id.createuser_password);
                // 确认按钮监听
                dialogViewHolder.setOnClick(R.id.createConfirm, new View.OnClickListener(){@Override
                public void onClick(View view) {
                    if (CommUtil.isNotBlank(createuser_super.getText().toString().trim()) &&
                            CommUtil.isNotBlank(createuser_superpass.getText().toString().trim()) &&
                            CommUtil.isNotBlank(createuser_username.getText().toString().trim()) &&
                            CommUtil.isNotBlank(createuser_password.getText().toString().trim())) {
                        if(SysCode.USER_SUPERNAME.equals(createuser_super.getText().toString().trim()) &&
                                SysCode.USER_SUPERPASS.equals(createuser_superpass.getText().toString().trim())) {
                            // 保存输入的ip和端口
                            SPUtils.put(LoginActivity.this, SysCode.USER_LOGIN_XM, createuser_username.getText().toString());
                            SPUtils.put(LoginActivity.this, SysCode.USER_LOGIN_MM, createuser_password.getText().toString());
                            dismiss();
                            CommUtil.showToast(LoginActivity.this, "新用户创建成功");
                        } else {
                            CommUtil.showToast(LoginActivity.this, "超级用户不正确");
                        }
                    } else {
                        CommUtil.showToast(LoginActivity.this, "请输入超级用户和新用户及密码");
                    }
                }
                });
                // 取消按钮监听
                dialogViewHolder.setOnClick(R.id.createCancel, new View.OnClickListener(){@Override
                public void onClick(View view) {
                    dismiss();
                }
                });
            }
        };
        // 设置外界点击不取消对话框
        dialog.setCancelAble(false);
        int width = CommUtil.getScreenWidth(this) * 3/4;
        int height = CommUtil.getScreenHeight(this) * 1/2;
        dialog.backgroundLight(0.5).setWidthAndHeight(width, height).showDialog();
    }

    /**
     * 显示修改密码对话框
     */
    private void showUpdatePassDialog(){
        // 引用lsh的github的万能对话框
        final XXDialog dialog = new XXDialog(LoginActivity.this, R.layout.view_dialog_updatapassword) {
            @Override
            public void convert(DialogViewHolder dialogViewHolder) {
                final EditText updateOldName = (EditText)dialogViewHolder.getView(R.id.updateOldName);
                final EditText updateOldPass = (EditText)dialogViewHolder.getView(R.id.updateOldPass);
                final EditText updateNewPass = (EditText)dialogViewHolder.getView(R.id.updateNewPass);
                // 确认按钮监听
                dialogViewHolder.setOnClick(R.id.updateConfirm, new View.OnClickListener(){@Override
                public void onClick(View view) {
                    if (CommUtil.isNotBlank(updateOldName.getText().toString().trim()) &&
                            CommUtil.isNotBlank(updateOldPass.getText().toString().trim()) &&
                            CommUtil.isNotBlank(updateNewPass.getText().toString().trim())) {
                        if(SPUtils.getString(LoginActivity.this, SysCode.USER_LOGIN_XM)
                                .equals(updateOldName.getText().toString().trim()) &&
                                SPUtils.getString(LoginActivity.this, SysCode.USER_LOGIN_MM)
                                        .equals(updateOldPass.getText().toString().trim())) {
                            // 保存输入的ip和端口
                            SPUtils.put(LoginActivity.this, SysCode.USER_LOGIN_MM, updateNewPass.getText().toString());
                            dismiss();
                            CommUtil.showToast(LoginActivity.this, "密码修改成功");
                        } else {
                            CommUtil.showToast(LoginActivity.this, "原密码不正确，请重新输入");
                        }
                    } else {
                        CommUtil.showToast(LoginActivity.this, "请输入原信息和新密码");
                    }
                }
                });
                // 取消按钮监听
                dialogViewHolder.setOnClick(R.id.updateCancel, new View.OnClickListener(){@Override
                public void onClick(View view) {
                    dismiss();
                }
                });
            }
        };
        // 设置外界点击不取消对话框
        dialog.setCancelAble(false);
        int width = CommUtil.getScreenWidth(this) * 3/4;
        int height = CommUtil.getScreenHeight(this) * 1/2;
        dialog.backgroundLight(0.5).setWidthAndHeight(width, height).showDialog();
    }

    /**
     * 获取存储的ip和端口
     */
    private void getDefaultIPPort(EditText editIP, EditText editPort){
        editIP.setText(SPUtils.getString(LoginActivity.this, SysCode.SET_IP));
        editIP.setSelection(editIP.getText().toString().length());
        editPort.setText(SPUtils.getString(LoginActivity.this, SysCode.SET_PORT));
    }

}
