package com.luckyliuqs.mymusic.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.luckyliuqs.mymusic.MainActivity;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.StringUtil;
import com.luckyliuqs.mymusic.Util.ToastUtil;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.Session;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.event.LoginSuccessEvent;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册类：继承BaseTitleActivity，将会显示返回图标和标题信息
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
        //判断输入的用户名称是否为空
        if(StringUtils.isBlank(nickname)){
            //提示请输入用户昵称
            ToastUtil.showSortToast(getActivity(),R.string.enter_nickname);
            return;
        }
        //判断输入的用户名称是否包含空格
        if (nickname.contains(" ")){
            //提示昵称不能包含空格
            ToastUtil.showSortToast(getActivity(),R.string.nickname_space);
            return;
        }

        phone = et_phone.getText().toString();
        //判断输入的手机号是否为空
        if(StringUtils.isBlank(phone)){
            //提示请输入手机号
            ToastUtil.showSortToast(getActivity(),R.string.hint_phone);
            return;
        }
        //判断输入的手机号是否正确
        if(!StringUtil.isPhone(phone)){
            //提示手机号格式错误
            ToastUtil.showSortToast(getActivity(),R.string.hint_error_phone);
            return;
        }

        password = et_password.getText().toString();
        //判断输入的密码是否为空
        if(StringUtils.isBlank(password)){
            //提示请输入密码
            ToastUtil.showSortToast(getActivity(),R.string.hint_password);
        }

        if(!StringUtil.isPassword(password)){
            //提示密码格式错误
            ToastUtil.showSortToast(getActivity(),R.string.hint_error_password_format);
        }

        //创建User对象
        User user = new User();
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setPassword(password);
        //设置用户登录类型为手机号
        user.setType(User.TYPE_PHONE);

        Api.getInstance().register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<Session>>(getActivity()){
                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });
    }

    /**
     * 注册成功后开始登录
     * @param session
     */
    public void next(Session session){
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setType(User.TYPE_PHONE);

        Api.getInstance().login(user)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<Session>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {
                        super.onSucceeded(data);
                        sp.setToken(data.getData().getToken());
                        sp.setUserId(data.getData().getId());
                        sp.setIMToken(data.getData().getIm_token());
                        startActivityAfterFinishThis(MainActivity.class);

                        //发布登陆成功信息，登陆界面自动关闭
                        EventBus.getDefault().post(new LoginSuccessEvent());
                    }
                });

    }

}
