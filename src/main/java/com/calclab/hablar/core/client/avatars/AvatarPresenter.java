package com.calclab.hablar.core.client.avatars;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.hablar.icons.client.AvatarConfig;

public class AvatarPresenter {
	private final AvatarDisplay display;
	private final AvatarConfig avatarConfig;
	private XmppURI jid;
	
	public AvatarPresenter(final AvatarDisplay display, final AvatarConfig avatarConfig) {
		this.display = display;
		this.avatarConfig = avatarConfig;
		display.asWidget().setVisible(false);
	}
	
	public void setJid(final XmppURI jid) {
		this.jid = jid;
		display.setAvatarUrl(avatarConfig.getUrl(jid, display.getSize()));
		display.asWidget().setVisible(true);
	}
	
	public XmppURI getJid() {
		return jid;
	}

	public AvatarDisplay getDisplay() {
		return display;
	}
}
