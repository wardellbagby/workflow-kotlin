import com.ccfraser.muirwik.components.MGridAlignItems.center
import com.ccfraser.muirwik.components.MGridDirection.column
import com.ccfraser.muirwik.components.MGridJustify
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.direction
import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mGridContainer
import com.ccfraser.muirwik.components.mTypography
import com.squareup.sample.gameworkflow.GameOverScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import kotlinx.css.height
import kotlinx.css.pct
import styled.css

@OptIn(WorkflowUiExperimentalApi::class) val GameOverScreen =
  componentFactory<GameOverScreen> { rendering ->
    mContainer {
      mGridContainer(alignItems = center, justify = MGridJustify.center) {
        attrs {
          direction = column
        }
        css {
          height = 100.pct
        }
        mTypography("The game is over! ${rendering.endGameState.completedGame.lastTurn.playing.name} ended the game with a ${rendering.endGameState.completedGame.ending.name}!")
        mButton("Play again?") {
          attrs {
            onClick = {
              rendering.onPlayAgain()
            }
          }
        }
      }
    }
  }

