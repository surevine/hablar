package com.calclab.hablar.core.client.avatars;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.hablar.icons.client.AvatarConfig;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.PopupPanel;

public class ZoomableAvatarPresenter extends AvatarPresenter {

	private PopupPanel panel;
	private AvatarDisplay zoomedAvatar;
	private AvatarConfig avatarConfig;
	
	public ZoomableAvatarPresenter(final AvatarDisplay display, final AvatarConfig avatarConfig) {
		super(display, avatarConfig);
		
		this.avatarConfig = avatarConfig;

		// This is the wrong place for this, but I'm not sure where it should be
		panel = new PopupPanel();
		zoomedAvatar = new AvatarWidget("medium");
		panel.getElement().getStyle().setZIndex(1000);
		panel.add(zoomedAvatar);
		panel.setStylePrimaryName("hablar-ZoomedAvatarPopup");	// By overriding the normal primary name, we remove any gwt styling from the panel
		
		display.mouseOver().addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				panel.setPopupPosition(display.asWidget().getAbsoluteLeft(), display.asWidget().getAbsoluteTop() + display.asWidget().getOffsetHeight());
				panel.show();
			}
			
		});

		display.mouseOut().addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if(panel != null) {
					panel.hide();
				}
			}
		});
	}
	
	@Override
	public void setJid(final XmppURI jid) {
		super.setJid(jid);
		
		zoomedAvatar.setAvatarUrl(avatarConfig.getUrl(getJid(), zoomedAvatar.getSize()));
	}
}
