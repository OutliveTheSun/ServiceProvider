package com.outlivethesun.serviceprovider

import com.outlivethesun.serviceprovider.api.*
import com.outlivethesun.serviceprovider.api.SPFetchTest
import com.outlivethesun.serviceprovider.api.SPFindTest
import com.outlivethesun.serviceprovider.api.SPPutTest
import com.outlivethesun.serviceprovider.api.SPRemoveTest
import com.outlivethesun.serviceprovider.internal.typeFetchingTracker.TypeFetchingTracker
import com.outlivethesun.serviceprovider.internal.typeFetchingTracker.TypeFetchingTrackerTest
import org.junit.platform.suite.api.*


/**
 * Suites are used to run the test classes/packages in a self-specified order.
 * Since the ServiceProvider is static, it is important that some tests for it run first, so e.g. the boot loader service "ReflectionInfo" is loaded as a mock.
 */

/**
 * [TypeFetchingTracker] is mocked later on by the SP, so it has to be tested first
 */
@Suite
@SelectClasses(TypeFetchingTrackerTest::class)
class TrackerClassSuite

@Suite
@SelectClasses(SPFetchTest::class, SPFindTest::class, SPPutTest::class, SPRegisterTest::class, SPRemoveTest::class)
class SPSuite

@Suite
@SelectPackages("com.outlivethesun.serviceprovider.api", "com.outlivethesun.serviceprovider.internal")
/**
 * Exclude the [TypeFetchingTracker] and Service Provider Tests because it is executed first before anything else
 */
@ExcludeTags("TypeFetchingTracker", "SP")
class PackagesSuite
