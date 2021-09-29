import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.modal.AlertContainerScreen
import react.dom.div

@OptIn(WorkflowUiExperimentalApi::class) val AlertContainerScreen =
  componentFactory<AlertContainerScreen<Any>> { rendering ->
    div {
      WorkflowStub(rendering.beneathModals)
      rendering.modals.firstOrNull()?.also {
          WorkflowStub(it)
      }
    }
  }
