package com.calclab.hablar.core.client.avatars;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class AvatarWidget extends Composite implements AvatarDisplay {
	
	FlowPanel avatarPanel;
	
	Image avatarImage;
	
	String size;
	
	public @UiConstructor AvatarWidget(final String size) {
		this.size = size;
		
		avatarImage = new Image();
		
		avatarPanel = new FlowPanel();
		avatarPanel.add(avatarImage);
		avatarPanel.setStylePrimaryName("hablar-avatar");
		avatarPanel.addStyleDependentName(size);
		
		initWidget(avatarPanel);
	}

	@Override
	public void setAvatarUrl(String url) {
		avatarImage.setUrl(url);
	}

	@Override
	public HasMouseOverHandlers mouseOver() {
		return avatarImage;
	}

	@Override
	public HasMouseOutHandlers mouseOut() {
		return avatarImage;
	}

	@Override
	public String getSize() {
		return size;
	}
}
