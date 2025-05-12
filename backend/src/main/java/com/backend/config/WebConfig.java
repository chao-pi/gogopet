package com.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // 获取项目根目录
            Resource resource = resourceLoader.getResource("classpath:");
            File projectRoot = resource.getFile().getParentFile().getParentFile();
            String absoluteUploadPath = projectRoot.getAbsolutePath() + File.separator + uploadPath;

            // 配置上传文件的访问路径
            // 映射所有uploads下的子目录
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:" + absoluteUploadPath + File.separator)
                    .setCachePeriod(3600) // 缓存1小时
                    .resourceChain(true); // 启用资源链

            // 单独映射各个子目录，确保每个子目录都能被正确访问
            registry.addResourceHandler("/uploads/avatars/**")
                    .addResourceLocations("file:" + absoluteUploadPath + File.separator + "avatars" + File.separator)
                    .setCachePeriod(3600)
                    .resourceChain(true);

            registry.addResourceHandler("/uploads/pets/**")
                    .addResourceLocations("file:" + absoluteUploadPath + File.separator + "pets" + File.separator)
                    .setCachePeriod(3600)
                    .resourceChain(true);

        } catch (IOException e) {
            throw new RuntimeException("Failed to configure resource handlers", e);
        }
    }
} 