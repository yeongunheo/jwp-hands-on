package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Junit4Test instance = clazz.getConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            MyTest annotation = method.getAnnotation(MyTest.class);
            if (annotation != null) {
                method.invoke(instance, null);
            }
        }
    }
}
