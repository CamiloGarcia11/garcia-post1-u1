package org.springframework.beans.factory;

/**
 * Fragmento de evidencia de org.springframework.beans.factory.BeanFactory
 * Modulo: spring-beans
 * Patron: Factory Method (Creacional)
 * SOLID asociado: DIP (Inversion de Dependencias)
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    boolean containsBean(String name);
}
