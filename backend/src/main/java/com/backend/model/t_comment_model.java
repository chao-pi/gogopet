package com.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@ToString
public class t_comment_model {

    @Size(min = 18, max = 18, message = "comment_id 长度为 18 个字符")
    private String comment_id;

    @Size(min = 18, max = 18, message = "company_id 长度为 18 个字符")
    private String company_id;

    @Size(min = 18, max = 18, message = "user_id 长度为 18 个字符")
    private String user_id;

    @Size(max = 65535, message = "comment_content 长度不能超过 65535 个字符")
    private String comment_content;
}
