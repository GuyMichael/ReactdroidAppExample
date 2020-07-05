package com.guymichael.componentapplicationexample.ui.countries

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.execute
import com.guymichael.componentapplicationexample.logic.countries.CountriesLogic
import com.guymichael.componentapplicationexample.network.client.countries.data.CountryData
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.datatype.DataTypeCountry
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.props.CharSequenceProps
import com.guymichael.kotlinreact.model.props.DataProps
import com.guymichael.kotlinreact.model.props.StringProps
import com.guymichael.reactdroid.core.model.AViewComponent
import com.guymichael.reactdroid.extensions.components.edittext.withStringInput
import com.guymichael.reactdroid.extensions.components.list.ComponentListUtils
import com.guymichael.reactdroid.extensions.components.list.items.TextItem
import com.guymichael.reactdroid.extensions.components.list.model.ListItemProps
import com.guymichael.reactdroid.extensions.components.list.model.ListProps
import com.guymichael.reactdroid.extensions.components.list.withList
import com.guymichael.reactdroidflux.model.connect

class CountriesPage(layout: View) : AViewComponent<EmptyOwnProps, CountriesPageState>(layout) {
    override fun createInitialState(props: EmptyOwnProps) = CountriesPageState()

    private val cInputConstraint = withStringInput(R.id.country_name_constraint) {
        //user changed 'search' constraint. Set a new state to re-render
        setState(CountriesPageState(it))
    }

    private val cList = connect(withList(R.id.countries_list)
        , ::mapStateToCountries
        , { MainStore }
    )

    override fun componentDidMount() {
        CountriesLogic.loadOrFetchCountries().execute(this, withBlockUiProgress = true)
    }

    override fun render() {
        cInputConstraint.onRender(ownState.constraint)
        cList.onRender(StringProps(ownState.constraint))
    }
}








private fun mapFirstLetterToProps(letter: Char): ListItemProps<CharSequenceProps> {
    return ListItemProps(letter.toLowerCase().toString()                //id
        , R.layout.item_country_letter_header
        , CharSequenceProps(letter.toUpperCase().toString())            //name
        , ::TextItem
    )
}

private fun mapCountryToProps(country: CountryData): ListItemProps<DataProps<CountryData>> {
    return ListItemProps(country.name, R.layout.item_country, DataProps(country), ::CountryItem)
}

private fun mapCountriesToPropsAndHeadersSorted(countries: List<CountryData>): List<ListItemProps<*>> {
    return countries
        .groupBy { it.name.first().toUpperCase() }  //group to map first letters to countries
        .toSortedMap()                              //sort map (keys) by first letter (natural order)
        .flatMap {                                  //join all countries to one sorted 'props' list
            //add first letter header before related countries
            listOf(mapFirstLetterToProps(it.key))
            //add countries (as props), sort countries in each group
            .plus(it.value.map(::mapCountryToProps).sortedBy { country -> country.props.data.name })
        }
}

//note: this method is called off main thread, so it is fine it takes a little work.
// (called by the connect() method, ultimately by the Store)
/**
 * loads countries from state and map to `ListProps`.
 * Also, we used this method to add a `constraint` 'apiProps' to our list -
 * so that we will pass it a search string, and it will do the filtering
 */
private fun mapStateToCountries(state: GlobalState, constraint: StringProps): ListProps {
    return ListProps(DataTypeCountry.getAll(state)  //get all countries from state (cache)
        ?.let {
            if (constraint.value.isNullOrBlank()) {
                //no constraint. Show sorted countries with first-letter headers
                mapCountriesToPropsAndHeadersSorted(it)
            } else {
                //user typed a constraint. Filter and sort the countries, no first-letter headers
                ComponentListUtils.filterAnyWordStartsWith(it
                    , constraint.value
                    , CountryData::name
                )
                    .sortedBy(CountryData::name)
                    .map(::mapCountryToProps)
            }
        }
        ?: emptyList()
    )
}//NOCOMMIT netflix first api not persisted/shown.