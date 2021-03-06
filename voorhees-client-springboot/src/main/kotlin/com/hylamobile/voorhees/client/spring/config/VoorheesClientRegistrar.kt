package com.hylamobile.voorhees.client.spring.config

import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata
import org.springframework.util.ClassUtils

class VoorheesClientRegistrar : ImportBeanDefinitionRegistrar, EnvironmentAware, BeanClassLoaderAware {

    private lateinit var environment: Environment
    private lateinit var beanClassLoader: ClassLoader

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun setBeanClassLoader(beanClassLoader: ClassLoader) {
        this.beanClassLoader = beanClassLoader
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        ifConfigPresents { config ->
            Registrar(config, registry, beanClassLoader).apply {
                registerJsonRpcClients()
                registerJsonRpcServices()
            }
        }
    }

    private fun ifConfigPresents(block: (VoorheesProperties) -> Unit) {
        val binder = Binder.get(environment).bind("voorhees.client", VoorheesProperties::class.java)
        binder.ifBound(block)
    }

    private class Registrar(
        val clientConfig: VoorheesProperties,
        val registry: BeanDefinitionRegistry,
        val beanClassLoader: ClassLoader) {

        fun registerJsonRpcClients() {
            clientConfig.services.forEach { (service, info) ->
                val serviceName = service.uniform
                val beanDef = BeanDefinitionBuilder
                    .genericBeanDefinition(SpringJsonRpcClient::class.java)
                    .addConstructorArgValue(clientConfig.basePackage)
                    .addConstructorArgValue(info)
                    .beanDefinition
                registry.registerBeanDefinition("${serviceName}JsonRpcClient", beanDef)
            }
        }

        fun registerJsonRpcServices() {
            val scanner = JsonRpcClientScanner()
            scanner.findCandidateComponents(clientConfig.basePackage)
                .forEach { beanDef ->
                    registerJsonRpcService(beanDef.beanClassName)
                }
        }

        private fun registerJsonRpcService(beanClassName: String?) {
            checkNotNull(beanClassName)

            val service = clientConfig.services.asSequence()
                .filter { (service, info) ->
                    service != "default" &&
                        info.targets.any { p -> beanClassName.startsWith(p) }
                }
                .map { (service, _) -> service }
                .firstOrNull() ?: "default"

            val beanClass = ClassUtils.resolveClassName(beanClassName, beanClassLoader)
            val beanName = ClassUtils.getShortNameAsProperty(beanClass)
            val serviceBeanDef = BeanDefinitionBuilder
                .genericBeanDefinition()
                .addConstructorArgValue(beanClass)
                .setFactoryMethodOnBean("getService", "${service.uniform}JsonRpcClient")
                .beanDefinition
            registry.registerBeanDefinition(beanName, serviceBeanDef)
        }
    }
}
