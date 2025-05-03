# -*- coding: utf-8 -*-
"""
中文评论关键词统计脚本（无情感分类版）
依赖库：snownlp、jieba
"""
import sys
import logging
import json
import jieba.analyse
from collections import Counter

# 设置日志级别
jieba.setLogLevel(logging.INFO)

def process_comments(comments):
    """处理每一条评论并统计关键词频率"""
    word_counts = Counter()
    
    for text in comments:
        # 提取关键词（使用TF-IDF算法）
        keywords = jieba.analyse.extract_tags(text, topK=10)
        word_counts.update(keywords)
    
    # 计算归一化值（0-100）
    max_count = max(word_counts.values()) if word_counts else 1
    word_cloud_data = [
        {"word": word, "value": int(100 * count / max_count)}
        for word, count in word_counts.most_common(100)
    ]
    
    return word_cloud_data

def main():
    # 从标准输入读取评论数据
    comments = [line.strip() for line in sys.stdin if line.strip()]
    
    # 处理评论
    result = process_comments(comments)
    
    # 输出JSON结果
    print(json.dumps(result, ensure_ascii=False))

if __name__ == "__main__":
    main()
