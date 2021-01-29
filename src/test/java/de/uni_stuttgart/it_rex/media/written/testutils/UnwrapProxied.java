package de.uni_stuttgart.it_rex.media.written.testutils;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

public final class UnwrapProxied {
  private UnwrapProxied() {
  }

  /**
   * Checks if the given object is a proxy, and unwraps it if it is.
   *
   * @param bean The object to check
   * @return The unwrapped object that was proxied, else the object
   * @throws Exception
   */
  public static Object unwrap(final Object bean) throws Exception {
    if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
      Advised advised = (Advised) bean;
      return advised.getTargetSource().getTarget();
    }
    return bean;
  }
}
