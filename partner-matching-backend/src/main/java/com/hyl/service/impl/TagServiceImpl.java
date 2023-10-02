package com.hyl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.model.entity.Tag;
import com.hyl.service.TagService;
import com.hyl.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author Alan
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2023-10-02 13:54:05
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




