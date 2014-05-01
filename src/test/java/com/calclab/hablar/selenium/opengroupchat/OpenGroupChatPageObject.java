package com.calclab.hablar.selenium.opengroupchat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.selenium.PageObject;

public class OpenGroupChatPageObject extends PageObject {

    private final static String USER_LIST_WIDGET_ID = "gwt-debug-InviteToRoomWidget-list";

    @FindBy(id = "gwt-debug-HablarRooms-openRoom")
    private WebElement action;

    @FindBy(id = "gwt-debug-InviteToRoomWidget-roomName")
    private WebElement roomName;

    @FindBy(id = "gwt-debug-InviteToRoomWidget-message")
    private WebElement message;

    @FindBy(id = "gwt-debug-InviteToRoomWidget-invite")
    private WebElement invite;

    @FindBy(id = "gwt-debug-InviteToRoomWidget-cancel")
    private WebElement cancel;

    @FindBy(className = "hablar-OpenRoomWidget")
    private WebElement widget;

    public OpenGroupChatPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    public WebElement getAction() {
	return action;
    }

    public WebElement getCancel() {
	return cancel;
    }

    public WebElement getInvite() {
	return invite;
    }

    public WebElement getMessage() {
	return message;
    }

    public WebElement getRoomName() {
	return roomName;
    }

    public WebElement getUserSelect(final String jid) {
	// TODO This is a bit brittle - there must be a better way!
	return findElement(By.xpath("//*[@id='" + USER_LIST_WIDGET_ID
		+ "']//input[@type='checkbox' and (../../div[@class='jid']) = '" + jid + "']"));
    }

    public WebElement getWidget() {
	return widget;
    }
}
