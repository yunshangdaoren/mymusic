package com.luckyliuqs.mymusic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterActivity extends BaseTitleActivity {
    /**
     * 用户名信息
     */
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    /**
     * 手机号信息
     */
    @BindView(R.id.et_phone)
    EditText et_phone;
    /**
     * 账户密码信息
     */
    @BindView(R.id.et_password)
    EditText et_password;

    //用户名称
    private String nickname;
    //用户手机号
    private String phone;
    //账户密码
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();
    }

    /**
     * 注册按钮点击事件
     */
    @OnClick(R.id.bt_register)
    public void bt_register(){
        nickname = et_nickname.getText().toString();
        //判断用户名称是否为空
        if(StringUtils.isBlank(nickname)){
            ToastUtil.showSortToast(getActivity(),R.string.enter_nickname);
            return;
        }
        //判断用户名称是否包含空格
        if (nickname.contains(" ")){
            ToastUtil.showSortToast(getActivity(),R.string.nickname_space);
            return;
        }

        phone = et_phone.getText().toString();
        if(StringUtils.isBlank(phone)){
            
        }

    }


}
