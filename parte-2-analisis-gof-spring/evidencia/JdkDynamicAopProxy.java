package org.springframework.aop.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Fragmento de evidencia de org.springframework.aop.framework.JdkDynamicAopProxy
 * Modulo: spring-aop
 * Patron: Proxy (Estructural)
 * SOLID asociado: OCP (Abierto/Cerrado)
 */
final class JdkDynamicAopProxy implements InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = this.advised.getTargetSource().getTarget();
        // Intercepta e inyecta soporte transversal sin modificar el Target original
        return method.invoke(target, args);
    }
}
