import com.outlivethesun.implementationtest.IService
import com.outlivethesun.implementationtest.IService2
import com.outlivethesun.implementationtest.Service
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.put

class Test()

fun main() {

    SP.fetch<IService2>()

}