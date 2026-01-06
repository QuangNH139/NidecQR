# App Icons

This directory should contain the launcher icon PNG files.

For proper app icons, you should generate icons using Android Studio:
1. Right-click on `res` folder
2. New → Image Asset
3. Configure your icon
4. Generate all densities

Icon sizes:
- mdpi: 48x48 px
- hdpi: 72x72 px
- xhdpi: 96x96 px
- xxhdpi: 144x144 px
- xxxhdpi: 192x192 px

For now, the app uses adaptive icon XML resources defined in:
- mipmap-anydpi-v26/ic_launcher.xml
- mipmap-anydpi-v26/ic_launcher_round.xml
- drawable/ic_launcher_foreground.xml

The app will build and run with these vector drawables.
