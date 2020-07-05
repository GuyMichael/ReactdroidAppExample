package com.guymichael.componentapplicationexample.ui.inputsheet

import androidx.annotation.IdRes
import com.guymichael.componentapplicationexample.store.reducers.InputSheetReducerKey
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.OwnProps

data class InputSheetProps(
        val checkbox1: Boolean,
        val checkbox2: Boolean,
        val checkbox3: Boolean,

        @IdRes val radioCheckedId: Int?,

        val toggle1: Boolean,
        val toggle2: Boolean,
        val toggle3: Boolean,

        val switch1: Boolean,
        val switch2: Boolean,
        val switch3: Boolean,

        val seekbar1: Int,
        val seekbar2: Int,
        val seekbar3: Int
    ) : OwnProps() {

    override fun getAllMembers() = listOf(
        checkbox1, checkbox2, checkbox3
        , radioCheckedId
        , toggle1, toggle2, toggle3
        , switch1, switch2, switch3
        , seekbar1, seekbar2, seekbar3
    )


    companion object {
        //note: for this particular case, we could use local state ('ownState') instead of the whole
        // reducer and keys overhead. BUT, it's a much better practice as it is way more dynamic
        // and allows for future inter-feature connections (e.g. show the 'input' status on the main page)
        fun mapStateToProps(state: GlobalState, apiProps: EmptyOwnProps): InputSheetProps {
            return InputSheetProps(
                checkbox1 = InputSheetReducerKey.checkbox1_checked.getBoolean(state) ?: false,
                checkbox2 = InputSheetReducerKey.checkbox2_checked.getBoolean(state) ?: false,
                checkbox3 = InputSheetReducerKey.checkbox3_checked.getBoolean(state) ?: false,

                radioCheckedId = InputSheetReducerKey.radio_checked_id.getInt(state),

                toggle1 = InputSheetReducerKey.toggle_checked_1.getBoolean(state) ?: false,
                toggle2 = InputSheetReducerKey.toggle_checked_2.getBoolean(state) ?: false,
                toggle3 = InputSheetReducerKey.toggle_checked_3.getBoolean(state) ?: false,

                switch1 = InputSheetReducerKey.switch_checked_1.getBoolean(state) ?: false,
                switch2 = InputSheetReducerKey.switch_checked_2.getBoolean(state) ?: false,
                switch3 = InputSheetReducerKey.switch_checked_3.getBoolean(state) ?: false,

                seekbar1 = InputSheetReducerKey.seekbar1_progress.getInt(state) ?: 0,
                seekbar2 = InputSheetReducerKey.seekbar2_progress.getInt(state) ?: 0,
                seekbar3 = InputSheetReducerKey.seekbar3_progress.getInt(state) ?: 0
            )
        }
    }
}