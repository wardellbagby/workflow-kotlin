import react.RBuilder
import react.ReactElement

interface ReactComponentRendering {
  val componentFactory: RBuilder.() -> ReactElement
}
