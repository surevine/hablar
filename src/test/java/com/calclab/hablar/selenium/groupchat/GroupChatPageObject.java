package com.calclab.hablar.selenium.groupchat;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.core.client.Idify;
import com.calclab.hablar.groupchat.client.HablarGroupChat;
import com.calclab.hablar.selenium.PageObject;

public class GroupChatPageObject extends PageObject {

    String CONVERT_ACTION = "gwt-debug-" + HablarGroupChat.ACTION_ID_CONVERT;

    @FindBy(id = "gwt-debug-InviteToRoomWidget-invite")
    private WebElement openGroupChatAccept;

    public GroupChatPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    public WebElement getConvertAction(final String jid) {
	final String id = Idify.id(HablarGroupChat.ACTION_ID_CONVERT, Idify.uriId(jid));
	return findElement(new ByIdOrName("gwt-debug-" + id));
    }

    public WebElement getOpenGroupChatAccept() {
	return openGroupChatAccept;
    }

    public WebElement getRoomHeader(final String id) {
	return findElement(new ByIdOrName("gwt-debug-HeaderWidget-Room-" + id));
    }

    public WebElement getRoomScroll(final String id) {
	return findElement(new ByIdOrName("gwt-debug-ChatWidget-scroll-Room-" + id));
    }

    public WebElement getRoomStatus(final String id) {
	return findElement(new ByIdOrName("gwt-debug-ChatWidget-status-Room-" + id));
    }

    public WebElement getRoomTextBox(final String id) {
	return findElement(new ByIdOrName("gwt-debug-ChatWidget-talkBox-Room-" + id));
    }

    public void waitForStatus(final String id, final String text) {
	waitFor(getRoomStatus(id), text);
    }

    public void waitForTextInRoom(final String id, final String text) {
	waitFor(getRoomScroll(id), text);
    }
}
