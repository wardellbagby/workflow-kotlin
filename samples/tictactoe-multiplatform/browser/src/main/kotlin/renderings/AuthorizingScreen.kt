import com.ccfraser.muirwik.components.MGridAlignItems.center
import com.ccfraser.muirwik.components.MGridDirection.column
import com.ccfraser.muirwik.components.MGridJustify
import com.ccfraser.muirwik.components.direction
import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.mGridContainer
import com.ccfraser.muirwik.components.mTypography
import com.squareup.sample.authworkflow.AuthorizingScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class) val AuthorizingScreen =
  componentFactory<AuthorizingScreen> { rendering ->
    mGridContainer(alignItems = center, justify = MGridJustify.center) {
      attrs {
        direction = column
      }

      mCircularProgress()
      mTypography(rendering.message)
    }
  }
