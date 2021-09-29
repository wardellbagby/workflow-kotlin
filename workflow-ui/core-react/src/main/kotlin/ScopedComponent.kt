import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import react.PropsWithChildren
import react.RBuilder
import react.fc
import react.useEffectOnce
import react.useMemo

internal fun <Props : PropsWithChildren> ScopedComponent(
  name: String,
  render: RBuilder.(Props, scope: CoroutineScope) -> Unit
) = fc(name, fun RBuilder.(props: Props) {
  val scope = useMemo { CoroutineScope(Job()) }

  useEffectOnce {
    scope.coroutineContext.job.start()
    console.log("Starting coroutine scope for $name")

    cleanup {
      console.log("Stopping coroutine scope for $name")
      scope.cancel()
    }
  }
  render(props, scope)
})
