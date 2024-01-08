# LocationSMS: Control Your Android Device's Location and Get Battery Updates via SMS

![image](https://github.com/Hasnain17/Location_battery_through_SMS/assets/62245237/9d6fb716-78d3-483d-9434-124b4c9d8806)

## Overview
LocationSMS is an Android application that enables users to remotely control their device's location settings and receive battery status updates through SMS commands. This innovative solution is designed for situations where you need to manage your device's location or check its battery status without direct physical access.

## Features
- **Remote Location Control**: Turn the device's location on or off by sending an SMS with the body "on" or "off".
- **Battery Status via SMS**: Receive an SMS with the current battery percentage by sending an SMS with the body "battery".
- **Foreground Service**: Runs a foreground service to ensure continuous operation even when the app is not actively open.

## How It Works
1. **SMS Reception**: The app continuously listens for incoming SMS messages.
2. **Command Processing**: Depending on the SMS content ("on", "off", or "battery"), the app performs the corresponding action.
3. **Battery Status**: When requested, the app checks the current battery level and sends it back via SMS.
4. **Location Control**: The app can enable or disable the device's location settings based on the received command.

## Permissions
The application requires the following permissions:
- `android.permission.FOREGROUND_SERVICE`
- `android.permission.INTERNET`
- `android.permission.ACCESS_BACKGROUND_LOCATION`
- `android.permission.READ_SMS`
- `android.permission.RECEIVE_SMS`
- `android.permission.SEND_SMS`
- `android.permission.WRITE_SECURE_SETTINGS` (with a protection level)

## Implementation
- **ForegroundService**: Ensures the app runs continuously in the background.
- **MainActivity**: Handles the UI and main functionalities like saving contacts, showing battery level, and managing location settings.
- **ReceiveSms**: A broadcast receiver that processes incoming SMS messages and triggers actions based on the message content.

## Dependencies
- Gson: `com.google.code.gson:gson:2.8.7`
- Google Location Services: `com.google.android.gms:play-services-location:17.0.0`

## Setup
1. Clone the repository.
2. Open the project in Android Studio.
3. Build the application and install it on your Android device.
4. Ensure that the required permissions are granted.

## Usage
1. Send an SMS with the body "on" to turn on the location.
2. Send an SMS with the body "off" to turn off the location.
3. Send an SMS with the body "battery" to receive the current battery status.

## Security Note
This application should be used responsibly and ethically, considering the privacy and security implications of remotely controlling a device's location and accessing its battery status.

## Contributing
Contributions, issues, and feature requests are welcome. Feel free to check [issues page](link-to-issues-page) for open issues or to open a new issue.



---

> Note: This README is a guideline and should be modified according to the specific requirements and features of your project.
