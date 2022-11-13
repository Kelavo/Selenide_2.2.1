import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class OrderingACardTest {

    @BeforeEach
    @Test
    void shouldSendARequest() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        String planningDate = generateDate(3);

        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Филипп Преображенский");
        form.$("[data-test-id=phone] input").setValue("+79788956483");
        form.$("[data-test-id=agreement]").click();
        form.$("[class=button__text]").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    @Test
    void shouldSendARequestTheCalendarAndCityDDlist() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");

        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").sendKeys("Мо");
        $x("//*[text()='Москва']").click();
        form.$("[class=icon-button__text]").click();

        int dayOfDelivery = LocalDate.now().plusDays(7).getDayOfMonth();
        if (dayOfDelivery > 7) {
            $x(String.format("//*[@class='calendar__row']/td[contains(text(), '%s')]", dayOfDelivery)).doubleClick();
        } else {
            $x("//*[@data-step='1']").click();
            $x(String.format("//*[@class='calendar__row']/td[contains(text(), '%s')]", dayOfDelivery)).doubleClick(); //ищем наше число
        }
        form.$("[data-test-id=name] input").setValue("Филипп Преображенский");
        form.$("[data-test-id=phone] input").setValue("+79788956483");
        form.$("[data-test-id=agreement]").click();
        form.$("[class=button__text]").click();
        String datePattern = "dd.MM.yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        String myDate = dateFormatter.format(LocalDate.now().plusDays(7)).toString();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + myDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}
