package ru.clevertec.news_service.builder.dto;

public class LoginDto {

    private String login;
    private String password;

    public LoginDto(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
