package com.asen.android.lib.base.core.gps.extension;

/**
 * 扩展的单次定位，由定位主体主动调用{@link IExtensionLocation#getLocation()}定位方法获取最新的坐标信息进行定位，时间间隔固定
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class ExtensionASingleLocation extends BaseExtensionLocation {

    @Override
    public final void start() {
    }

    @Override
    public final void stop() {
    }

}