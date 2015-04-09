package com.example.testpaydemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class PayActivity extends Activity {
	
	public TextView weiboName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		
		weiboName = (TextView) findViewById(R.id.tv_weiboname);
	}
}
