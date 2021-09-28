import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import react.RBuilder
import react.RProps
import react.functionalComponent
import react.useEffectWithCleanup
import react.useMemo

internal fun <Props : RProps> scopedComponent(
  name: String,
  render: RBuilder.(Props, scope: CoroutineScope) -> Unit
) = functionalComponent<Props>(name) { props ->
  val scope = useMemo(callback = { CoroutineScope(Job()) }, arrayOf())

  useEffectWithCleanup(listOf()) {
    scope.coroutineContext.job.start()
    console.log("Starting coroutine scope for $name")
    return@useEffectWithCleanup {
      console.log("Stopping coroutine scope for $name")
      scope.cancel()
    }
  }
  render(props, scope)
}
