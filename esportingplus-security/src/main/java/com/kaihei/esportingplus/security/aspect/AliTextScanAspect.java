package com.kaihei.esportingplus.security.aspect;

import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.security.domain.service.IContentProcessService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Aspect
@Order(10)
public class AliTextScanAspect {
    @Autowired
    IContentProcessService iContentProcessService;

    @Pointcut("@annotation(com.kaihei.esportingplus.security.annotation.AliTextScan)")
    private void aliTextScanCut(){
    }

    @Before("aliTextScanCut()")
    public void before(JoinPoint pjp) throws Throwable {
        //获取切入方法的参数值
        Object args[] = pjp.getArgs();
        //获取切入的方法对象
        Method method = ((MethodSignature)pjp.getSignature()).getMethod();
        //获取所有参数
        Class<?>[] paramTypes = method.getParameterTypes();
        //结果字符串
        StringBuffer sb =new StringBuffer();
        for(int i=0;i<paramTypes.length;i++){
            System.out.println(paramTypes[i].equals(String.class));
            if(paramTypes[i].equals(String.class)){
                sb.append(args[i]);
            }else{
                System.out.println(paramTypes[i].getName());
                sb.append(getAllFieldsValue(args[i]));
            }
        }
        System.out.println("result: " + sb.toString());
        iContentProcessService.textScan(sb.toString());
    }

    //获取传入对象的成员变量和值
    public String getAllFieldsValue(Object object) {
        StringBuffer sb =new StringBuffer("");
        if (object == null){
            return sb.toString();
        }
        // 1. 获取其从父类继承下来的所有字段 (Object 除外)
        List<Field> fieldList = new ArrayList<>();
        Class clazz = object.getClass();
        while (clazz != null && !clazz.equals(Object.class)) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        // 2. 非空校验
        for (Field field: fieldList) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = null;
            // 忽略序列化 id
            if ("serialVersionUID".equalsIgnoreCase(fieldName)) {
                continue;
            }
            // 忽略静态变量
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if (isStatic) {
                continue;
            }
            try {
                fieldValue = field.get(object);
                System.out.println("filedValue:"+fieldValue);
                if (ObjectTools.isNotEmpty(fieldValue)) {
                    sb.append(String.valueOf(fieldValue));
                }
            } catch (IllegalAccessException e) {
                // 已经设置了 accessible ...
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
