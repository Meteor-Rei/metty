package org.meteor.metty.server.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: RpcClassPathBeanDefinitionScanner
 * @Created Time: 2024-04-09 17:07
 **/
public class RpcClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {


    private Class<? extends Annotation> annotationType;
    public RpcClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
        registerFilters();
    }

    public RpcClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotationType) {
        super(registry);
        this.annotationType = annotationType;
        // 放行指定的注解类型
        registerFilters();
    }

    private void registerFilters() {
        // 放行指定 annotation 类型
        if (annotationType != null) {
            this.addIncludeFilter(new AnnotationTypeFilter(this.annotationType));
        } else { // 放行所有类型
            this.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        }
    }
    @Override
    public int scan(String... basePackages) {
        return super.scan(basePackages);
    }
}
