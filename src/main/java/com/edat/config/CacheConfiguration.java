package com.edat.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.edat.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.edat.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.edat.domain.Authority.class.getName());
            createCache(cm, com.edat.domain.Alumno.class.getName());
            createCache(cm, com.edat.domain.ResponsableAlumno.class.getName());
            createCache(cm, com.edat.domain.Autorizado.class.getName());
            createCache(cm, com.edat.domain.Alumno.class.getName() + ".responsableAlumnos");
            createCache(cm, com.edat.domain.ResponsableAlumno.class.getName() + ".alumnos");
            createCache(cm, com.edat.domain.Alumno.class.getName() + ".autorizados");
            createCache(cm, com.edat.domain.Autorizado.class.getName() + ".alumnos");
            createCache(cm, com.edat.domain.ResponsableAlumno.class.getName() + ".autorizados");
            createCache(cm, com.edat.domain.Autorizado.class.getName() + ".responsableAlumnos");
            createCache(cm, com.edat.domain.Alumno.class.getName() + ".historials");
            createCache(cm, com.edat.domain.Autorizado.class.getName() + ".historials");
            createCache(cm, com.edat.domain.Historial.class.getName());
            createCache(cm, com.edat.domain.Alumno.class.getName() + ".baneados");
            createCache(cm, com.edat.domain.Baneados.class.getName());
            createCache(cm, com.edat.domain.Baneados.class.getName() + ".alumnos");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
