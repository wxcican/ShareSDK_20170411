package com.fuicuiedu.xc.sharesdk_20170411;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

public class MainActivity extends AppCompatActivity {
    private Platform weibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShareSDK.initSDK(this);

        findViewById(R.id.main_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aaa();
            }
        });

        findViewById(R.id.main_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //移除授权
                weibo.removeAccount(true);
            }
        });



    }

    //手动授权（用于第三方登录）
    private void aaa(){
        weibo = ShareSDK.getPlatform(QZone.NAME);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weibo.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                Log.e("aaa","登录失败");
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                Log.e("aaa","登录成功");
                //输出所有授权信息
                arg0.getDb().exportData();

                handler.sendEmptyMessage(0);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                Log.e("aaa","取消登录");
                // TODO Auto-generated method stub
            }
        });
        //authorize与showUser单独调用一个即可
//        weibo.authorize();//单独授权,OnComplete返回的hashmap是空的

        weibo.showUser(null);//授权并获取用户信息
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, "登录成功，用户昵称= " + weibo.getDb().getUserName(), Toast.LENGTH_SHORT).show();
        }
    };

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
    //关闭sso授权
        oks.disableSSOWhenAuthorize();

    // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
    // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
    // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
    // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
    // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
    // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

    // 启动分享GUI
        oks.show(this);
    }
}
