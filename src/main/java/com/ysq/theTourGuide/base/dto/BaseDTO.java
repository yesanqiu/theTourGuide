package com.ysq.theTourGuide.base.dto;



import net.sf.json.JSONObject;

import java.io.Serializable;


public abstract class BaseDTO implements Serializable {

	private static final long serialVersionUID = 4035984879657965692L;

	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}

}
