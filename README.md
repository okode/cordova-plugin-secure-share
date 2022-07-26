# cordova-plugin-secure-share
Cordova plugin to share private data between apps from the same developer based on signature protection on Android and Keychain on iOS to protect access. Data is saved and retrieved as a flat key-value map.

# Prerequisites

## Android
1. Min sdk version: 21 (Android 5.0 onwards)
2. Applications that need to share information securely must be signed with the same keystore as access to other apps information is protected at signature level
3. Apps must use `cordova-android>=8.0.0`
4. Apps must be compatible with Android-X library system

**Note**: For apps compiling with a targetSdk 30+, it is needed to explicitly declare access to other apps. If we fail to do that, the functionality won't work on Android 11+ devices. In this case, you need to specify which packages are visible in order to access them. To do this, the following `<config-file>` element will be added to the `config.xml` file:

```
...
<platform name="android">
  ...
  <config-file parent="/manifest/queries" target="AndroidManifest.xml">
    <package android:name="com.mapfre.tarjetadesalud" />
    <package android:name="com.mapfre.inversion" />
    <package android:name="com.mapfre.mapfreapp" />
  </config-file>
  ...
</platform>
...
```
Android 11 package visibility: https://developer.android.com/about/versions/11/privacy/package-visibility

## iOS
1. On iOS applications need to share the same Team ID
2. Apps must use `cordova-ios>=5.0.0`

# Install

1. Add the plugin to devDependencies in package.json

```
devDependencies: {
  ...
  "cordova-plugin-secure-share": "github:okode/cordova-plugin-secure-share#1.0.0",
  ...
}
```
2. Add the plugin to the plugins section in package.json

```
"cordova": {
  "plugins": {
    ...
     "cordova-plugin-secure-share": {},
    ...
  }
}
```
3. Regenerate your package-lock.json through npm install and build your apps as usual

# Usage
This plugin provides typescript definition. Wherever you need to use the plugin you can import them as following:

```
import { SecureSharePlugin } from 'cordova-plugin-secure-share/types/index';
declare var SecureShare: SecureSharePlugin;

...

  SecureShare.save(map).catch(error => this.log.e(this, 'Error saving shared user data:', error));
```

## Methods

###  clear

▸ **clear**(): *[Promise]‹void›*

**`description`** Clears any data stored to be shared to other apps.

**`errors`** Error message from the cordova plugin

**Returns:** *[Promise]‹void›*

A promise of void

___

###  retrieve

▸ **retrieve**(): *[Promise]‹[Record]‹string, string››*

**`description`** Get data saved in secure share storage

**`errors`** Error message from the cordova plugin

**Returns:** *[Promise]‹[Record]‹string, string››*

A promise of a string to string map with the data saved. An empty map will be returned if no data is found

___

###  retrieveFrom

▸ **retrieveFrom**(`packageName`: string): *[Promise]‹[Record]‹string, string››*

**`description`** Get data saved in secure share storage

**`errors`** Error message from the cordova plugin

**Parameters:**

Name | Type |
------ | ------ |
`packageName` | string |

**Returns:** *[Promise]‹[Record]‹string, string››*

A promise of a string to string map with the data saved

___

###  save

▸ **save**(`data`: [Record]‹string, string›): *[Promise]‹void›*

**`description`** Save data to be shared to other apps.

**`errors`** Error message from the cordova plugin

**Parameters:**

Name | Type | Description |
------ | ------ | ------ |
`data` | [Record]‹string, string› | string to string map holding the data to be saved  |

**Returns:** *[Promise]‹void›*

A promise of void


