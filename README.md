# Overview

### Description:
AudioJournal is a fully functional, hardware-integrated Android application that acts as a digital dictaphone. It allows users to capture voice recordings, view a chronological list of their audio logs with exact durations, play them back directly within the app, and manage them using native swipe-to-delete gestures. The app features a modern, responsive dark-themed UI with custom animations and dynamic scrolling.

### Purpose:
I built this application to deepen my understanding of modern Android development using Kotlin and Jetpack Compose. Specifically, my goals were to master declarative UI design, state hoisting, runtime hardware permissions, and bridging the gap between frontend visuals and backend hardware engines (accessing the device's microphone and audio systems).

### Video Demonstration:
[Software Demo Video - Project Walkthrough](http://youtube.link.goes.here)

# Development Environment

### Tools Used:
* Android Studio
* Android Emulator & Physical Device Testing

### Programming Language & Libraries:
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Material 3, Foundation, Animation, Icons)
* **Hardware APIs:** `MediaRecorder` (for capturing audio), `MediaPlayer` (for playback), and `MediaMetadataRetriever` (for extracting file durations)
* **Concurrency:** Kotlin Coroutines (used for the real-time `LaunchedEffect` recording timer)

# Useful Websites

* [Android Developers: Jetpack Compose Basics](https://developer.android.com/develop/ui/compose/documentation)
* [Android Developers: MediaRecorder API](https://developer.android.com/reference/android/media/MediaRecorder)
* [Android Developers: Requesting App Permissions](https://developer.android.com/training/permissions/requesting)
* [Kotlin Official Documentation](https://kotlinlang.org/docs/home.html)

# Future Work

{Make a list of things that you need to fix, improve, and add in the future.}
* Implement an audio visualizer (moving waveforms) that reacts to the user's voice during recording and playback.
* Add a feature allowing users to rename their audio files from the default timestamped names.
* Integrate cloud syncing (such as Firebase) so users can back up their journals across devices.
* Implement a Speech-to-Text to automatically generate text transcriptions of the audio logs.
* Implement an option to rename the audio log.
