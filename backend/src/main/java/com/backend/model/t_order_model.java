package com.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@ToString
public class t_order_model {

    @Size(min = 18, max = 18, message = "order_id 长度为 18 个字符")
    private String order_id;

    @Size(min = 18, max = 18, message = "pet_id 长度为 18 个字符")
    private String pet_id;

    @Size(min = 18, max = 18, message = "picture_id 长度为 18 个字符")
    private String picture_id;

    private char order_status;
    private char pet_status;
    private char delivery_status;
}
