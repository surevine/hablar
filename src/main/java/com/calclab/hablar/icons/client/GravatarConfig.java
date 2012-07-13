/**
 * 
 */
package com.calclab.hablar.icons.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class GravatarConfig implements AvatarConfig {

	@Override
	public String getUrl(final XmppURI xmppURI) {
		return "http://gravatar.com/avatar/" + md5(xmppURI.getJID().toString()) +"?s=24&d=mm";
	}

	private static String md5(String message) {
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			final byte[] array = md.digest(message.getBytes("CP1252"));
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
}
