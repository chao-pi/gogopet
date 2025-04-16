package com.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@ToString
public class t_user_model {

    @Size(min = 18, max = 18, message = "user_id 长度为 63 个字符")
    private String user_id;

    @Size(max = 63, message = "user_name 不能超过 63 个字符")
    private String user_name;

    @Size(max = 63, message = "password 不能超过 63 个字符")
    private String password;

    private char user_type = ' ';

    @Size(max = 63, message = "user_address 不能超过 63 个字符")
    private String user_address;

    @Size(min = 18, max = 18, message = "picture_id 长度为 18 个字符")
    private String picture_id;

    @Size(min = 18, max = 18, message = "company_id 长度为 18 个字符")
    private String company_id;
}
