import Option.Some
import Option.None

sealed interface Option<out A>{
    data class Some<out A> (val res:A):Option<A>
    data object None:Option<Nothing>
}

fun <A,B> Option<A>.map(f:(A)->B):Option<B> = when(this){
    is None -> None
    is Some -> Some(f(res))
}

fun <A,B> Option<A>.flatMap(f:(A)->Option<B>):Option<B> = map(f).getOrElse { None }
/*when(this){
is None -> None
is Some -> f(res)
}*/

fun <A> Option<A>.getOrElse(default:()->A):A = when(this){
    is None -> default()
    is Some -> res
}

fun <A> Option<A>.orElse(ob:()->Option<A>):Option<A> = when(this){
    is None -> ob()
    is Some -> this
}

fun <A> Option<A>.filter(f:(A)->Boolean):Option<A> = when(this){
    is None -> None
    is Some -> if(f(res)) this else None
}

fun two(num:Int) = Some(2 + num)

fun main(){
    Some(2)
        .flatMap{two(it)}
        .also(::println)
}