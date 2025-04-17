@echo off
echo Verification de Java...
java -version 2>nul
if errorlevel 1 (
    echo Java Development Kit (JDK) n'est pas installe
    pause
    exit /b 1
)

echo Nettoyage du dossier bin...
if exist bin\* del /Q bin\*

echo Compilation des fichiers Java...
javac -d bin src\*.java
if errorlevel 1 (
    echo Erreur de compilation
    pause
    exit /b 1
)

echo Compilation reussie
echo Lancement de la simulation...
java -cp bin SimulationNuageOiseaux
pause 