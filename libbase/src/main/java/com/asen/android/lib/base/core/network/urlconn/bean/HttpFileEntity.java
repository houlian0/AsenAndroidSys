package com.asen.android.lib.base.core.network.urlconn.bean;

import java.io.File;

/**
 * Simple to Introduction
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpFileEntity {

	private String name;

	private File value;

	public HttpFileEntity(String name, File value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getValue() {
		return value;
	}

	public void setValue(File value) {
		this.value = value;
	}

}
