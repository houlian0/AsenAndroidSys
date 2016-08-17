package com.asen.android.lib.base.module.service;

import android.app.Service;
import android.os.Binder;


/**
 * BaseBinder »ù´¡µÄBinder
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public class BaseBinder extends Binder {

    private Service mService;

    public BaseBinder(Service service) {
        mService = service;
    }

    public Service getService() {
        return mService;
    }

}
