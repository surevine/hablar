package com.calclab.hablar.core.client.avatars;

import com.calclab.hablar.core.client.mvp.Display;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;

public interface AvatarDisplay extends Display {
	/**
	 * Sets the url to be displayed for the avatar
	 * @param url
	 */
	void setAvatarUrl(String url);
	
	/**
	 * Can be used to detect when the mouse has moved over the avatar
	 * @return
	 */
	HasMouseOverHandlers mouseOver();
	
	/**
	 * Can be used to detect when the mouse has moved out of the avatar
	 * @return
	 */
	HasMouseOutHandlers mouseOut();

	/**
	 * Gets the size code of the required avatar. Can be used to customise the url
	 * so that the correct size image is generated for the display.
	 * @return
	 */
	String getSize();
}
