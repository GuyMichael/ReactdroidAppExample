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
MainActivity extends ReactiveApp's BaseActivity, which basically makes the native Android Activity behave just like an *AComponent*.
While it is possible to use an Activity (both the native one and the BaseActivity) to manipulate the UI, it is not encouraged.
Activities supplies Context, but they are not a UI building block.
So what we will do is, wrap the (Main) Activity's layout (ViewGroup) with an *AComponent* - which we will call - MainPage.
MainPage is an *AComponent* and so will be the one in charge of MainActivity's layout - the Drawer, Toolbar, FAB and Dialog.
In fact, as Jetpack's Navigation handles the Drawer's Fragment navigation (and Toolbar's back) [in xml only](https://github.com/GuyMichael/ReactiveAppExample/blob/master/app/src/main/res/navigation/mobile_navigation.xml), the MainPage will practically need to handle
the Dialog alone.



[Countries](https://github.com/GuyMichael/ReactiveAppExample/blob/master/app/src/main/java/com/guymichael/componentapplicationexample/ui/countries)
