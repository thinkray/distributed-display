# Text Display
Text Display is a lightweight, open-source program that is designed for displaying text with a background image on the user's screen and the ease of adjusting the text and the image on the fly.

# Prerequisites

* For developers: Java (JDK 8+)
* For users: Java (JRE 8+)

# Setup

There are two ways you can do to get the program running:
1. Download the executable .jar file and run it on your system
    1. Grab the executable .jar file from the "releases" folder;
    1. Make sure you have Java Runtime Environment on your computer. Double click the .jar executable file or use the command line tool 
        > java -jar TextDisplay.jar
    
        and you are ready to go.
1. Compile the project on your own
    * Steps in IntelliJ IDEA
        1. Open the project in IntelliJ IDEA
        1. Select "File"->"Project Structure"
        1. In the "Project Strucutre" window, select "Artifacts" from the left column
        1. Add a new artifact: "+"->"JAR"->"From modules with dependencies..."
        1. Select the main class "GUI" and save the settings
        1. Back to the editor. Go to "Build"->"Build Artifacts..."->"Build"
        1. The executable .jar file will be placed in the folder "out/artifacts/TextDisplay_jar"
        1. Copy the "lang" and "help" folder to the directory

# Getting Started

* Upon your first run, the program will be promptd with a message indicating that no serverSideProfile is found, and a new one will be generated, just click "OK" to continue
* Introduction to the fields of the serverSideProfile's parameters
    1. Profile Name: the name of the serverSideProfile. It doesn't really matter at this point of the development. ;)
    1. Font: The font of the text to be displayed.
    1. Size: The size of the text to be displayed.
    1. Style: The style of the text to be displayed. You may select "Plain," "Bold," or "Italic" according to your preference.
    1. Color: The color of the text to be displayed. Seven default colors are provided. You may choose your own by selecting the "Others" option when all seven colors provided do not satisfy your needs.
    1. Text Direction: The direction of the text. You may select "horizontal", "vertical", or "vertical (inverted)". If you choose "vertical (inverted)," the text displayed will be displayed vertically from right to left.
    1. Vertical Spacing: Only enable when "Text Direction" is selected as "vertical", or "vertical (inverted)." It adjusts the vertical spacing of each character of a given line.
    1. Line Space: The spacing between lines.
    1. Vertical Offset: Adjust the vertical offset.
    1. Horizontal Offset: Adjust the horizontal offset.
    1. Image: The background image.
    1. Fit: The fitting method for the image. You may choose "Fit," "Stretch," or "Tile."
        * Fit: The image will fit the window without changing to ratio.
        * Stretch: The image will fill the entire window. The ratio of the image may be adjusted.
        * Tile: The image will be displayed will any resizing or stretching.
* Introduction to the bottom panels
    * Cancel: Exit the program or return back to the display window (when you've clicked on the "confirm" button before).
    * Preview: Preview the serverSideProfile (the serverSideProfile is not saved).
    * Apply: The current settings will be saved.
    * Confirm: Save the current settings and enter display mode
* Introduction to the menus
    * File
        * Save: Save the current serverSideProfile
        * Exit: Exit the program
    * Language
        * English: Set your language to English
        * 简体中文: Set your language to Simplified Chinese
        * 正體中文: Set your language to Tradition Chinese
    * Help
        * Help: Bring up the current web file
        * About: Show information regarding the application
* Introduction to Display Mode
    * If you 've finished all your settings, click on "confirm" and you are in "Display Mode."
    * If you want to exit the program or change the settings of the displaying contents in "display mdoe," right click (or long press on your touch screen) to bring up the menu.

# Q&A
* What is the difference between "preview" and "display" mode?

    Answer: When you are using the "preview" mode, the current settings won't be saved and you can exit preview mode by simply clicking on the window. In the "display" mode, all settings will be saved before entering the "display" mode.
* The program tells me to reinstall the program upon startup. What should I do?
    Answer: the "lang" folder may be in the wrong directory. Please make sure the folder is correctly placed in the same directory as the executable. If the problem continues to appear, try deleting the config.json file and try to run the program again. If the problem continous to occur, please consider reinstalling the program.
* The program doesn't show up when I try to run it. What should I do?
    Answer: First, please make sure your JVM satisfies the requirement. If you are sure it isn't your problem, please report the "log.txt" file to the developer via GitHub or send @ me at wbh3516@gmail.com.

# Files
    ├── TextDisplay.jar
    ├── serverSideProfile.bin
    ├── config.json
    ├── log.txt
    │   ├── lang
    │   │   ├── en-US.json
    │   │   ├── zh-Hans.json
    │   │   └── zh-Hant.json
    │   ├── help
    │   │   ├── READEME-en-US.html
    │   │   ├── READEME-zh-Hans.html
    │   │   └── READEME-zh-Hant.html
|Name|Description|
|-|-|
|TextDisplay.jar|The executable of the program|
|serverSideProfile.bin|Your current serverSideProfile|
|config.json|The configurations for the program|
|log.txt|The log for the program. The journal for the runtime|
|lang (folder)|The folder that stores all the language packs|
|help (folder)|The folder that stores all the readme (help) files|

# Uninstall
To uninstall the program, simply remove the entire folder, and you are good to go.

# Changelog
## 1.2-Release
1. A new logger is implemented
1. Bug fixes

## 1.1-Release
1. Added support for adjusting vertical spacing, inverting vertical text, change the image fit method
1. Applied color theme Dracula
1. Performance improved when previewing the serverSideProfile
1. Bug fixes

## 1.0-Release
1. Original release

# Denpendencies
|Name|Description|
|-|-|
|commons-io-2.6.jar|For file input and output|
|darcula.jar|The color theme|
|google-collect-snapshot-20080321.jar|For BiHashMap|
|json-20141113.jar|For the program's configuration|

# Copyright
Copyright© 2019-2020 Bohui WU (Bowen, @\_RapDoodle\_)

Email: wbh3516@gmail.com