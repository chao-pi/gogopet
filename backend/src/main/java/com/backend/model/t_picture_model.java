package com.backend.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;
import lombok.ToString;

@Setter
@Getter
@ToString
public class t_picture_model {

    @Size(min = 18, max = 18, message = "picture_id 长度为 18 个字符")
    private String picture_id;

    private char picture_usage;
}
