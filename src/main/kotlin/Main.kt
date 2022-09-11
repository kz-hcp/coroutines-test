import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    repeat(10) { index ->
        println("Test #${index + 1}")
        test()
        println()
    }
}

private suspend fun test() {
    errorFlow()
        .onEach { value ->
            println("onEach: $value")
        }
        .onCompletion {
            println("onCompletion")
        }
        .catch { throwable ->
            println("catch: ${throwable.message}")
        }
        .collect()
}

private suspend fun errorFlow(): Flow<String> = flow {
    emit("errorFlow - start - this is sometimes printed, sometimes not")
    error("error")
    // if we remove .flowOn(Dispatchers.IO) then everything is printed correctly
}.flowOn(Dispatchers.IO)
