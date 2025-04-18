#!/bin/bash

# Obtenir le nom de la branche courante
current_branch=$(git branch --show-current)

# Pousser vers le dépôt origin (Hexa-Da)
echo "Pushing to Hexa-Da repository..."
git push origin $current_branch

# Pousser vers le dépôt jafaar
echo "Pushing to Jafaar repository..."
git push jafaar $current_branch

echo "Push completed to both repositories!" 