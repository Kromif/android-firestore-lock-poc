# StribianApp (Base Version)

A native Android application developed in Kotlin, designed to showcase advanced mobile architecture concepts including **Android Enterprise (Device Owner)** integration and real-time synchronization with **Firebase Firestore**.

This repository contains the core foundation and public reference version of the project, excluding sensitive credentials and business logic.

---

## 🚀 Key Features

* **Android Enterprise Integration:** Leverages `DevicePolicyManager` to manage system-level device owner capabilities and provisioning policies.
* **Real-Time Synchronization:** Utilizes a **Foreground Service** coupled with Firestore **Snapshot Listeners** to react instantly to remote device status changes.

---

## 📱 System Requirements & Compatibility

* **Minimum SDK:** Android 11 (API Level 30) or higher.
* **Target SDK:** Android 14 (API Level 34) / Android 15.
* **Device Provisioning:** Requires a compatible test device configured with **Device Owner** privileges (typically provisioned via NFC bump, QR code during factory reset, or Android Debug Bridge/ADB shell command).

---

## 🛠️ Tech Stack

* **Language:** Kotlin
* **Platform:** Android (Android Enterprise / Device Administration)
* **Backend / Database:** Firebase Firestore

---

## ⚙️ Setup & Installation

1. Clone the repository:
   ```bash
   git clone [https://github.com/Kromif/stribian-app-core.git](https://github.com/Kromif/stribian-app-core.git)

2. Add your own google-services.json file inside the app/ directory (required for Firebase connectivity).

3. Build and run the project on a test device or emulator configured with Device Owner privileges.
