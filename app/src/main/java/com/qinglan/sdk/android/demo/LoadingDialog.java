package com.qinglan.sdk.android.demo;


import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 加载对话框
 * @author zhaoyl
 *
 */
public class LoadingDialog extends Dialog{
	public TextView mTvMessage;
	public ProgressBar mBar;
	public LoadingDialog(Context context) {
		super(context,ResUtil.getStyle(context, "Dialog"));
		this.setCanceledOnTouchOutside(false);
		setContentView(ResUtil.getLayout(context, "dialog_loading"));
		mTvMessage = (TextView) findViewById(ResUtil.getId(context, "loading_message"));
		mBar = (ProgressBar)findViewById(ResUtil.getId(context, "loading_pb"));
	}
	
	public LoadingDialog(Context context, CharSequence message) {
		this(context);
		setMessage(message);
	}

	public void setMessage(CharSequence message) {
		mTvMessage.setText(message);
	}

}
