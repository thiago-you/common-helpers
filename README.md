# Common Helpers
Common helpers to Java/Android Applications.

### Helpers
    - ImageHelper
    - InputHelper
    - DeviceHelper
    - JsonHelper
    - DateHelper
    - PreferencesHelper
    - SecurityHelper
    - VideoHelper
    - Validator

### Requirements
    Min SDK Version >= 19

### Import library from Jitpack
    - Add Jitpack repository into you project (build.gradle):

        allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }

    - Add library implementation into build.gradle (Module:app)

        dependencies {
            ...
            implementation 'com.github.thiago-you:common-helpers:Tag'
        }

    - Sync build.gradle and build your project

See [Jitpack](https://jitpack.io/docs/) docs for more info.

### Download Library
Follow these steps to import the library into your project:

    - Download the library
    - Go to you project under "File" -> "New" -> "Import Module"
    - In build.gradle, import library as "implementation project(':common-helpers')"
    - Sync build.gradle and build your project