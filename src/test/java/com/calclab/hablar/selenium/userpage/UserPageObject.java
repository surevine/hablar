package com.calclab.hablar.selenium.userpage;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.selenium.PageObject;
import com.calclab.hablar.selenium.vcard.VCardPageObject;

public class UserPageObject extends PageObject implements VCardPageObject {

    @FindBy(id = "gwt-debug-HeaderWidget-User-1")
    private WebElement header;

    @FindBy(id = "gwt-debug-PresenceWidget-status")
    private WebElement status;

    @FindBy(id = "gwt-debug-PresenceWidget-menu")
    private WebElement menu;

    @FindBy(id = "gwt-debug-SignalsPreferencesWidget-titleSignals-input")
    private WebElement titleSignals;

    @FindBy(id = "gwt-debug-SignalsPreferencesWidget-incomingNotifications-input")
    private WebElement incomingNotifications;

    @FindBy(id = "gwt-debug-SignalsPreferencesWidget-rosterNotifications-input")
    private WebElement rosterNotifications;

    @FindBy(id = "gwt-debug-UserWidget-close")
    private WebElement close;

    @FindBy(id = "gwt-debug-OwnVCardWidget-name")
    private WebElement name;

    @FindBy(id = "gwt-debug-OwnVCardWidget-nickName")
    private WebElement nickName;

    @FindBy(id = "gwt-debug-OwnVCardWidget-familyName")
    private WebElement familyName;

    @FindBy(id = "gwt-debug-OwnVCardWidget-givenName")
    private WebElement givenName;

    @FindBy(id = "gwt-debug-OwnVCardWidget-middleName")
    private WebElement middleName;

    @FindBy(id = "gwt-debug-OwnVCardWidget-organizationName")
    private WebElement organizationName;

    @FindBy(id = "gwt-debug-OwnVCardWidget-email")
    private WebElement email;

    @FindBy(id = "gwt-debug-OwnVCardWidget-homepage")
    private WebElement homepage;

    public UserPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    public WebElement getClose() {
	return close;
    }

    @Override
    public WebElement getEmail() {
	return email;
    }

    @Override
    public WebElement getFamilyName() {
	return familyName;
    }

    @Override
    public WebElement getGivenName() {
	return givenName;
    }

    public WebElement getHeader() {
	return header;
    }

    @Override
    public WebElement getHomepage() {
	return homepage;
    }

    public WebElement getIncomingNotifications() {
	return incomingNotifications;
    }

    public WebElement getMenu() {
	return menu;
    }

    @Override
    public WebElement getMiddleName() {
	return middleName;
    }

    @Override
    public WebElement getName() {
	return name;
    }

    @Override
    public WebElement getNickName() {
	return nickName;
    }

    @Override
    public WebElement getOrganizationName() {
	return organizationName;
    }

    public WebElement getRosterNotifications() {
	return rosterNotifications;
    }

    public WebElement getStatus() {
	return status;
    }

    public WebElement getTitleSignals() {
	return titleSignals;
    }

    public void waitForStatus(final String text) {
	waitFor(header, text);
    }

}
