import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backstack.BackStackScreen

@OptIn(WorkflowUiExperimentalApi::class) val BackStackScreen =
  componentFactory<BackStackScreen<Any>> { rendering ->
      WorkflowStub(rendering.top)
  }

