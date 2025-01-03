package com.library.pages;

import com.library.utility.BrowserUtil;
import com.library.utility.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Books extends BasePage{




    public String findBook(String bookName){

        BrowserUtil.sleep(1);
        searchBox.sendKeys(bookName+ Keys.ENTER);
        BrowserUtil.sleep(1);

        WebElement actualBookName = Driver.getDriver().findElement(By.xpath("//td[3]"));
        return actualBookName.getText();
    }

    @FindBy(xpath = "//input[@type='search']")
    public WebElement searchBox;
}
