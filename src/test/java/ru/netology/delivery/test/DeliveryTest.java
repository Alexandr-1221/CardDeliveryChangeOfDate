package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        val firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        val daysToAddForSecondMeeting = 7;
        val secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder=\"Город\"]").setValue(validUser.getCity());
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[placeholder=\"Дата встречи\"]").setValue(firstMeetingDate);
        $("[name=\"name\"]").setValue(validUser.getName());
        $("[name=\"phone\"]").setValue(validUser.getPhone());
        $("[class=\"checkbox__box\"]").click();
        $(".button__text").click();
        if ($(withText("Доставка в выбранный город недоступна")).isDisplayed()){
            int i = validUser.getCity().length() - 2;
            while (i > 0){
                $("[placeholder=\"Город\"]").sendKeys(Keys.BACK_SPACE);
                i--;
            }
            $("[class=\"button button_view_extra button_size_m button_theme_alfa-on-white\"]").click();
        }
        $("[data-test-id=\"success-notification\"] [class=\"notification__content\"]").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[placeholder=\"Дата встречи\"]").setValue(secondMeetingDate);
        $(".button__text").click();
        $(withText("Необходимо подтверждение" )).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] .button").click();
        $("[data-test-id=\"success-notification\"] [class=\"notification__content\"]").shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
