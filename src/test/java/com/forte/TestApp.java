package com.forte;

import com.forte.mapper.MsgMapper;
import com.forte.mapper.MsgMapper2;
import com.forte.qqrobot.SimpleRobotApplication;
import com.forte.qqrobot.depend.DependCenter;
import com.simbot.modules.debugger.common.DebugApplication;
import com.simbot.modules.debugger.common.DebugContext;

/**
 *
 * @author ForteScarlet <ForteScarlet@163.com> 
 */
@SimpleRobotApplication
public class TestApp {
    public static void main(String[] args) {
        DebugContext context = new DebugApplication().run(TestApp.class, args);

        DependCenter center = context.getDependCenter();

        MsgMapper msgMapper = center.get(MsgMapper.class);

        System.out.println(msgMapper);
        System.out.println(msgMapper.selectOne());

        MsgMapper2 msgMapper2 = center.get(MsgMapper2.class);

        System.out.println(msgMapper2);
        System.out.println(msgMapper2.selectOne());

    }
}
