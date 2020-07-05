package com.guymichael.componentapplicationexample.ui.inputsheet

import android.view.View
import android.widget.RadioButton
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.reducers.InputSheetReducerKey
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactiveapp.getString
import com.guymichael.reactdroid.core.getString
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.extensions.components.compoundbtn.CCompoundBtn
import com.guymichael.reactdroid.extensions.components.compoundbtn.CompoundBtnProps
import com.guymichael.reactdroid.extensions.components.compoundbtn.checkbox.CCheckbox
import com.guymichael.reactdroid.extensions.components.compoundbtn.checkbox.CheckboxProps
import com.guymichael.reactdroid.extensions.components.compoundbtn.checkbox.withCheckbox
import com.guymichael.reactdroid.extensions.components.compoundbtn.radiobtn.CRadioGroup
import com.guymichael.reactdroid.extensions.components.compoundbtn.radiobtn.RadioGroupProps
import com.guymichael.reactdroid.extensions.components.compoundbtn.radiobtn.withRadioGroup
import com.guymichael.reactdroid.extensions.components.compoundbtn.switches.CSwitch
import com.guymichael.reactdroid.extensions.components.compoundbtn.switches.SwitchProps
import com.guymichael.reactdroid.extensions.components.compoundbtn.switches.withSwitch
import com.guymichael.reactdroid.extensions.components.compoundbtn.toggle.CToggle
import com.guymichael.reactdroid.extensions.components.compoundbtn.toggle.ToggleProps
import com.guymichael.reactdroid.extensions.components.compoundbtn.toggle.withToggle
import com.guymichael.reactdroid.extensions.components.compoundbtn.withCompoundBtn
import com.guymichael.reactdroid.extensions.components.seekbar.CSeekbar
import com.guymichael.reactdroid.extensions.components.seekbar.SimpleSeekbarProps
import com.guymichael.reactdroid.extensions.components.seekbar.withSeekbar
import com.guymichael.reactdroid.extensions.components.text.renderText
import com.guymichael.reactdroid.extensions.components.text.withText
import com.guymichael.reactdroidflux.model.connect

class InputSheetPage(v: View) : AComponent<InputSheetProps, EmptyOwnState, View>(v) {
    override fun createInitialState(props: InputSheetProps) = EmptyOwnState

    /* section 1 (checkboxes) */
    private val cCheckbox1 = withCheckbox(R.id.checkbox_1)
    private val cCheckbox2 = withCheckbox(R.id.checkbox_2)
    private val cCheckbox3 = withCheckbox(R.id.checkbox_3)
    private val cTxtCheckboxSummary = withText(R.id.checkbox_summary)

    /* section 2 (radio buttons) */
    private val cRadioGroup = withRadioGroup(R.id.radio_group)
    private val cTxtRadioSummary = withText(R.id.radio_summary)

    /* section 3 (toggles) */
    private val cToggle1 = withToggle(R.id.toggle_1)
    private val cToggle2 = withToggle(R.id.toggle_2)
    private val cToggle3 = withToggle(R.id.toggle_3)
    private val cTxtToggleSummary = withText(R.id.toggle_summary)

    /* section 4 (Switches) */
    private val cSwitch1 = withSwitch(R.id.switch_1)
    private val cSwitch2 = withSwitch(R.id.switch_2)
    //treat switch3 as a compoundBtn - we could've done that for ALL buttons and toggles here!
    // this is to demonstrate that if two Views have same base class, you can have just 1 Component
    // class to wrap both of them and treat them the same (logically)
    private val cSwitch3 = withCompoundBtn(R.id.switch_3_which_is_actually_switchmaterial)
    private val cTxtSwitchSummary = withText(R.id.switch_summary)

    /* section 5 (Seekbars) */
    private val cSeekbar1 = withSeekbar(R.id.seekbar_1)
    private val cSeekbar2 = withSeekbar(R.id.seekbar_2)
    private val cSeekbar3 = withSeekbar(R.id.seekbar_3)
    private val cTxtSeekbarSummary = withText(R.id.seekbar_summary)




