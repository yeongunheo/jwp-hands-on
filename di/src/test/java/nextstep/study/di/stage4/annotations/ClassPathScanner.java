package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassPathScanner {

    private static final Logger log = LoggerFactory.getLogger(ClassPathScanner.class);

    public static Set<Object> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = new HashSet<>();
        Set<Class<?>> list1 = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> clazz : list1) {
            log.info(clazz.getName());
            classes.add(clazz);
        }
        Set<Class<?>> list2 = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> clazz : list2) {
            log.info(clazz.getName());
            classes.add(clazz);
        }

        Set<Object> beans = createBeans(classes);
        for (Object bean : beans) {
            setFields(bean, beans);
        }
        return beans;
    }

    // 기본 생성자로 빈을 생성한다.
    private static Set<Object> createBeans(final Set<Class<?>> classes) {
        Set<Object> result = new HashSet<>();
        for (Class<?> clazz : classes) {
            Object instance = null;
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                instance = constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            result.add(instance);
        }
        return result;
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    // 각 필드에 빈을 대입(assign)한다.
    private static void setFields(final Object bean, final Set<Object> beans) {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Inject.class) != null) {
                Class<?> fieldType = field.getType();
                for (Object b : beans) {
                    if (fieldType.isInstance(b)) {
                        field.setAccessible(true);
                        try {
                            field.set(bean, b);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
