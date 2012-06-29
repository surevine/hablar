package com.calclab.hablar.selenium.login;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.login.client.LoginMessages;
import com.calclab.hablar.selenium.PageObject;
import com.calclab.hablar.selenium.tools.I18nHelper;
import com.calclab.hablar.selenium.tools.SeleniumConstants;

public class LoginPageObject extends PageObject {
    @FindBy(id = "gwt-debug-HeaderWidget-Login-1")
    private WebElement header;
    @FindBy(id = "gwt-debug-LoginWidget-user")
    private WebElement user;
    @FindBy(id = "gwt-debug-LoginWidget-password")
    private WebElement passwd;
    @FindBy(id = "gwt-debug-LoginWidget-button")
    private WebElement button;
    private final I18nHelper i18n;

    public LoginPageObject(final WebDriver webdriver) {
	super(webdriver);
	i18n = new I18nHelper(LoginMessages.class);
    }

    public void assertIsConnectedAs(final String user) {
	waitFor(header, i18n.get("connectedAs", user));
    }

    public void assertIsDisconnected() {
	waitFor(header, i18n.get("disconnected"));
    }

    public WebElement getHeader() {
	return header;
    }

    public WebElement Header() {
	return getHeader();
    }

    public void logout() {
	header.click();
	waitFor(button, i18n.get("logout"));
	button.click();
	assertIsDisconnected();
    }

    public void signIn(final String username, final String password) {
	header.click();
	user.clear();
	passwd.clear();
	assertIsDisconnected();
	user.sendKeys(username);
	passwd.sendKeys(password);
	button.click();
    }

    public void signInDefUser() {
	signIn(SeleniumConstants.USERJID, SeleniumConstants.PASSWD);
    }

}
