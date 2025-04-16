package com.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@ToString
public class t_pet_model {

    @Size(min = 18, max = 18, message = "pet_id 长度为 18 个字符")
    private String pet_id;

    @Size(min = 18, max = 18, message = "user_id 长度为 18 个字符")
    private String user_id;

    @Size(max = 63, message = "pet_name 长度不能超过 63 个字符")
    private String pet_name;

    @Size(max = 63, message = "pet_breed 长度不能超过 63 个字符")
    private String pet_breed;

    @Size(max = 18, message = "pet_weight 长度不能超过 18 个字符")
    private String pet_weight;

    private char pet_health_status;
}
