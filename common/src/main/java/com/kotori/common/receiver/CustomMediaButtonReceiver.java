package com.kotori.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by le.xin on 2018/9/25.
 *
 * @author le.xin
 */
public class CustomMediaButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("CustomMediaButtonReceiver.onReceive  " + intent);
    }
}
