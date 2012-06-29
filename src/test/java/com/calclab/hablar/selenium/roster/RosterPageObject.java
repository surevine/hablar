package com.calclab.hablar.selenium.roster;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindBy;

import com.calclab.hablar.core.client.Idify;
import com.calclab.hablar.selenium.PageObject;

public class RosterPageObject extends PageObject {

    @FindBy(id = "gwt-debug-RosterWidget-disabledPanel")
    private WebElement disabledLabel;

    @FindBy(id = "gwt-debug-HeaderWidget-Roster-1")
    private WebElement header;

    @FindBy(id = "gwt-debug-HablarGroupChat-openGroupChatAction")
    private WebElement openGroupChat;

    @FindBy(id = "gwt-debug-HablarRoster-removeFromGroupAction")
    private WebElement removeFromGroupAction;

    @FindBy(id = "gwt-debug-HablarRoster-removeFromRosterAction")
    private WebElement removeBuddyAction;

    @FindBy(id = "gwt-debug-HablarRoster-manageGroupsAction")
    private WebElement manageGroupsAction;

    @FindBy(id = "gwt-debug-EditBuddy-editAction")
    private WebElement editBuddyAction;

    @FindBy(id = "gwt-debug-HablarVCard-seeVCardAction")
    private WebElement seeBuddyVCardAction;

    @FindBy(id = "gwt-debug-ManageGroupsWidget-accept")
    private WebElement manageGroupsAccept;

    @FindBy(id = "gwt-debug-HablarRoster-deleteGroupAction")
    private WebElement deleteGroupAction;

    @FindBy(id = "gwt-debug-ManageGroupsWidget-newGroup")
    private WebElement createNewGroupButton;

    @FindBy(id = "gwt-debug-GroupSelectorWidget-editableName")
    private WebElement newGroupField;

    public RosterPageObject(final WebDriver webdriver) {
	super(webdriver);
    }

    public WebElement getCreateNewGroupButton() {
	return createNewGroupButton;
    }

    public WebElement getDeleteGroupAction() {
	return deleteGroupAction;
    }

    public WebElement getDisableLabel() {
	return disabledLabel;
    }

    public WebElement getEditBuddyAction() {
	return editBuddyAction;
    }

    public WebElement getGroup(final String groupId) {
	final String id = Idify.id("GroupHeaderWidget-name", groupId);
	return findElement(new ByIdOrName("gwt-debug-" + id));
    }

    public WebElement getGroupMenu(final String groupId) {
	System.out.println("Roster - getGroupMenu : " + groupId);
	final String id = Idify.id("GroupHeaderWidget-menu", groupId);
	return findElement(new ByIdOrName("gwt-debug-" + id));
    }

    public WebElement getHeader() {
	return header;
    }

    public WebElement getItemMenu(final String groupId, final String jid) {
	final String id = Idify.id("RosterItemWidget", groupId, Idify.uriId(jid), "roster-menu");
	return findElement(new ByIdOrName("gwt-debug-" + id));
    }

    public WebElement getManageGroupsAccept() {
	return manageGroupsAccept;
    }

    public WebElement getManageGroupsAction() {
	return manageGroupsAction;
    }

    public WebElement getNewGroupField() {
	return newGroupField;
    }

    public WebElement getOpenGroupChat() {
	return openGroupChat;
    }

    public WebElement getRemoveBuddyAction() {
	return removeBuddyAction;
    }

    public WebElement getRemoveFromGroupAction() {
	return removeFromGroupAction;
    }

    public WebElement getRosterItem(final String groupId, final String jid) {
	final String id = Idify.id("RosterItemWidget", groupId, Idify.uriId(jid));
	return findElement(new ByIdOrName("gwt-debug-" + id));
    }

    public WebElement getSeeBuddyVCardAction() {
	return seeBuddyVCardAction;
    }

    public void waitForItemMenu(final String groupId, final String jid) {
	waitForId("gwt-debug-" + Idify.id("RosterItemWidget", groupId, Idify.uriId(jid), "roster-menu"));
    }
}
