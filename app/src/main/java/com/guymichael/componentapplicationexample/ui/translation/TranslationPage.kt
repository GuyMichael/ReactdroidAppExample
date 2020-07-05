package com.guymichael.componentapplicationexample.ui.translation

import android.view.View
import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.execute
import com.guymichael.componentapplicationexample.logic.translation.TranslationLogic
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.datatype.DataTypeTranslation
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.props.StringProps
import com.guymichael.reactdroid.core.model.AViewComponent
import com.guymichael.reactdroid.extensions.components.edittext.withStringInput
import com.guymichael.reactdroid.extensions.components.text.TextProps
import com.guymichael.reactdroid.extensions.components.text.withText
import com.guymichael.reactdroidflux.model.connect
import io.reactivex.rxjava3.disposables.Disposable

class TranslationPage(v: View) : AViewComponent<EmptyOwnProps, TranslationPageOwnState>(v) {
    //create the initial state of this page (Component)
    override fun createInitialState(props: EmptyOwnProps) = TranslationPageOwnState.empty()

    private var fetchTranslationDisposable: Disposable? = null

    //we place 'c' at name start to annotate as 'component'
    private val cInput = v.withStringInput(R.id.translation_input, { input -> //wrap EditText with CEditText (Component)
        //update own state on text (input) changes
        setState(TranslationPageOwnState(
            input = input,                           //new input
            resultId = null                          //clear resultId as the input just changed
        ))

        //fetch translation if missing in cache (Store)
        if( !input.isNullOrBlank() && !DataTypeTranslation.exists(input)) {
            fetchTranslation(input)
        }
    })

    private val cTxtResult = connect(withText(R.id.translation_result)  //connect the textView to the Store
        , ::mapResultIdToTranslationText  //map the GlobalState and the translation resultId to TextProps
        , { MainStore }                   //define the Store to connect to
    )



    private fun fetchTranslation(input: String) {
        // for network performance, we delay each request by 500ms and, if new text comes before that,
        // we cancel the request and schedule a new one
        fetchTranslationDisposable?.dispose()                           //cancel prev request
        fetchTranslationDisposable = APromise.ofDelay(500).then {    //schedule a new request
            //fetch translation.
            // Note: We don't actually cancel the API request on text changes -
            // we cancel the scheduling/delay so it won't even start.
            // If it did start, we don't care of canceling it, as its result will be cached
            // in the Store, in case the user searches for this text again
            TranslationLogic.fetchAndDispatchTranslations(input)         //request translation
                //we pass 'this' component to the promise, to show errors to user (see 'execute' docs)
                .execute(this, handleErrorMessage = true, withBlockUiProgress = false)

            //we pass 'this' component to the promise, to automatically cancel (dispose) the scheduling
            //if this component is destroyed (unmounted)
        }.execute(this, autoCancel = true)
    }




    override fun render() {
        //render input
        cInput.onRender(ownState.input)

        //render result - pass on the resultId (nullable).
        // As this Component is connected to the Store, it will automatically map the resultId
        // to the right translation text from cache (nullable).
        // Also, every time the translation will update in the cache (Store), this Component
        // will automatically re-render, as its underlying props (translation text) has changed.
        // See `mapResultIdToTranslationText()` below for more info
        cTxtResult.onRender(StringProps(ownState.resultId))
    }
}











/**
 * Wraps the props passed to our 'result' `TextView` with a mapper,
 * which maps a `resultId` param (id of the relevant translation result) to the result value -
 * the translation text (or null if missing in cache).
 *
 * It does that by:
 * 1. Using [StringProps] to serve as the new props for our 'result' `TextView` -
 *    hold the `resultId` which will be passed on by the TranslationPage (insider `render()`) and
 *    taken from the [ownState][TranslationPageOwnState].
 * 2. Add simple logic to map the `resultId` to the cached value (translation), by retrieving
 *    a translation from the Store - using [DataTypeTranslation.get] and the given `resultId`, if present
 */
private fun mapResultIdToTranslationText(state: GlobalState, apiProps: StringProps)
    = TextProps(apiProps.value?.let { DataTypeTranslation.get(it, state)?.output })