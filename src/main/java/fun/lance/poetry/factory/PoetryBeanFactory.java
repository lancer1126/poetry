package fun.lance.poetry.factory;

import fun.lance.poetry.service.IPoetryService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PoetryBeanFactory implements ApplicationContextAware {

    private static final Map<String, IPoetryService> poetryServiceMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IPoetryService> beanMap = applicationContext.getBeansOfType(IPoetryService.class);
        beanMap.forEach((k, v) -> poetryServiceMap.put(v.getName(), v));
    }

    public IPoetryService getService(String beanName) {
        return poetryServiceMap.get(beanName);
    }
}
