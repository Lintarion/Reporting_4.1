package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;


import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class DeliveryTest {


    @BeforeAll static void setUpAll(){
        SelenideLogger.addListener("allure",new AllureSelenide());
    }
    @AfterAll static void tearDownAll(){
        SelenideLogger.removeListener("allure");

    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $("[class=button__text]").click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible).shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("[class=button__text]").click();
        $("[data-test-id=replan-notification] .notification__content").shouldBe(Condition.visible).shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $x("//*[contains(text(),Перепланировать)]").click();
        $("[data-test-id=success-notification]").shouldHave(Condition.visible, Condition.text("Успешно! Встреча успешно запланирована на " + secondMeetingDate));


    }


}
