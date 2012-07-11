package com.calclab.hablar.selenium.openchat;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.selenium.PageObject;

public class OpenChatPageObject extends PageObject {

    @FindBy(id = "gwt-debug-OpenChatWidget-jabberId")
    private WebElement jabberId;

    @FindBy(id = "gwt-debug-OpenChatWidget-open")
    private WebElement open;

    @FindBy(id = "gwt-debug-OpenChatWidget-cancel")
    private WebElement cancel;

    @FindBy(id = "gwt-debug-HablarOpenChat-openAction")
    private WebElement action;

    @FindBy(className = "hablar-OpenChatWidget")
    private WebElement widget;

    @FindBy(id = "gwt-debug-OpenChatWidget-addToRoster-input")
    private WebElement addToRoster;

    public OpenChatPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    public WebElement getAction() {
	return action;
    }

    public WebElement getAddToRoster() {
	return addToRoster;
    }

    public WebElement getCancel() {
	return cancel;
    }

    public WebElement getJabberId() {
	return jabberId;
    }

    public WebElement getOpen() {
	return open;
    }

    public WebElement getWidget() {
	return widget;
    }
}
