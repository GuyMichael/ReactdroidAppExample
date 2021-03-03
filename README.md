# ReactiveAppExample
A working Android application example for using the [ReactiveApp](https://github.com/GuyMichael/ReactiveApp) and/or the [Reactdroid](https://github.com/GuyMichael/Reactdroid) library, directly.

If you want to contribute (Android/iOS), please contact me:)

## Learning Reactdroid and the ReactiveApp library
The best way to learn how to use the architecture/library, is to show and explain the code present in this example app,
so if you'd like to test it, you could just fork it and run on your device.

To import ReactiveApp using Gradle:
```kotlin
  implementation 'com.github.GuyMichael:ReactiveApp:0.0.4'
```

### App Overview
To start the tutorial, we should first understand the example app structure. That structure is designed to showcase
as many features and usages as the libraries have to offer - it isn't necessarily how you'd want to structure your actual app.

It has a [**MainActivity**](https://github.com/GuyMichael/ReactiveAppExample/blob/master/app/src/main/java/com/guymichael/componentapplicationexample/ui/MainActivity.kt)
with a [layout](https://github.com/GuyMichael/ReactiveAppExample/blob/master/app/src/main/res/layout/activity_main.xml) that consists of only a Drawer (NavigationView) and its Fragments container.
The Drawer is used to switch between the Pages (as Fragments) where every page is a totally different feature.
The default page is the [Home Page](https://github.com/GuyMichael/ReactiveAppExample/tree/master/app/src/main/java/com/guymichael/componentapplicationexample/ui/home).

So basically the MainActivity and the [MainPage](https://github.com/GuyMichael/ReactiveAppExample/blob/master/app/src/main/java/com/guymichael/componentapplicationexample/ui/MainPage.kt) have no UI (screen), other than being the app's 'frame' which consists of a Toolbar, a Drawer, a FAB and a Dialog. So the first page you see when you first open the app is the Home Page, which is just a page inside the Drawer - not the Main Page.

### First Step - Creating the Main Activity
First, we will create the MainActivity and MainPage of the app, which handle the app's frame (Toolbar, Drawer, FAB and a Dialog that can be shown on top of all Drawer pages).
MainActivity extends ReactiveApp's *BaseActivity* which extends Reactdroid's [*ComponentActivity*](https://github.com/GuyMichael/Reactdroid/blob/master/reactdroid/src/main/java/com/guymichael/reactdroid/core/activity/ComponentActivity.kt), which basically makes the native Android Activity behave just like an *AComponent*. If you're not yet familiar with them, you can read more in [ReactiveApp's turotial](https://github.com/GuyMichael/ReactiveApp)
While it is possible to use an Activity (both the native one and the BaseActivity/ComponentActivity) to manipulate the UI, it is not encouraged.
Activities supply Context, but they are not a UI building block.
So what we will do is, wrap the (Main) Activity's layout (ViewGroup) with an *AComponent* - which we will call - MainPage.
MainPage is an *AComponent* and so will be the one in charge of MainActivity's layout - the Drawer, Toolbar, FAB and Dialog.
In fact, as Jetpack's Navigation handles the Drawer's Fragment navigation (and Toolbar's back) [in xml only](https://github.com/GuyMichael/ReactiveAppExample/blob/master/app/src/main/res/navigation/mobile_navigation.xml), the MainPage will practically need to handle
the Dialog alone.

Below is a simplified version of the MainActivity with some explanation. More detailed explanation can be found in the Reactdroid and ReactiveApp tutorials.
```kotlin

  class MainActivity
  : BaseActivity<EmptyOwnProps, AComponent<EmptyOwnProps, *, *>, EmptyOwnProps>() {
             //activity-props   //page class                     //page's-props class
  
      //a getter to get the xml layout for this activity (replaces setContentView()).
      // It's defined in ComponentActivity
      override fun getLayoutRes() = R.layout.activity_main
      
      //a function used to convert the activity's Intent to the activity-props.
      // In this case, it's a no-op as this activity uses empty props
      override fun mapIntentToProps(intent: Intent) = EmptyOwnProps
      
      //a creator function to create the activity's page - an AComponent that will handle its UI
      override fun createPageComponent(activityView: ViewGroup) = MainPage.connected(activityView)
      
      //a function used to convert the activity-props to the AComponent page's props.
      // In this case, it's a no-op as both this activity and the page use empty props
      override fun mapActivityPropsToPageProps(props: EmptyOwnProps) = EmptyOwnProps

      //a callback to prepare the activity's frame (non-page UI).
      // It's defined in BaseActivity and basically replaces 'onCreate()'
      // for non-page related tasks
      override fun onPrepareActivityFrame() {
      
          //setup the toolbar (BaseActivity's method)
          withToolbar(R.id.toolbar)

          //setup the fab (BaseActivity's method)
          withFab(R.id.fab) { view ->
              Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  .setAction("Action", null).show()
          }

          //setup the drawer (BaseActivity's method)
          withDrawer(
              R.id.drawer_layout
              , R.id.nav_view
              , R.id.nav_host_fragment
              ...
          )
      }
  }
```
As you can see, there's not much code here. Merely implementations of (abstract) methods defined in ComponentActivity and BaseActivity which help
defining the layout, page and props handling between the Intent, the (Main) activity props and the (Main) page props.
Don't worry if you don't fully understand everything. We will repeat similar steps over and over when we'll define the features -
which built using Fragments and (AComponent) pages - and use the same technique.

### Second Step - Creating the Main Page
As mentioned above and seen in above code, the Main page is the *AComponent* which is in charge of the Main Activity's UI.
```kotlin
class MainPage(v: View) : ASimpleComponent<MainPageProps>(v) {

    //this is how we define child components from within parent components:
    // withAlertDialog uses Kotlin Extension and so is a member function of any AComponent.
    // It creates a CAlertDialog (a Dialog AComponent)
    private val cDialog = withAlertDialog(
        //when dialog is dismissed, dispatch to update the global state (shown = false)
        onDismiss = { MainStore.dispatch(GeneralReducerKey.welcomeDialogShown, false) }
    )

    //inside render - call onRender of all child components.
    // In this case, only the dialog
    override fun render() {
        cDialog.onRender(AlertDialogProps(
            shown = props.welcomeDialogShown
            , title = getString(R.string.home_dialog_title)
            , message = getString(R.string.home_dialog_msg)
            
            //as positive btn clicks auto-dismiss the dialog we can pass null
            // as the click-listener.
            // We can return an APromise instead, to delay the dismissal for until
            // that promise finishes
            , positiveBtn = getString(R.string.dialog_btn_ok) to null
        ))
    }



    //this is how it's best to 'connect' an AComponent to the Store.
    // We just define a non-connected AComponent with the props it needs,
    // then we provide a 'connected' creator inside the companion object,
    // like a 'static' Java method
    companion object {
    
        @JvmStatic
        fun connected(v: View): AHOC<EmptyOwnProps, *, *, *, EmptyOwnState> {
            return connect(
                MainPage(v)
                , MainPageProps.Companion::mapStateToProps
                , { MainStore }
            )
        }
    }
}


//and here is how we define props properly, also with the same approach
// of adding a helper method inside the companion object, to help connect
// that component
data class MainPageProps(val welcomeDialogShown: Boolean) : OwnProps() {
    override fun getAllMembers() = listOf(welcomeDialogShown)

    companion object {
        fun mapStateToProps(state: GlobalState, apiProps: EmptyOwnProps): MainPageProps {
            return MainPageProps(
                welcomeDialogShown = GeneralReducerKey.welcomeDialogShown.getBoolean(state) == true
            )
        }
    }
}
```

This concludes the high level app structure. We now have a Main page that can handle showing some Dialog over
all other Drawer-pages, by being *connected* to the *Store* to automatically (re) *render* (show/hide the dialog)
if/when the *GeneralReducerKey.welcomeDialogShown* state-key changes its (boolean) value.
This means that if some inner Drawer-page changes that key's value to *false* (by *dispatching* to the *Store*),
this Dialog will be hidden - no other code is necessary - no direct setter on that dialog, no global variables somewhere,
and no risk of leaking the *Context*.


### Third Step - Creating our first feature - Countries Search
We can finally start adding features (Drawer pages).
Our first feature will be a simple list of (all) [countries](https://github.com/GuyMichael/ReactiveAppExample/tree/master/app/src/main/java/com/guymichael/componentapplicationexample/ui/countries), along with a 'searchbox' to let user
type text. Upon typing, the list will auto-filter to show only relevant countries.
Oh and, of course, we will *fetch* the countries using some API.

Let's define the Countries Fragment, which will hold a Countries Page.
Yes, same approach as with the MainActivity/Page above.

```kotlin
  class CountriesFragment : BaseFragment<EmptyOwnProps, AComponent<EmptyOwnProps, *, *>, EmptyOwnProps>() {

    private lateinit var cCountriesPage: CountriesPage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        : View {

        return inflater.inflate(R.layout.fragment_countries, container, false).also {
            cCountriesPage = CountriesPage(it)
            cCountriesPage.onRender(EmptyOwnProps)
        }
    }
}
```
[Countries](https://github.com/GuyMichael/ReactiveAppExample/blob/master/app/src/main/java/com/guymichael/componentapplicationexample/ui/countries)
