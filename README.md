<h1 align="center">
 Qalculate! Android UI
</h1>

<!-- description -->
<p align="center">
  <strong>~ Powerful and versatile multi-purpose calculator for the Android platform ~</strong>
  <br/>
  :warning: <i>This project is in very early development state</i>
    <br />
    <a href="https://github.com/mr-kenhoff/qalculate-android/issues/new">Report Bug</a>
    ·
    <a href="https://github.com/mr-kenhoff/qalculate-android/issues/new">Request Feature</a>
</p>
Android UI of the almighty <a href="https://qalculate.github.io">Qalculate!</a> calculator. It is simple to use but provides power and versatility normally reserved for complicated math packages, as well as useful tools for everyday needs (such as currency conversion and percent calculation). Features include a large library of customizable functions, unit calculations and conversion, symbolic calculations (including integrals and equations), arbitrary precision, uncertainty propagation, interval arithmetic, plotting, and a user-friendly interface.

<br/>
<br/>
<p align="center">
  <a href="https://user-images.githubusercontent.com/5811634/160284839-a76dd58b-ae7e-447e-8697-ea81ff5c2d74.png">
    <img src="https://user-images.githubusercontent.com/5811634/160284839-a76dd58b-ae7e-447e-8697-ea81ff5c2d74.png" width="250">
  </a>
</p>

## Compiling
The project can only be built under Linux (maybe MacOS too) but not on Windows as it relies on bash shell scripts. On Windows, you can use a virtual machine.

You need to have Android Studio and the Android `NDK` installed. For building the native binaries, you also need `swig`.

Use the following commands: 
-```
-git clone https://github.com/jherkenhoff/libqalculate-android
-git clone https://github.com/jherkenhoff/qalculate-android
-cd qalculate-android
-./gradlew assembleDebug
-```

If there is any issue, please see the more detailed compilation instructions in [libqalculate-android](https://github.com/jherkenhoff/qalculate-android).

## Features
Features specific to qalculate-android:

* Platform-Native Graphical user interface
* Simple default view
* Optional calculate-as-you-type mode (enabled by default)
* Fast sliding to type special characters (similarly to [Calculator++](https://github.com/Bubu/android-calculatorpp))

Features from libqalculate:

* Calculation and parsing:
* Result display:
* Symbolic calculation:
* Functions:
* Units:
* Variables and constants:
* Plotting:
* and more...

Missing feature:

* Only the final result is shown, even if approximated. Hence we miss on other representations such as with rational numbers, and exact solutions, as only the approximated one will be shown. Eg, solve2(10x=4y^2; sqrt(y)=1; x; y) = 2 ∕ 5 = 0,4   -- another example: (-1)^(1/4) = ((1 + i) ⋅ √(2)) ∕ 2 ≈ 0,707 106 78 + 0,707 106 78i

_For more details about the syntax, and available functions, units, and variables, please consult the manual (https://qalculate.github.io/manual/)_

## License

This project was created by jherkenhoff.

This project is licensed under the GNU General Public License v2.0 (GPLv2).
