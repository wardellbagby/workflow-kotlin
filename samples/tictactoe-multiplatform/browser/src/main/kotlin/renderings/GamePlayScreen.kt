import com.ccfraser.muirwik.components.MGridAlignItems.center
import com.ccfraser.muirwik.components.MGridDirection.column
import com.ccfraser.muirwik.components.MGridJustify
import com.ccfraser.muirwik.components.direction
import com.ccfraser.muirwik.components.mGridContainer
import com.squareup.sample.gameworkflow.GamePlayScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class) val GamePlayScreen =
  componentFactory<GamePlayScreen> { rendering ->
    mGridContainer(alignItems = center, justify = MGridJustify.center) {
      attrs {
        direction = column
      }

      Board(rendering.gameState.board, rendering.onClick)
    }
  }

