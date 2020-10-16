package com.squareup.sample.poetry

import com.squareup.sample.poetry.model.Poem
import com.squareup.workflow1.StatelessWorkflow

/**
 * Given a [Poem], renders a list of its [initialStanzas][Poem.initialStanzas].
 *
 * Output is the index of a clicked stanza, or -1 on exit.
 */
object StanzaListWorkflow : StatelessWorkflow<Poem, Int, StanzaListRendering>() {

  override fun RenderContext.render(): StanzaListRendering {
    return StanzaListRendering(
        title = props.title,
        subtitle = props.poet.fullName,
        firstLines = props.initialStanzas,
        onStanzaSelected = eventHandler { index -> setOutput(index) },
        onExit = eventHandler { setOutput(-1) }
    )
  }
}

data class StanzaListRendering(
  val title: String,
  val subtitle: String,
  val firstLines: List<String>,
  val onStanzaSelected: (Int) -> Unit,
  val onExit: () -> Unit,
  val selection: Int = -1
)
