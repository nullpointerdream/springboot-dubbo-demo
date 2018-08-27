package com.y2game.dubbo.dao;


import com.github.pagehelper.Page;
import com.y2game.common.util.TkMapper;
import com.y2game.dubbo.pojo.UserDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: chenjiale
 * @Date: 2018/8/16 14:40
 * @Description:
 */
public interface UserMapper extends TkMapper<UserDO> {
    @Select("select * from t_user")
    List<UserDO> getUsers();
}
