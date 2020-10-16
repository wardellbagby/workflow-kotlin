package com.squareup.sample.poetry

import com.squareup.sample.container.overviewdetail.OverviewDetailScreen
import com.squareup.sample.poetry.PoemWorkflow.Action.ClearSelection
import com.squareup.sample.poetry.PoemWorkflow.Action.HandleStanzaListOutput
import com.squareup.sample.poetry.PoemWorkflow.Action.SelectNext
import com.squareup.sample.poetry.PoemWorkflow.Action.SelectPrevious
import com.squareup.sample.poetry.PoemWorkflow.ClosePoem
import com.squareup.sample.poetry.StanzaWorkflow.Output.CloseStanzas
import com.squareup.sample.poetry.StanzaWorkflow.Output.ShowNextStanza
import com.squareup.sample.poetry.StanzaWorkflow.Output.ShowPreviousStanza
import com.squareup.sample.poetry.StanzaWorkflow.Props
import com.squareup.sample.poetry.model.Poem
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.WorkflowAction.Companion.noAction
import com.squareup.workflow1.parse
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backstack.BackStackScreen
import com.squareup.workflow1.ui.backstack.toBackStackScreen

/**
 * Renders a [Poem] as a [OverviewDetailScreen], whose overview is a [StanzaListRendering]
 * for the poem, and whose detail traverses through [StanzaRendering]s.
 */
object PoemWorkflow : StatefulWorkflow<Poem, Int, ClosePoem, OverviewDetailScreen>() {
  object ClosePoem

  override fun initialState(
    props: Poem,
    snapshot: Snapshot?
  ): Int {
    return snapshot?.bytes?.parse { source -> source.readInt() }
        ?: -1
  }

  @OptIn(WorkflowUiExperimentalApi::class)
  override fun RenderContext.render(): OverviewDetailScreen {
    val previousStanzas: List<StanzaRendering> =
      if (state == -1) emptyList()
      else props.stanzas.subList(0, state)
          .mapIndexed { index, _ ->
            renderChild(StanzaWorkflow, Props(props, index), "$index") { noAction() }
          }

    val visibleStanza =
      if (state < 0) {
        null
      } else {
        renderChild(
            StanzaWorkflow, Props(props, state), "$state"
        ) {
          when (it) {
            CloseStanzas -> ClearSelection
            ShowPreviousStanza -> SelectPrevious
            ShowNextStanza -> SelectNext
          }
        }
      }

    val stackedStanzas = visibleStanza?.let {
      (previousStanzas + visibleStanza).toBackStackScreen<Any>()
    }

    val stanzaIndex =
      renderChild(StanzaListWorkflow, props) { selected ->
        HandleStanzaListOutput(selected)
      }
          .copy(selection = state)
          .let { BackStackScreen<Any>(it) }

    return stackedStanzas
        ?.let { OverviewDetailScreen(overviewRendering = stanzaIndex, detailRendering = it) }
        ?: OverviewDetailScreen(
            overviewRendering = stanzaIndex,
            selectDefault = { actionSink.send(HandleStanzaListOutput(0)) }
        )
  }

  override fun snapshotState(state: Int): Snapshot = Snapshot.write { sink ->
    sink.writeInt(state)
  }

  private sealed class Action : WorkflowAction<Poem, Int, ClosePoem>() {
    object ClearSelection : Action()
    object SelectPrevious : Action()
    object SelectNext : Action()
    class HandleStanzaListOutput(val selection: Int) : Action()
    object ExitPoem : Action()

    // We continue to use the deprecated method here for one more release, to demonstrate
    // that the migration mechanism works.
    override fun Updater.apply() {
      when (this@Action) {
        ClearSelection -> state = -1
        SelectPrevious -> state -= 1
        SelectNext -> state += 1
        is HandleStanzaListOutput -> {
          if (selection == -1) setOutput(ClosePoem)
          state = selection
        }
        ExitPoem -> setOutput(ClosePoem)
      }
    }
  }
}
