 
 open class Dog {                // 1
     open fun sayHello() {       // 2
        println("wow wow!")
    }
}

class Labrador : Dog() {       // 3
    override fun sayHello() {   // 4
        println("wif wif!")
    }
}

fun main() {
    val dog: Dog = Labrador()
    dog.sayHello()
}
