import com.outlivethesun.implementationtest.IService
import com.outlivethesun.implementationtest.Service
import com.outlivethesun.serviceprovider.SP
import com.outlivethesun.serviceprovider.getService
import com.outlivethesun.serviceprovider.setService

fun main() {
//    val service = SP.getService<IService>()
    getServiceAutowired()
//    setGetService()
}

fun setGetService() {
    val setService = Service()

    SP.setService<IService>(setService)
    val getService = SP.getService<IService>()
    val getService2 = SP.getService<IService>()
}

private fun getServiceAutowired() {
    val service = SP.getService<IService>()
    val service2 = SP.getService<IService>()
    service.doYourMagic()
}