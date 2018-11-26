package org.nerve.core.module.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		org.nerve.core.module.cache
 * FILE			CacheConfiguration.java
 * Created by 	zengxm on 2017/12/25.
 *
 */
@Configuration
@ConditionalOnProperty(name ="zeus.cache.enable", matchIfMissing = true, havingValue = "true")
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration extends CachingConfigurerSupport {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	CacheConfig cacheConfig;
	@Autowired
    CacheProperties cacheProperties;
	@Autowired(required = false)
	CacheLoader<Object, Object> cacheLoader;

	@PostConstruct
	protected void init(){
		logger.info("[Cache] 初始化 CacheConfiguration ( set zeus.cache.enable=false if you want to disable zeus cache!)");
		logger.info("[Cache] {}", cacheConfig.caches);
	}

	@Bean
	@Override
	public CacheResolver cacheResolver() {
		logger.debug("[Cache] building CacheResolver... ");
		return new ZeusCacheResolver(cacheManager());
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		ZeusCaffeineCacheManager cacheManager = createCacheManager();
		if (!CollectionUtils.isEmpty(cacheProperties.getCacheNames())) {
			cacheManager.setCacheNames(cacheProperties.getCacheNames());
		}

		cacheConfig.caches.forEach(bean -> {
			boolean hasSpec = StringUtils.hasText(bean.spec);
			if(!hasSpec)
				logger.warn("[Cache] 检测到 name={} 的cache没有定义 spec 属性...", bean.name);

			logger.info("[Cache] adding cache name={} {}", bean.name, bean.spec);
			Cache cache = new CaffeineCache(
					bean.name,
					hasSpec && bean.spec.contains("refreshAfterWrite")?
							Caffeine.from(bean.spec).build(cacheLoader)
							:
							Caffeine.from(hasSpec?bean.spec:"").build()
			);

			logger.info("[Cache] added cache name={} {}", bean.name, cache);
			cacheManager.addCache(cache);
		});

		return cacheManager;
	}

	@Bean
	public CacheLoader<Object, Object> cacheLoader() {
		//默认返回 null 强制失效缓存
		CacheLoader<Object, Object> defaultCacheLoader = key -> null;
		return defaultCacheLoader;
	}

	private ZeusCaffeineCacheManager createCacheManager(){
		logger.debug("[Cache] building CacheManager (CacheLoader={})....", cacheLoader);
		ZeusCaffeineCacheManager cacheManager = new ZeusCaffeineCacheManager();
		setCacheBuilder(cacheManager);
		if (cacheLoader != null) {
			cacheManager.setCacheLoader(this.cacheLoader);
		}
		return cacheManager;
	}

	private void setCacheBuilder(CaffeineCacheManager cacheManager) {
		logger.debug("[Cache] building default CacheSpecification....");
		String specification = cacheProperties.getCaffeine().getSpec();
		if (StringUtils.hasText(specification)) {
			cacheManager.setCacheSpecification(specification);
		}
	}
}
