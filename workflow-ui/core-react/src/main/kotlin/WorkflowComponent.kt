import com.squareup.workflow1.Workflow
import com.squareup.workflow1.renderWorkflowIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import react.RBuilder
import react.RProps
import react.child
import react.useEffect
import react.useMemo
import react.useState

private external interface WorkflowComponentProps<Props, Output, Rendering : ReactComponentRendering> :
  RProps {
  var workflow: Workflow<Props, Output, Rendering>
  var workflowProps: Props
  var onOutput: ((Output) -> Unit)?
}

fun <Props, Output, Rendering : ReactComponentRendering> RBuilder.Workflow(
  workflow: Workflow<Props, Output, Rendering>,
  props: Props,
  onOutput: ((Output) -> Unit)? = null
) {
  child(workflowComponent<Props, Output, Rendering>(workflow::class.simpleName!!)) {
    this.attrs.workflow = workflow
    this.attrs.workflowProps = props
    this.attrs.onOutput = onOutput
  }
}

private fun <Props, Output, Rendering : ReactComponentRendering> workflowComponent(name: String) =
  scopedComponent<WorkflowComponentProps<Props, Output, Rendering>>(name) { props, scope ->
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
      WorkflowStub(rendering)
  }
}

fun RBuilder.WorkflowStub(rendering: ReactComponentRendering?) =
  rendering?.componentFactory?.invoke(RBuilder())?.let {
    child(it)
  }


