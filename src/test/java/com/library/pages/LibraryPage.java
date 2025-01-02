package com.library.pages;

import com.library.utility.ApiUtil;
import com.library.utility.BrowserUtil;
import com.library.utility.Driver;
import io.cucumber.java.zh_cn.假如;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class LibraryPage {

    public LibraryPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//input[@id='inputEmail']")
    public WebElement emailInput;



    @FindBy(xpath = "//input[@id='inputPassword']")
    public WebElement passwordInput;


    @FindBy(xpath = "//button[@class='btn btn-lg btn-primary btn-block']")
    public WebElement signIn;


    public void login(String role) {

        Map<String, String> roleCredentials = ApiUtil.getCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        login(email,password);
    }

    public void login(String email,String password){
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        BrowserUtil.waitFor(5);
        signIn.click();

    }

}