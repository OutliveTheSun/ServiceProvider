import com.outlivethesun.implementationtest.IService
import com.outlivethesun.implementationtest.Service
import com.outlivethesun.serviceprovider.SP
import com.outlivethesun.serviceprovider.fetch
import com.outlivethesun.serviceprovider.put
import com.outlivethesun.serviceprovider.remove

fun main() {
//    val service = SP.getService<IService>()
    getServiceAutowired()
//    setGetService()

}

fun setGetService() {
    val setService = Service()

    SP.put<IService>(setService)
    val getService = SP.fetch<IService>()
    val getService2 = SP.fetch<IService>()
}

private fun getServiceAutowired() {
    val service = SP.fetch<IService>()
    val service2 = SP.fetch<IService>()
    service.doYourMagic()
}