    //on some cases, a page's state should be cleared when it's closed,
    // and we can easily do it by clearing out specific Store-keys, or entire reducers.
    // --> This is why it's best practice to assign a reducer for each and every feature / page
    override fun componentWillUnmount() {
        //if we enable this line, the state of the inputs will be cleared when this page closes.
        // If we disable it, the entire input state will be saved (in Store) and automatically shown
        // when this page re-opens (until app is destroyed)
//        MainStore.dispatchClearState(InputSheetReducer)
    }


    //note: best practice is to export each inner "section" as a separate method or component,
    //      depending on the complexity.
    override fun render() {
        /* section 1 */
        //we cache the props state at start for best practice (stateless), although you may assume
        // that props do not change during 'render's
        val c_checked1 = props.checkbox1
        val c_checked2 = props.checkbox2
        val c_checked3 = props.checkbox3
        renderCheckbox(cCheckbox1, c_checked1, R.string.input_checkbox_1, InputSheetReducerKey.checkbox1_checked)
        renderCheckbox(cCheckbox2, c_checked2, R.string.input_checkbox_2, InputSheetReducerKey.checkbox2_checked)
        renderCheckbox(cCheckbox3, c_checked3, R.string.input_checkbox_3, InputSheetReducerKey.checkbox3_checked)
        cTxtCheckboxSummary.renderText(getString(R.string.input_checkbox_summary_format_d
            , countChecked(c_checked1, c_checked2, c_checked3)
        ))

        /* section 2 */
        @IdRes val r_checked_id = props.radioCheckedId
        renderRadioGroup(cRadioGroup, r_checked_id, InputSheetReducerKey.radio_checked_id)
        cTxtRadioSummary.renderText(getString(R.string.input_radio_summary_format_s
            , r_checked_id?.takeIf { it != 0 && it != -1 }?.let {
                mView.findViewById<RadioButton>(it).text
            } ?: "-"
        ))

        /* section 3 */
        val t_checked1 = props.toggle1
        val t_checked2 = props.toggle2
        val t_checked3 = props.toggle3
        renderToggle(cToggle1, t_checked1, R.string.input_toggle_1, InputSheetReducerKey.toggle_checked_1)
        renderToggle(cToggle2, t_checked2, R.string.input_toggle_2, InputSheetReducerKey.toggle_checked_2)
        renderToggle(cToggle3, t_checked3, R.string.input_toggle_3, InputSheetReducerKey.toggle_checked_3)
        cTxtToggleSummary.renderText(getString(R.string.input_toggle_summary_format_d
            , countChecked(t_checked1, t_checked2, t_checked3)
        ))

        /* section 4 */
        val s_checked1 = props.switch1
        val s_checked2 = props.switch2
        val s_checked3 = props.switch3
        renderSwitch(cSwitch1, s_checked1, R.string.input_switch_1, InputSheetReducerKey.switch_checked_1)
        renderSwitch(cSwitch2, s_checked2, R.string.input_switch_2, InputSheetReducerKey.switch_checked_2)
        renderCompoundBtn(cSwitch3, s_checked3, R.string.input_switch_3, InputSheetReducerKey.switch_checked_3)
        cTxtSwitchSummary.renderText(getString(R.string.input_switch_summary_format_d
            , countChecked(s_checked1, s_checked2, s_checked3)
        ))

        /* section 5 */
        val seekbar1 = props.seekbar1
        val seekbar2 = props.seekbar2
        val seekbar3 = props.seekbar3
        renderSeekbar(cSeekbar1, seekbar1, InputSheetReducerKey.seekbar1_progress)
        renderSeekbar(cSeekbar2, seekbar2, InputSheetReducerKey.seekbar2_progress)
        renderSeekbar(cSeekbar3, seekbar3, InputSheetReducerKey.seekbar3_progress)
        cTxtSeekbarSummary.renderText(getString(R.string.input_seekbar_summary_format_s
            , "1: $seekbar1, 2: $seekbar2, 3: $seekbar3"
        ))
    }





