package com.example.springboottest.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "아이디는 필수 항목입니다.")
    public String  username;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    public String password1;

    @NotEmpty(message = "비밀번호확인은 필수 항목입니다.")
    public String password2;

    @Size(min = 2, max = 10)
    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    public String usernickname;
}