package cn.codingguide.skywalking.apm.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author itlemon <lemon_jiang@aliyun.com>
 * Created on 2023-10-27
 */
public class SkyWalkingAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("Hello, This is a SkyWalking Handbook JavaAgent demo.");
        instrumentation.addTransformer(new SkywalkingTransformer());
    }

}
