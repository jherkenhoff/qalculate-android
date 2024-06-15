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
  <a href="readme/screenshot.png">
    <img src="readme/screenshot.png" width="250">
  </a>
</p>

## Compiling
You need to have the Android `NDK` installed. For building the native binaries, you also need `swig`.

```
git clone https://github.com/mr-kenhoff/libqalculate-android
git clone https://github.com/mr-kenhoff/qalculate-android
cd qalculate-android
./gradlew assembleDebug
```

## Features
Features specific to qalculate-android:

* Platform-Native Graphical user interface
* Simple default view
* Optional calculate-as-you-type mode

Features from libqalculate:

* Calculation and parsing:
* Result display:
* Symbolic calculation:
* Functions:
* Units:
* Variables and constants:
* Plotting:
* and more...

_For more details about the syntax, and available functions, units, and variables, please consult the manual (https://qalculate.github.io/manual/)_

