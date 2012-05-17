package com.calclab.hablar.vcard.client;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ScrollPanel;

public class OtherVCardWidget extends Composite implements VCardDisplay {

	private VCardWidget vCardWidget;
	
	public OtherVCardWidget(final boolean readOnly) {
		final ScrollPanel scroll = new ScrollPanel();
		vCardWidget = new VCardWidget(readOnly, "OtherVCardWidget");
		scroll.add(vCardWidget);
		initWidget(scroll);
	}

	@Override
	public HasClickHandlers getCancel() {
		return vCardWidget.getCancel();
	}

	@Override
	public HasText getField(Field field) {
		return vCardWidget.getField(field);
	}

	@Override
	public HasText getLoading() {
		return vCardWidget.getLoading();
	}

	@Override
	public void setAcceptVisible(boolean visible) {
		vCardWidget.setAcceptVisible(visible);	
	}

	@Override
	public void setCancelText(String text) {
		vCardWidget.setCancelText(text);		
	}

	@Override
	public void setCancelVisible(boolean visible) {
		vCardWidget.setCancelVisible(visible);
	}

	@Override
	public void setFormVisible(boolean visible) {
		vCardWidget.setFormVisible(visible);
	}

	@Override
	public void setLoadingVisible(boolean visible) {
		vCardWidget.setLoadingVisible(visible);
	}

	@Override
	public void setPageTitle(String title) {
		vCardWidget.setPageTitle(title);
	}

}