    //export as a connected component.
    //note: as-is, this page component has low value as non-connected, because it has hard-coded
    //      store-dispatches - instead of callbacks in props for ALL changes - for the parent
    //      component to actually dispatch the changes.
    // --> This is FINE for top-level (app-level) components as they are intentionally meant to be
    //     used in a specific way. But for generic components, such as library-level ones,
    //     it is best practice not to assume ANY outside relations.
    //     In such case, the 'connected' method's 'mapStateToProps' will also map callbacks
    //     to dispatches (or local setState calls on the parent), and will not even be inside
    //     the original component's companion-object.
    // --> It might make sense sometimes to force a library-level component to be connected -
    //     to force best practice - in which case you should export the Store supplier and dispatch
    //     store-keys, as props, so it will be possible to connect your component to whatever Store,
    //     and whatever store-keys
    companion object {
        fun connected(v: View) = connect(
            InputSheetPage(v)
            , InputSheetProps.Companion::mapStateToProps
            , { MainStore }
        )
    }
}








//we put these methods here (outside of class scope) to clarify (and make sure) that they are
// stateless - depend only on the input arguments - even though they are NOT entirely pure -
// they dispatch to an outside Store.
//Note: Dispatching to an outside Store is OK, and probably the only (output) side-effect you should have
// in your 'functional' methods - as the Store is the (only) one special place intended for applying
// side-effects (out of your component or method). Let's call it OUTPUT side-effect.
// What's NOT-OK, is having side-effects as INPUT to the method. So for example, if your method
// depends on some Store's state, you should have this state (GlobalState) as an explicit input argument
// for the method. You may have a 'default' value for easy usage (e.g. state: GlobalState = MainStore.state)

private fun renderRadioGroup(group: CRadioGroup, @IdRes checkedBtn: Int? = null
        , key: InputSheetReducerKey
    ) {

    group.onRender(RadioGroupProps(
        checkedBtnId = checkedBtn,
        initial_onChecked = { id: Int? -> MainStore.dispatch(key, id) }
    ))
}

private fun renderCheckbox(switch: CCheckbox, checked: Boolean, @StringRes name: Int
        , key: InputSheetReducerKey
    ) {

    switch.onRender(CheckboxProps(
        checked = checked to { newChecked: Boolean -> MainStore.dispatch(key, newChecked) },
        text = "${getString(name)}: ${(if (checked) "v" else "x")}"
    ))
}

private fun renderToggle(toggle: CToggle, checked: Boolean, @StringRes name: Int
        , key: InputSheetReducerKey
    ) {

    toggle.onRender(ToggleProps(
        checked = checked to { newChecked: Boolean -> MainStore.dispatch(key, newChecked) },
        text = "${getString(name)}: ${(if (checked) "v" else "x")}"
    ))
}

private fun renderSwitch(switch: CSwitch, checked: Boolean, @StringRes name: Int
        , key: InputSheetReducerKey
    ) {

    switch.onRender(SwitchProps(
        checked = checked to { newChecked: Boolean -> MainStore.dispatch(key, newChecked) },
        text = "${getString(name)}: ${(if (checked) "v" else "x")}"
    ))
}

private fun renderCompoundBtn(btn: CCompoundBtn, checked: Boolean, @StringRes name: Int
        , storeKey: InputSheetReducerKey
    ) {

    btn.onRender(CompoundBtnProps(
        checked = checked to { newChecked: Boolean -> MainStore.dispatch(storeKey, newChecked) },
        text = "${getString(name)}: ${(if (checked) "v" else "x")}"
    ))
}

private fun renderSeekbar(bar: CSeekbar, progress: Int, storeKey: InputSheetReducerKey) {
    bar.onRender(SimpleSeekbarProps(
        progress = progress to { newProgress: Int -> MainStore.dispatch(storeKey, newProgress) },
        min = 0,
        max = 100,
        initial_progressCallbackDebounceMs = Long.MAX_VALUE //we use debounce to reduce dispatches,
                                                            // and Long.MAX_VALUE in particular
                                                            // to receive updates only after user
                                                            // stopped interacting ("on-touch-up")
    ))
}

private fun countChecked(vararg checked: Boolean): Int {
    return checked.count { it }
}