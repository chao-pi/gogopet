package com.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Size;

@Setter
@Getter
@ToString
public class t_company_model {

    @Size(min = 18, max = 18, message = "company_id 长度为 18 个字符")
    private String company_id;

    @Size(max = 65535, message = "company_intro 长度不能超过 65535 个字符")
    private String company_intro;

    @Size(max = 63, message = "company_local 长度不能超过 63 个字符")
    private String company_local;
}
