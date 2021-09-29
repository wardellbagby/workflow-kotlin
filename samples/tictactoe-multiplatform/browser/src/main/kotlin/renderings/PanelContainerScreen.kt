import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.styles.Breakpoint.md
import com.squareup.sample.container.panel.PanelContainerScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import react.dom.div

@OptIn(WorkflowUiExperimentalApi::class) val PanelContainerScreen =
  componentFactory<PanelContainerScreen<Any, Any>> { rendering ->
    div {
      WorkflowStub(rendering.beneathModals)
      rendering.modals.firstOrNull()?.also {
        mContainer(maxWidth = md, fixed = false) {
          mPaper(square = true) {
            WorkflowStub(it)
          }
        }
      }
    }
  }

