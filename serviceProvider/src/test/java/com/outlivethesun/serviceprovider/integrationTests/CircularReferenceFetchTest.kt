package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import com.outlivethesun.serviceprovider.api.fetch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

class CircularReference1(reference2: CircularReference2)
class CircularReference2(reference1: CircularReference1)
class CircularReference3(reference3: CircularReference3)
internal class CircularReferenceFetchTest {

    @Test
    fun circularReferenceExc() {
        try {
            SP.fetch<CircularReference1>()
            fail()
        } catch (e: CircularReferenceServiceProviderException) {
            assertEquals(CircularReference1::class, e.abstractTypeToAutowire)
        }
    }

    @Test
    fun circularReferenceReferencingItself() {
        try {
            SP.fetch<CircularReference3>()
            fail()
        } catch (e: CircularReferenceServiceProviderException) {
            assertEquals(CircularReference3::class, e.abstractTypeToAutowire)
        }
    }
}