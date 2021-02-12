package de.uni_stuttgart.it_rex.media.config.written;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mockingDetails;

@Configuration
public class MockitoSkipAutowireConfiguration {

  @Bean
  MockBeanFactory mockBeanFactory() {
    return new MockBeanFactory();
  }

  private static class MockBeanFactory extends InstantiationAwareBeanPostProcessorAdapter {
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
      return !mockingDetails(bean).isMock();
    }
  }

} 