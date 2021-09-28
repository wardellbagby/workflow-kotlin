import com.squareup.workflow1.Workflow
import com.squareup.workflow1.renderWorkflowIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import react.RBuilder
import react.RProps
import react.useEffect
import react.useMemo
import react.useState

external interface WorkflowComponentProps<Props, Output, Rendering : ReactComponentRendering> :
  RProps {
  var workflow: Workflow<Props, Output, Rendering>
  var workflowProps: Props
  var onOutput: ((Output) -> Unit)?
}

fun <Props, Output, Rendering : ReactComponentRendering> workflow(
  name: String
) = scopedComponent<WorkflowComponentProps<Props, Output, Rendering>>(name) { props, scope ->
  with(scope) {
    val propsFlow = useMemo(callback = { MutableStateFlow(props.workflowProps) }, arrayOf())
    useEffect(listOf(props.workflowProps)) {
      propsFlow.tryEmit(props.workflowProps)
    }

    val (rendering, setRendering) = useState<ReactComponentRendering?>(null)
    useEffect(listOf()) {
      launch {
        renderWorkflowIn(
          props.workflow,
          this,
          propsFlow,
          onOutput = { props.onOutput?.invoke(it) })
          .collect {
            console.log("updated rendering for $name")
            setRendering(it.rendering)
          }
      }
    }
    workflowStub(rendering)
  }
}

fun RBuilder.workflowStub(rendering: ReactComponentRendering?) =
  rendering?.componentFactory?.invoke(RBuilder())?.let {
    child(it)
  }


