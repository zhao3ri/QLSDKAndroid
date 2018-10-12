package com.qinglan.sdk.http;

import android.os.AsyncTask;

import com.qinglan.sdk.common.Log;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
public class RequestTask extends AsyncTask<IRequest, Void, String> {
    private boolean isSuccess = false;
    private OnRequestFinishedListener mListener;

    public RequestTask setFinishedListener(OnRequestFinishedListener listener) {
        mListener = listener;

        return this;
    }

    @Override
    protected String doInBackground(IRequest... requests) {
        IRequest request = requests[0];
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
            mListener.onFinished(isSuccess, s);
        }
    }
}
