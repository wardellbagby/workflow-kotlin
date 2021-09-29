import com.squareup.workflow1.Workflow
import com.squareup.workflow1.renderWorkflowIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import react.PropsWithChildren
import react.RBuilder
import react.buildElement
import react.createContext
import react.fc
import react.useContext
import react.useEffect
import react.useEffectOnce
import react.useMemo
import react.useState

private external interface WorkflowComponentProps<Props, Output, Rendering> : PropsWithChildren {
  var workflow: Workflow<Props, Output, Rendering>
  var workflowProps: Props
  var onOutput: ((Output) -> Unit)?
  var componentRegistry: ComponentRegistry
}

val ComponentRegistryContext = createContext<ComponentRegistry>()

fun <Props, Output, Rendering> RBuilder.Workflow(
  workflow: Workflow<Props, Output, Rendering>,
  props: Props,
  onOutput: ((Output) -> Unit)? = null,
  componentRegistry: ComponentRegistry = ComponentRegistry()
) {
  child(workflowComponent<Props, Output, Rendering>(workflow::class.simpleName!!)) {
    this.attrs.workflow = workflow
    this.attrs.workflowProps = props
    this.attrs.onOutput = onOutput
    this.attrs.componentRegistry = componentRegistry
  }
}

private fun <Props, Output, Rendering> workflowComponent(name: String) =
  ScopedComponent<WorkflowComponentProps<Props, Output, Rendering>>(name) { props, scope ->
    with(scope) {
      val propsFlow = useMemo { MutableStateFlow(props.workflowProps) }
      useEffect(listOf(props.workflowProps)) {
        propsFlow.tryEmit(props.workflowProps)
      }

      val (rendering, setRendering) = useState<Any?>(null)
      useEffectOnce {
        launch {
          renderWorkflowIn(
            props.workflow,
            this,
            propsFlow,
            onOutput = { props.onOutput?.invoke(it) })
            .collect {
              console.log("rendering ${it.rendering}")
              setRendering(it.rendering)
            }
        }
      }

      ComponentRegistryContext.Provider(props.componentRegistry) {
        WorkflowStub(rendering)
      }
    }
  }

external interface WorkflowStubComponentProps : PropsWithChildren {
  var rendering: Any?
}

fun RBuilder.WorkflowStub(
  rendering: Any?
) {
  child(workflowStubComponent("WorkflowStub")) {
    this.attrs.rendering = rendering
  }
}

private fun workflowStubComponent(name: String) =
  fc(name, fun RBuilder.(props: WorkflowStubComponentProps) {
    val registry = useContext(ComponentRegistryContext)
    when (val rendering = props.rendering) {
      null -> return
      is ReactComponentRendering -> child(buildElement(rendering.componentFactory))
      else -> child(registry.getComponent(rendering))
    }
  })


