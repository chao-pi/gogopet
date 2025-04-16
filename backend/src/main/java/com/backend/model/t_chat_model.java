package com.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@ToString
public class t_chat_model {

    @Size(min = 18, max = 18, message = "chat_id 长度为 18 个字符")
    private String chat_id;

    @Size(min = 18, max = 18, message = "user_id 长度为 18 个字符")
    private String user_id;

    @Size(max = 65535, message = "chat_content 长度不能超过 65535 个字符")
    private String chat_content = "";
}
