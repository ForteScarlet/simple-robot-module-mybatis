package com.forte.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
@Mapper
public interface MsgMapper {

    @Insert("INSERT INTO `nmsl`.`good` (`msg`) VALUES (#{msg})")
    void insertGoodMsgByStr(@Param("msg") String msg);


    @Insert("INSERT INTO `nmsl`.`bad` (`msg`, `type`) VALUES (#{msg}, #{type} )")
    void insertBadMsgByStr(@Param("msg") String msg, @Param("type") int type);


    @Select("SELECT msg FROM `nmsl`.`bad` LIMIT 1")
    String selectOne();


}
