import com.outlivethesun.implementationtest.IService
import com.outlivethesun.serviceprovider.SP
import com.outlivethesun.serviceprovider.getService

fun main() {
    val service = SP.getService<IService>()
    service.doYourMagic()
}