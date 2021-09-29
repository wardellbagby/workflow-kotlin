import com.squareup.sample.gameworkflow.Board
import com.squareup.sample.gameworkflow.Player
import com.squareup.sample.gameworkflow.Player.O
import com.squareup.sample.gameworkflow.Player.X
import kotlinx.css.Display.grid
import kotlinx.css.GridTemplateAreas
import kotlinx.css.GridTemplateColumns
import kotlinx.css.GridTemplateRows
import kotlinx.css.TextAlign.center
import kotlinx.css.background
import kotlinx.css.display
import kotlinx.css.fontSize
import kotlinx.css.gridTemplateAreas
import kotlinx.css.gridTemplateColumns
import kotlinx.css.gridTemplateRows
import kotlinx.css.height
import kotlinx.css.lineHeight
import kotlinx.css.pct
import kotlinx.css.properties.lh
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.css.width
import kotlinx.html.js.onClickFunction
import react.PropsWithChildren
import react.RBuilder
import react.dom.attrs
import react.dom.onClick
import react.fc
import styled.css
import styled.styledDiv

external interface SquareProps : PropsWithChildren {
  var player: Player?
  var backgroundColor: String
  var onClick: () -> Unit
}

fun RBuilder.Square(
  player: Player?,
  backgroundColor: String,
  onClick: () -> Unit
) {
  child(square) {
    this.attrs.player = player
    this.attrs.backgroundColor = backgroundColor
    this.attrs.onClick = onClick
  }
}

private val square = fc<SquareProps> { props ->
  styledDiv {
    css {
      background = props.backgroundColor
      textAlign = center
      height = 100.pct
      width = 100.pct
      lineHeight = 200.px.lh
      fontSize = 72.px
    }

    attrs {
      onClickFunction = {
        console.log("Clicking a square")
        props.onClick()
      }
    }

    +(props.player.let {
      when (it) {
        X -> "❌"
        O -> "\uD83D\uDD35"
        null -> " "
      }
    })
  }
}

fun RBuilder.Board(
  boardState: Board,
  onClick: (row: Int, col: Int) -> Unit
) {
  child(board) {
    this.attrs.boardState = boardState
    this.attrs.onClick = onClick

  }
}

//todo stop using PropsWithChildren
external interface BoardProps : PropsWithChildren {
  var boardState: Board
  var onClick: (row: Int, col: Int) -> Unit
}

private val board = fc<BoardProps> { props ->
  styledDiv {
    css {
      display = grid
      gridTemplateColumns = GridTemplateColumns(200.px, 200.px, 200.px)
      gridTemplateRows = GridTemplateRows(200.px, 200.px, 200.px)
      gridTemplateAreas = GridTemplateAreas(". . .\n. . .\n...")
    }

    props.boardState.flatten().forEachIndexed { index, player ->
      Square(
        player = player,
        backgroundColor = if (index % 2 == 0) {
          "#616161"
        } else {
          "#EEEEEE"
        },
        onClick = {
          console.log("Clicking square", index / 3, index % 3)
          props.onClick(index / 3, index % 3)
        }
      )
    }
  }
}
