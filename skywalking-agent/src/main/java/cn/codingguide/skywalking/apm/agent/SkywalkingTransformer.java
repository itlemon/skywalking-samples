package cn.codingguide.skywalking.apm.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author itlemon <lemon_jiang@aliyun.com>
 * Created on 2023-11-16
 */
public class SkywalkingTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classFileBuffer) throws IllegalClassFormatException {
        // 只拦截SkywalkingAgentTest测试类
        if (!"cn/codingguide/skywalking/apm/demo/SkywalkingAgentTest".equals(className)) {
            return null;
        }

        // 获取Javaassist Class池
        ClassPool pool = ClassPool.getDefault();
        try {
            // 获取到Class池中的SkywalkingAgentTest的CtClass对象，它与SkywalkingAgentTest的Class对象一一对应
            CtClass ctClass = pool.getCtClass(className.replace("/", "."));
            // 找到main方法
            CtMethod mainMethod = ctClass.getDeclaredMethod("main(String.class)");
            // 增加本地变量
            mainMethod.addLocalVariable("beginTime", CtClass.longType);
            // 在main方法第一行插入一行代码
            mainMethod.insertBefore("long beginTime = System.currentTimeMillis();");
            // 在main方法最后一行插入代码
            mainMethod.insertAfter("System.out.print(\"总共耗时:\");");
            mainMethod.insertAfter("System.out.println(System.currentTimeMillis() - beginTime);");

            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回null表示未做任何修改
        return null;
    }
}
