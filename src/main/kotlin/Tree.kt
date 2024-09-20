import Tree.Leaf
import Tree.Branch

sealed interface Tree<out A>{
    data class Leaf<A>(val res:A):Tree<A>
    data class Branch<A>(val left:Tree<A>,val right:Tree<A>):Tree<A>
}

fun <A> Tree<A>.size():Int = when(this){
    is Leaf -> 1
    is Branch -> left.size() + right.size()
}

fun Tree<Int>.maximum():Int = when(this){
    is Leaf -> res
    is Branch -> maxOf(left.maximum(),right.maximum())
}

fun <A,B> Tree<A>.map(f:(A)->B):Tree<B> = when(this){
    is Leaf -> Leaf(f(res))
    is Branch -> Branch(left.map(f),right.map(f))
}

fun <A,B> Tree<A>.fold(f1:(A)->B,f2:(B,B)->B):B = when(this){
    is Leaf -> f1(res)
    is Branch -> f2( left.fold(f1,f2), right.fold(f1,f2))
}

fun main() = tests().let{}
fun tests(){
    val injections = listOf(
        Leaf(1),
        Branch(Branch(Leaf(1), Branch(Leaf(2), Leaf(3))), Leaf(4))
    )

    injections[0].size().also{assert(it==1)}
    injections[1].size().also{assert(it==4)}

    injections[0].maximum().also{assert(it==1)}
    injections[1].maximum().also{assert(it==4)}

    injections[1].map{it + 2}.also(::println)

    injections[1].fold({it}){x,y->x+y}.also(::println)//reduce with plus operator
    injections[1].fold({it + 2}){x,y->x+y}.also(::println)//using fold as a map and reduce in the same time
    injections[1].fold({if(it%2==0) it else 0}){x,y->x+y}.also(::println)//filter in the essence of setting 0 in selected values
}
