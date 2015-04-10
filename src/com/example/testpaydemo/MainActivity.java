package com.example.testpaydemo;

import com.example.testpaydemo.utils.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	
	
	private AuthInfo mAuthInfo;
	 /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    
	public Button loginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loginBtn = (Button) findViewById(R.id.btn_login);
		
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(this, mAuthInfo);
	
		
		
		 
		 loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Log.i(TAG, "login button on click");
				 mSsoHandler.authorizeClientSso(new AuthListener());
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		  // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
	}
	
	class AuthListener implements WeiboAuthListener{

		@Override
		public void onCancel() {
			Log.i(TAG, "onCancel");
		}

		@Override
		public void onComplete(Bundle arg0) {
			 // 从 Bundle 中解析 Token
			
	        mAccessToken = Oauth2AccessToken.parseAccessToken(arg0);
	        String uid = mAccessToken.getUid();
	        Log.i(TAG, "onComplete :" +uid);
	        if (mAccessToken.isSessionValid()) {
//	        	UsersAPI user = new UsersAPI(MainActivity.this, Constants.APP_KEY, mAccessToken);
//	        	user.show(uid, new ReListener());
	        	Intent intent = new Intent(MainActivity.this, PayActivity.class);
	        	startActivity(intent);
	        }else{
	        	// 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = arg0.getString("code");
                Log.i(TAG, "login faild : "+code);
                if (!TextUtils.isEmpty(code)) {
                	code = code + "\nObtained the code: " + code;
                }
                Toast.makeText(MainActivity.this, code, Toast.LENGTH_LONG).show();
	        }
			
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Log.i(TAG, "onWeiboException ："+arg0);
		}

	}
	class ReListener implements RequestListener {

		@Override
		public void onComplete(String arg0) {
			Log.i(TAG,"onComplete : "+arg0);
//			Intent intent = new Intent(MainActivity.this, PayActivity.class);
//        	startActivity(intent);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Log.i(TAG,"onWeiboException -- :" + arg0.toString());
		}
		
	}
}
