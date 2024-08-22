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

## Contributors

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/jherkenhoff"><img src="https://avatars.githubusercontent.com/u/22686781?v=4?s=100" width="100px;" alt="jherkenhoff"/><br /><sub><b>jherkenhoff</b></sub></a><br /><a href="#code-jherkenhoff" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://www.bytehamster.com"><img src="https://avatars.githubusercontent.com/u/5811634?v=4?s=100" width="100px;" alt="ByteHamster"/><br /><sub><b>ByteHamster</b></sub></a><br /><a href="#code-ByteHamster" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->
