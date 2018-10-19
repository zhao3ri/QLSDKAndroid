package com.qinglan.sdk.android.net;

import android.os.AsyncTask;

import com.qinglan.sdk.android.common.Log;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public class HttpConnectionTask extends AsyncTask<IRequestInfo, Void, String> {
    private boolean isSuccess = false;
    private OnResponseListener mListener;

    public HttpConnectionTask setResponseListener(OnResponseListener listener) {
        mListener = listener;

        return this;
    }

    @Override
    protected String doInBackground(IRequestInfo... requests) {
        IRequestInfo request = requests[0];
        String result;
        try {
            result = HttpRequest.execute(request);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
            isSuccess = false;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("result = " + s);
        if (mListener != null) {
            mListener.onResponse(isSuccess, s);
        }
    }
}
