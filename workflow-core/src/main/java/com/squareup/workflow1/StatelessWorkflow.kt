@file:JvmMultifileClass
@file:JvmName("Workflows")

package com.squareup.workflow1

/**
 * Minimal implementation of [Workflow] that maintains no state of its own.
 *
 * @param PropsT Typically a data class that is used to pass configuration information or bits of
 * state that the workflow can always get from its parent and needn't duplicate in its own state.
 * May be [Unit] if the workflow does not need any props data.
 *
 * @param OutputT Typically a sealed class that represents "events" that this workflow can send
 * to its parent.
 * May be [Nothing] if the workflow doesn't need to emit anything.
 *
 * @param RenderingT The value returned to this workflow's parent during [composition][render].
 * Typically represents a "view" of this workflow's props, current state, and children's renderings.
 * A workflow that represents a UI component may use a view model as its rendering type.
 *
 * @see StatefulWorkflow
 */
abstract class StatelessWorkflow<in PropsT, out OutputT, out RenderingT> :
    Workflow<PropsT, OutputT, RenderingT> {

  @Suppress("UNCHECKED_CAST")
  inner class RenderContext internal constructor(
    val props: @UnsafeVariance PropsT,
    baseContext: BaseRenderContext<PropsT, *, OutputT>
  ) : BaseRenderContext<@UnsafeVariance PropsT, Nothing, @UnsafeVariance OutputT> by
  baseContext as BaseRenderContext<PropsT, Nothing, OutputT>

  @Suppress("UNCHECKED_CAST")
  private val statefulWorkflow = Workflow.stateful<PropsT, Unit, OutputT, RenderingT>(
      initialState = { Unit },
      render = { RenderContext(props, this, this@StatelessWorkflow).render() }
  )

  /**
   * Called at least once any time one of the following things happens:
   *  - This workflow's [RenderContext.props] change (via the parent passing a different one in).
   *  - A descendant (immediate or transitive child) workflow:
   *    - Changes its internal state.
   *    - Emits an output.
   *
   * **Never call this method directly.** To get the rendering from a child workflow, pass the child
   * and any required props to [RenderContext.renderChild].
   *
   * This method *should not* have any side effects, and in particular should not do anything that
   * blocks the current thread. It may be called multiple times for the same state. It must do all its
   * work by calling methods on the receiving [RenderContext].
   */
  abstract fun RenderContext.render(): RenderingT

  /**
   * Satisfies the [Workflow] interface by wrapping `this` in a [StatefulWorkflow] with `Unit`
   * state.
   *
   * This method is called a few times per instance, but we don't need to allocate a new
   * [StatefulWorkflow] every time, so we store it in a private property.
   */
  final override fun asStatefulWorkflow(): StatefulWorkflow<PropsT, *, OutputT, RenderingT> =
    statefulWorkflow
}

/**
 * Creates a `RenderContext` from a [BaseRenderContext] for the given [StatelessWorkflow].
 */
@Suppress("UNCHECKED_CAST", "FunctionName")
fun <PropsT, OutputT, RenderingT> RenderContext(
  props: PropsT,
  baseContext: BaseRenderContext<PropsT, *, OutputT>,
  workflow: StatelessWorkflow<PropsT, OutputT, RenderingT>
): StatelessWorkflow<PropsT, OutputT, RenderingT>.RenderContext =
  (baseContext as? StatelessWorkflow<PropsT, OutputT, RenderingT>.RenderContext)
      ?: workflow.RenderContext(props, baseContext)

/**
 * Returns a stateless [Workflow] via the given [render] function.
 *
 * Note that while the returned workflow doesn't have any _internal_ state of its own, it may use
 * [props][PropsT] received from its parent, and it may render child workflows that do have
 * their own internal state.
 */
/* ktlint-disable parameter-list-wrapping */
inline fun <PropsT, OutputT, RenderingT> Workflow.Companion.stateless(
  crossinline render: StatelessWorkflow<PropsT, OutputT, RenderingT>.RenderContext.() -> RenderingT
): Workflow<PropsT, OutputT, RenderingT> =
  object : StatelessWorkflow<PropsT, OutputT, RenderingT>() {
    override fun RenderContext.render(): RenderingT = render()
  }

/**
 * Returns a workflow that does nothing but echo the given [rendering].
 * Handy for testing.
 */
fun <RenderingT> Workflow.Companion.rendering(
  rendering: RenderingT
): Workflow<Unit, Nothing, RenderingT> = stateless { rendering }

/**
 * Convenience to create a [WorkflowAction] with parameter types matching those
 * of the receiving [StatefulWorkflow]. The action will invoke the given [lambda][update]
 * when it is [applied][WorkflowAction.apply].
 *
 * @param name A string describing the update for debugging, included in [toString].
 * @param update Function that defines the workflow update.
 */
fun <PropsT, OutputT, RenderingT>
    StatelessWorkflow<PropsT, OutputT, RenderingT>.action(
  name: String = "",
  update: WorkflowAction<PropsT, *, OutputT>.Updater.() -> Unit
) = action({ name }, update)

/**
 * Convenience to create a [WorkflowAction] with parameter types matching those
 * of the receiving [StatefulWorkflow]. The action will invoke the given [lambda][update]
 * when it is [applied][WorkflowAction.apply].
 *
 * @param name Function that returns a string describing the update for debugging, included in
 * [toString].
 * @param update Function that defines the workflow update.
 */
fun <PropsT, OutputT, RenderingT>
    StatelessWorkflow<PropsT, OutputT, RenderingT>.action(
  name: () -> String,
  update: WorkflowAction<PropsT, *, OutputT>.Updater.() -> Unit
): WorkflowAction<PropsT, Nothing, OutputT> = object : WorkflowAction<PropsT, Nothing, OutputT>() {
  override fun Updater.apply() = update.invoke(this)
  override fun toString(): String = "action(${name()})-${this@action}"
}
