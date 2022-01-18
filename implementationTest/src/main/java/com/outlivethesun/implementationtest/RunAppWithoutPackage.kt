import com.outlivethesun.implementationtest.IService
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.ServiceProviderException
import com.outlivethesun.serviceprovider.api.fetch

fun main() {
    try {
        SP.fetch<IService>()
        throw UnknownError()
    }catch (e: ServiceProviderException){
        println("Works")
    }

}

