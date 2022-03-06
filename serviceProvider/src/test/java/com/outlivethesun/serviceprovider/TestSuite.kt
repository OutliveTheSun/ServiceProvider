package com.outlivethesun.serviceprovider

import com.outlivethesun.serviceprovider.api.SPTest
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionTest
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite

/**
 * The Suite is used to run the test classes in a self-specified order.
 * Since the ServiceProvider is static, it is important that the tests for it run first, so the boot loader service "ReflectionInfo" is loaded as a mock.
 */
@Suite
@SelectClasses(SPTest::class, ServiceDefinitionTest::class)
//@SelectPackages("somePackage")
class ServiceProviderTestSuite