import com.ccfraser.muirwik.components.mBackdrop
import com.squareup.sample.container.panel.ScrimContainerScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class) val ScrimContainerScreen =
  componentFactory<ScrimContainerScreen<Any>> { rendering ->
    //todo I don't think this backdrop works how I want it to.
    if (rendering.dimmed) {
      mBackdrop(open = true) {
        WorkflowStub(rendering.wrapped)
      }
    } else {
      WorkflowStub(rendering.wrapped)
    }
  }

