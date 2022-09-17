package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $(By.cssSelector("[name='login']")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[name='password']")).setValue(registeredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $(By.cssSelector("[name='login']")).setValue(notRegisteredUser.getLogin());
        $(By.cssSelector("[name='password']")).setValue(notRegisteredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(byText("Ошибка")).shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $(By.cssSelector("[name='login']")).setValue(blockedUser.getLogin());
        $(By.cssSelector("[name='password']")).setValue(blockedUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(byText("Ошибка")).shouldBe(visible);
        $(withText("Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $(By.cssSelector("[name='login']")).setValue(wrongLogin);
        $(By.cssSelector("[name='password']")).setValue(registeredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(byText("Ошибка")).shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $(By.cssSelector("[name='login']")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[name='password']")).setValue(wrongPassword);
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(byText("Ошибка")).shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }
}
