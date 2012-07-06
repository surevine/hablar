package com.calclab.hablar.icons.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public interface AvatarConfig {
	
	AvatarConfig getFromMeta();

	String getUrl(XmppURI xmppURI);
}
