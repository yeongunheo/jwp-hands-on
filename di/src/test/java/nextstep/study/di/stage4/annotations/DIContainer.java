package nextstep.study.di.stage4.annotations;

import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    private DIContainer(final Set<Object> beans) {
        this.beans = beans;
    }

    // ClassPathScanner 클래스를 활용해보자.
    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Object> beans = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(beans);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        for (Object bean : beans) {
            if (clazz.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }
}
