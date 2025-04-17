#!/bin/bash

# Vérifier si Java est installé
if ! command -v javac &> /dev/null; then
    echo "Java Development Kit (JDK) n'est pas installé"
    exit 1
fi

# Se placer dans le dossier du script
cd "$(dirname "$0")"

# Nettoyer le dossier bin
rm -rf bin/*

# Compiler tous les fichiers Java
echo "Compilation des fichiers Java..."
javac -d bin src/*.java

# Vérifier si la compilation a réussi
if [ $? -eq 0 ]; then
    echo "Compilation réussie"
    # Exécuter le programme
    echo "Lancement de la simulation..."
    java -cp bin SimulationNueeOiseaux
else
    echo "Erreur de compilation"
    exit 1
fi 