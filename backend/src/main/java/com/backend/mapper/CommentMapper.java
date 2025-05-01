package com.backend.mapper;

import com.backend.model.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论Mapper接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 根据公司ID查询评论
     * @param companyId 公司ID
     * @return 评论列表
     */
    @Select("SELECT * FROM t_comment WHERE company_id = #{companyId}")
    List<Comment> selectByCompanyId(String companyId);
} 