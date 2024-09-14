import PersList.Cons
import PersList.Nil

sealed interface PersList<out T>{ // Abreviation for Persistant Single Linked List
    data object Nil:PersList<Nothing>

    data class Cons<out T>(val head:T,val tail:PersList<T>):PersList<T>{
        override fun toString():String{
            tailrec fun <T> loop(s:String="",xs:PersList<T>):String = when(xs){
                is Nil -> s
                is Cons -> if(xs.tail is Nil) loop("$s${xs.head}",xs.tail)
                            else loop("$s${xs.head},",xs.tail)
            }
            return loop("",this)
        }
    }

    companion object{
        fun <A> of(vararg aa:A):PersList<A> =
            aa.sliceArray(1 until aa.size)
                .let{when{
                    aa.isEmpty() -> Nil
                    else -> Cons(aa[0],of(*it))}
                }
    }
}

/*
fun <T,O> FList<T>.map(f:(T)->O):FList<O>{
    tailrec fun loop(xs:FList<T>,xs2:FList<O>):FList<O> = when(xs){
        is FList.Nil -> xs2
        is FList.Cons if xs2 is FList.Nil -> map(xs.tail,FList.Cons(f(xs.head),FList.Nil))
        is FList.Cons -> loop(xs.tail, xs2 + f(xs.head))
    }
    return loop(this,FList.Nil)
}*/

tailrec fun PersList<Int>.sum(n:Int=0):Int = when(this){
    is Nil -> n
    is Cons -> tail.sum(n+head)
}

//Ex 3.1
fun <T> PersList<T>.tail(): PersList<T> =  when(this){
    is Nil -> this
    is Cons -> this.tail
}

//Ex 3.2
fun <T> PersList<T>.setHead(x:T):PersList<T> = when(this){
    is Nil -> PersList.of(x)
    is Cons -> Cons(x,tail)
}

//Ex 3.3
tailrec fun <T> PersList<T>.drop(n:Int):PersList<T> =
    if(0>=n) this
    else when(this){
        is Nil -> this
        is Cons -> tail.drop(n-1)}

//Ex 3.4
tailrec fun <T> PersList<T>.dropWhile(f:(T)->Boolean):PersList<T> =
    if(this is Cons && f(head)) tail.dropWhile(f)
    else this

fun <T> PersList<T>.append(xs:PersList<T>):PersList<T> = when(this){
    is Nil -> xs
    is Cons -> Cons(head,tail.append(xs))
}

fun <T> PersList<T>.last():PersList<T> = when(this){
    is Nil -> Nil
    is Cons -> if(tail is Nil) this
                else tail.last()
}

//Ex 3.5
fun <T> PersList<T>.initi():PersList<T> = when(this){
    is Nil -> Nil
    is Cons -> if(tail is Nil) Nil
                else Cons(head, tail.initi())
}

fun <A,B> PersList<A>.foldRight(initial:B, f:(A, B)->B):B = when(this){
    is Nil -> initial
    is Cons -> f(head,tail.foldRight(initial,f))
}

//Ex 3.6
/*
    PersList.of(1.0,2.0,3.0,4.0).product() == 24
    PersList.of(1.0,2.0,3.0,4.0).productContinueIf0() == 24

    PersList.of(1.0,2.0,3.0,0.0,4.0).product() == 0
    PersList.of(1.0,2.0,3.0,0.0,4.0).productContinueIf0() == 24
 */
fun PersList<Double>.productContinueIf0():Double = foldRight(1.0){ a, acc->
    if(a==0.0) acc
    else a * acc
}

//Ex 3.7
fun ex37():Unit = PersList
    .of(1,2,3)
    .foldRight(PersList.empty<Int>()){x,y->Cons(x,y)}
    .let{println(it)}

fun <T> PersList.Companion.empty():PersList<T> = Nil

//Ex 3.8
fun <T> PersList<T>.length():Int = foldRight(0){ _, acc-> acc + 1}

fun main(){
    PersList
        .of(1.0,2.0,3.0,0.0,4.0)
        .also(::println)
        .length()
        .also(::println)
        .also{ex37()}
}