POOL CLOCK - cronometro da piscina per Wear OS

COMPILARE L'APK
1. Installa Android Studio (Koala o successivo).
2. File > Open > cartella PoolClock. Attendi la sincronizzazione Gradle.
3. Build > Build Bundle(s) / APK(s) > Build APK(s)
   File: app/build/outputs/apk/debug/app-debug.apk

INSTALLARE SULL'OROLOGIO (Galaxy Watch 4/5/6/7/8, Wear OS)
1. Orologio: Impostazioni > Info su orologio > Info software > tocca 7 volte "Versione software" (abilita opzioni sviluppatore).
2. Impostazioni > Opzioni sviluppatore > attiva "Debug ADB" e "Debug tramite Wi-Fi".
3. Annota IP:porta mostrato. Sul PC (stesso Wi-Fi):
     adb pair IP:PORTA_ABBINAMENTO   (con il codice mostrato)
     adb connect IP:PORTA
     adb install app/build/outputs/apk/debug/app-debug.apk
   In alternativa, in Android Studio scegli l'orologio come dispositivo e premi Run.

USO
- Le 4 lancette (nero, verde, rosso, giallo) girano a 1 giro/min, sfasate di 15 s.
- Tocco: pausa / riprendi.  Pressione lunga: azzera.
- Lo schermo resta acceso mentre l'app e' aperta.
